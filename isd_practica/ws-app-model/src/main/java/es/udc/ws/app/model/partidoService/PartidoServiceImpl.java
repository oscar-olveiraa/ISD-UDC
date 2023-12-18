package es.udc.ws.app.model.partidoService;


import es.udc.ws.app.model.compra.Compra;
import es.udc.ws.app.model.compra.SqlCompraDao;
import es.udc.ws.app.model.compra.SqlCompraDaoFactory;
import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partido.SqlPartidoDao;
import es.udc.ws.app.model.partido.SqlPartidoDaoFactory;
import es.udc.ws.app.model.partidoService.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;


import javax.sql.DataSource;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;

public class PartidoServiceImpl implements PartidoService {

    private final DataSource dataSource;
    private SqlPartidoDao partidoDao=null;
    private SqlCompraDao compraDao=null;
    //private static final String formato="yyyy-MM-dd'T'HH:mm";

    public PartidoServiceImpl(){
            dataSource=DataSourceLocator.getDataSource(APP_DATA_SOURCE);
            partidoDao=SqlPartidoDaoFactory.getDao();
            compraDao= SqlCompraDaoFactory.getDao();
    }

    public static void validarFecha(LocalDateTime fechaActual, LocalDateTime fechaPosterior) throws InputValidationException {
        if(fechaPosterior.isBefore(fechaActual)){
            throw new InputValidationException("La fecha introducida es anterior a la actual");
        }
    }

    public static void validarFechaHora(String fecha) throws InputValidationException{
            String[] fechaHora = fecha.split("T");
            String[] partefecha = fechaHora[0].split("-");
            String[] parteHora = fechaHora[1].split(":");

            int ano = Integer.parseInt(partefecha[0]);
            int mes = Integer.parseInt(partefecha[1]);
            int dia = Integer.parseInt(partefecha[2]);

            int hora = Integer.parseInt(parteHora[0]);
            int minutos = Integer.parseInt(parteHora[1]);
            //int segundos = Integer.parseInt(partefecha[5]);

            if (ano < 1000 || ano > 9999) {
                throw new InputValidationException("El año no es valido");
            }

            if(mes < 1 || mes > 12){
                throw new InputValidationException("El mes no es valido");
            }

            if((mes == 4 || mes == 6 || mes == 9 || mes == 11) && (dia < 1 || dia > 30)){
                throw new InputValidationException("El dia no es valido para el mes");
            }

            if((mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 ||mes == 12) && (dia < 1 || dia > 31)){
                throw new InputValidationException("El dia no es valido para el mes");
            }

            if(hora < 0 || hora > 23){
                throw new InputValidationException("La hora no es valida");
            }

            if(minutos < 0 || minutos > 60){
                throw new InputValidationException("Los minutos no son validos");
            }

            /*if(segundos < 0 || segundos > 60){
                throw new InputValidationException("Los segundos no son validos");
            }*/

    }

    private void validarEmail(String email) throws  InputValidationException{
        if(email==null){
            throw new InputValidationException("El campo email es nulo");
        }
        int contadorArroba = 0;
        for (int i=0; i < email.length(); i++){
            char c = email.charAt(i);
            if(c=='@'){
                contadorArroba++;
            }
        }

        if(contadorArroba!=1){
            throw new InputValidationException("La cantidad de '@' tiene que ser 1");
        }
        String[] partes = email.split("@");
        if(partes[0].equals("") || partes[1].equals("")){
            throw new InputValidationException("Error de formato en el email");
        }
    }

    private void validatePartido(Partido partido) throws InputValidationException{
        PropertyValidator.validateMandatoryString("name", partido.getNombre());
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        String strLocalDateTime = partido.getFechaPartido().toString(); //antes .format(formatter)
        validarFechaHora(strLocalDateTime);
        validarFecha(LocalDateTime.now().withNano(0), partido.getFechaPartido());
        PropertyValidator.validateDouble("price", partido.getPrecio(), 0, MAX_PRICE);
        PropertyValidator.validateLong("maxTickets", partido.getMaxEntradas(), 1, MAX_TICKETS);
        PropertyValidator.validateLong("soldTickets", partido.getEntradasVendidas(), 0, MAX_TICKETS);
    }

    private void validateCompra(Long idPartido, String email, String tarjeta, int cantidad) throws InvalidMatchDateException,InputValidationException, InstanceNotFoundException, NotEnoughTicketsException{
        PropertyValidator.validateCreditCard(tarjeta);
        validarEmail(email);
        if(cantidad<=0){
            throw new InputValidationException("No puedes comprar 0 o menos entradas");
        }
        try(Connection connection = dataSource.getConnection()){
            Partido partidoTest = partidoDao.find(connection, idPartido);

            if(partidoTest.getFechaPartido().isBefore(LocalDateTime.now())){
                throw new InvalidMatchDateException(idPartido, partidoTest.getFechaPartido());
            }

            try {
                PropertyValidator.validateLong("maxTickets", cantidad, 1,
                        partidoTest.getMaxEntradas() - partidoTest.getEntradasVendidas());
            }
            catch (Exception e){
                throw new NotEnoughTicketsException(idPartido, partidoTest.getMaxEntradas()-partidoTest.getEntradasVendidas());
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void validatePickup(Long idCompra, String tarjetaValidable) throws InstanceNotFoundException, WrongCreditCardException, TicketAlreadyGivenException{
        try(Connection connection = dataSource.getConnection()){
            Compra compra = compraDao.find(connection,idCompra);

            if(!tarjetaValidable.equals(compra.getTarjeta())){
                throw new WrongCreditCardException(idCompra);
            }
            if(compra.isRecogida()){
                throw new TicketAlreadyGivenException(idCompra);
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Partido addPartido(Partido partido) throws InputValidationException{
        validatePartido(partido);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        LocalDateTime now = LocalDateTime.now().withNano(0);
        String ahora = now.toString(); //antes .format(formatter)
        LocalDateTime fechaBuena = LocalDateTime.parse(ahora);
        partido.setFechaAlta(fechaBuena);

        try(Connection connection=dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Partido createdPartido = partidoDao.create(connection, partido);

                connection.commit();

                return createdPartido;

            }catch(SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            }catch(RuntimeException | Error e){
                connection.rollback();
                throw e;
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Partido> findByDate(LocalDateTime fechaIni, LocalDateTime fechaFin) throws InputValidationException{
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        validarFechaHora(fechaIni.toString()); //antes .format(formatter)
        validarFechaHora(fechaFin.toString()); //antes .format(formatter)
        validarFecha(fechaIni,fechaFin);
        try(Connection connection = dataSource.getConnection()){
            return partidoDao.findByDate(connection,fechaIni, fechaFin);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Partido findById(Long idPartido) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            return partidoDao.find(connection, idPartido);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Compra buyTicket(Long idPartido, String email, String tarjeta, int cantidad) throws InvalidMatchDateException,InstanceNotFoundException, InputValidationException, NotEnoughTicketsException{

        validateCompra(idPartido,email, tarjeta, cantidad);

        try (Connection connection = dataSource.getConnection()) {

            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Partido partido = partidoDao.find(connection, idPartido);
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
                LocalDateTime now = LocalDateTime.now().withNano(0);
                String ahora = now.toString(); //antes .format(formatter)
                LocalDateTime fechaBuena = LocalDateTime.parse(ahora);
                partido.setFechaAlta(fechaBuena);
                Compra compra = compraDao.create(connection, new Compra(email, tarjeta, cantidad,
                        fechaBuena, partido.getIdPartido()));

                partido.setEntradasVendidas(partido.getEntradasVendidas() + cantidad);
                partidoDao.update(connection,partido);

                connection.commit();

                return compra;

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Compra> findPurchase(String email) throws InputValidationException{
        validarEmail(email);
        try(Connection connection = dataSource.getConnection()){
            return compraDao.findByUser(connection,email);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pickupTicket(Long idCompra, String tarjetaValidable) throws InstanceNotFoundException, TicketAlreadyGivenException, WrongCreditCardException{

        validatePickup(idCompra, tarjetaValidable);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Compra compra = compraDao.find(connection,idCompra);
                compra.setRecogida(true);
                compraDao.update(connection,compra);

                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

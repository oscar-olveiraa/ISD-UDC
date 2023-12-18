package es.udc.ws.app.test.model.appservice;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import es.udc.ws.app.model.compra.Compra;
import es.udc.ws.app.model.compra.SqlCompraDao;
import es.udc.ws.app.model.compra.SqlCompraDaoFactory;
import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partido.SqlPartidoDao;
import es.udc.ws.app.model.partido.SqlPartidoDaoFactory;
import es.udc.ws.app.model.partidoService.PartidoService;
import es.udc.ws.app.model.partidoService.PartidoServiceFactory;
import es.udc.ws.app.model.partidoService.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppServiceTest {
    private final long NON_EXISTENT_PARTIDO_ID = -1;
    private final long NON_EXISTENT_COMPRA_ID = -1;
    private final String EMAIL = "pepe57@udc.es";
    private final String INVALID_EMAIL = "@juan";
    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";

    private final String WRONG_CREDIT_CARD_NUMBER = "1234567890123457";
    private final String INVALID_CREDIT_CARD_NUMBER = "";
    private static PartidoService partidoService = null;
    private static SqlCompraDao compraDao = null;

    private static SqlPartidoDao partidoDao = null;

    /*@BeforeAll
    public static void init() {

        DataSource dataSource = new SimpleDataSource();

        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        partidoService = PartidoServiceFactory.getService();

        compraDao = SqlCompraDaoFactory.getDao();

        partidoDao = SqlPartidoDaoFactory.getDao();
    }
    private Partido getValidMatch() {

        return getValidMatch(LocalDateTime.of(2024,3,22,16,30,0));
    }
    private Partido getValidMatch(LocalDateTime fecha){
        return  new Partido("Barcelona", fecha, 17.99f, 40000, 0);
    }
    private Partido createMatch(Partido partido) {

        Partido addedPartido = null;
        try {
            addedPartido = partidoService.addPartido(partido);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedPartido;

    }
    private void removeMatch(Long partidoId) {

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                partidoDao.remove(connection, partidoId);
                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void removePurchase(Long idCompra) {

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                compraDao.remove(connection, idCompra);

                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private Compra findPurchase(Long idCompra) throws InstanceNotFoundException {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try(Connection connection = dataSource.getConnection()){
            return compraDao.find(connection,idCompra);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testAddMatchAndFindMatch() throws InputValidationException, InstanceNotFoundException {

        Partido partido = getValidMatch();
        Partido addedMatch = null;

        try {

            // Create Match
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

            addedMatch = partidoService.addPartido(partido);

            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            Partido foundMatch = partidoService.findById(addedMatch.getIdPartido());

            assertEquals(addedMatch, foundMatch);
            assertEquals(foundMatch.getNombre(),partido.getNombre());
            assertEquals(foundMatch.getFechaPartido(),partido.getFechaPartido());
            assertEquals(foundMatch.getPrecio(),partido.getPrecio());
            assertEquals(foundMatch.getMaxEntradas(),partido.getMaxEntradas());
            assertTrue((foundMatch.getFechaAlta().compareTo(beforeCreationDate) >= 0)
                    && (foundMatch.getFechaAlta().compareTo(afterCreationDate) <= 0));
            assertEquals(foundMatch.getEntradasVendidas(),partido.getEntradasVendidas());

        } finally {
            // Clear Database
            if (addedMatch!=null) {
                removeMatch(addedMatch.getIdPartido());
            }
        }
    }


    @Test
    public void testFindMatches() throws InputValidationException {

        List<Partido> partidos = new LinkedList<Partido>();

        Partido partido4 = createMatch(getValidMatch(LocalDateTime.of(2021,8,4,22,30,0)));
        partidos.add(partido4);

        Partido partido2 = createMatch(getValidMatch(LocalDateTime.of(2022,1,2,21,0,0)));
        partidos.add(partido2);

        Partido partido5 = createMatch(getValidMatch(LocalDateTime.of(2022,4,6,19,30,0)));
        partidos.add(partido5);

        Partido partido1 = createMatch(getValidMatch(LocalDateTime.of(2022,11,2,20,30,0)));
        partidos.add(partido1);

        Partido partido3 = createMatch(getValidMatch(LocalDateTime.of(2023,5,6,17,30,0)));
        partidos.add(partido3);

        try {
            List<Partido> foundMatches = partidoService.findByDate(LocalDateTime.of(2010,1,1,20,0,0), LocalDateTime.of(2030,1,1,20,0,0));
            assertEquals(partidos, foundMatches);
        } finally {
            for (Partido partido : partidos) {
                removeMatch(partido.getIdPartido());
            }
        }
    }


    @Test
    public void testAddInvalidMatch() {
        //nombre sea nulo
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidMatch();
            partido.setNombre(null);
            Partido addedMatch = partidoService.addPartido(partido);
            removeMatch(addedMatch.getIdPartido());
        });

        //nombre sea vacio
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidMatch();
            partido.setNombre(" ");
            Partido addedMatch = partidoService.addPartido(partido);
            removeMatch(addedMatch.getIdPartido());
        });

        // Mirar que la fecha tenga un formato valido
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidMatch();
            partido.setFechaPartido(LocalDateTime.of(0,4,5,4,4,4));
            Partido addedMatch = partidoService.addPartido(partido);
            removeMatch(addedMatch.getIdPartido());
        });

        // Chequear que el precio no sea menor 0
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidMatch();
            partido.setPrecio(-9);
            Partido addedMatch = partidoService.addPartido(partido);
            removeMatch(addedMatch.getIdPartido());
        });

        // Chequear que el precio no sea mayor que el maximo precio
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidMatch();
            partido.setPrecio(1001);
            Partido addedMatch = partidoService.addPartido(partido);
            removeMatch(addedMatch.getIdPartido());
        });

        // Chequear que el maxEntradas no sea menor 0
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidMatch();
            partido.setMaxEntradas(-9);
            Partido addedMatch = partidoService.addPartido(partido);
            removeMatch(addedMatch.getIdPartido());
        });

        // Chequear que el maxEntradas no sea mayor que el maximo de entradas
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidMatch();
            partido.setMaxEntradas(50001);
            Partido addedMatch = partidoService.addPartido(partido);
            removeMatch(addedMatch.getIdPartido());
        });
        // Mirar que entradasVendidas no sea menor que 0
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidMatch();
            partido.setEntradasVendidas(-9);
            Partido addedMatch = partidoService.addPartido(partido);
            removeMatch(addedMatch.getIdPartido());
        });

        // Mirar que entradasVendidas no sea mayor que el maximo de entradas
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidMatch();
            partido.setEntradasVendidas(50001);
            Partido addedMatch = partidoService.addPartido(partido);
            removeMatch(addedMatch.getIdPartido());
        });
    }

    @Test
    public void testFindNonExistentMatch(){
        assertThrows(InstanceNotFoundException.class, () -> partidoService.findById(NON_EXISTENT_PARTIDO_ID));
    }

    @Test
    public void testBuyTicket()
            throws InvalidMatchDateException,InstanceNotFoundException, InputValidationException,
            NotEnoughTicketsException{

        Partido partido = createMatch(getValidMatch());
        Partido partidoTest = null;
        Compra compra = null;

        try {
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);
            compra = partidoService.buyTicket(partido.getIdPartido(), EMAIL, VALID_CREDIT_CARD_NUMBER, 2);
            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            Compra compraTest = findPurchase(compra.getIdCompra());
            assertEquals(compra, compraTest);
            assertEquals(compraTest.getIdCompra(),compra.getIdCompra());
            assertEquals(compraTest.getEmail(),compra.getEmail());
            assertEquals(compraTest.getTarjeta(),compra.getTarjeta());
            assertEquals(compraTest.getCantidad(),compra.getCantidad());
            assertTrue((compraTest.getFechaCompra().compareTo(beforeCreationDate) >= 0)
                    && (compra.getFechaCompra().compareTo(afterCreationDate) <= 0));


            partidoTest = partidoService.findById(partido.getIdPartido());
            assertEquals(partidoTest.getEntradasVendidas(), partido.getEntradasVendidas() + 2);

        } finally {
            // Clear database: remove sale (if created) and movie
            if (compra != null) {
                removePurchase(compra.getIdCompra());
            }
            removeMatch(partido.getIdPartido());
        }
    }

    @Test
    public void testInvalidPurchase(){
        // Invalid Email
        Partido partido1 = createMatch(getValidMatch());
        try {
            assertThrows(InputValidationException.class, () -> {
                Compra addedPurchase = partidoService.buyTicket(partido1.getIdPartido(),
                        INVALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 2);
                removePurchase(addedPurchase.getIdCompra());
            });
        }finally{
            removeMatch(partido1.getIdPartido());
        }
        // Invalid Credit Card Number
        Partido partido2 = createMatch(getValidMatch());
        try {
            assertThrows(InputValidationException.class, () -> {

                Compra addedPurchase = partidoService.buyTicket(partido2.getIdPartido(),
                        EMAIL, INVALID_CREDIT_CARD_NUMBER, 2);
                removePurchase(addedPurchase.getIdCompra());
            });
        }finally{
            removeMatch(partido2.getIdPartido());
        }
        // Invalid  entradas (<)
        Partido partido3 = createMatch(getValidMatch());
        try {
            assertThrows(InputValidationException.class, () -> {

                Compra addedPurchase = partidoService.buyTicket(partido3.getIdPartido(),
                        EMAIL, VALID_CREDIT_CARD_NUMBER, 0);
                removePurchase(addedPurchase.getIdCompra());
            });
        }finally{
            removeMatch(partido3.getIdPartido());
        }
        // Invalid entradas (>)
        Partido partido4 = createMatch(getValidMatch());
        try {
            assertThrows(NotEnoughTicketsException.class, () -> {
                partido4.setEntradasVendidas(2000);
                Compra addedPurchase = partidoService.buyTicket(partido4.getIdPartido(),
                        EMAIL, VALID_CREDIT_CARD_NUMBER, 49000);
                removePurchase(addedPurchase.getIdCompra());
            });
        }finally{
            removeMatch(partido4.getIdPartido());
        }
        // Invalid fecha
        Partido partido5 = createMatch(getValidMatch(LocalDateTime.of(2022,3,4,17,30,0)));
        try {
            assertThrows(InvalidMatchDateException.class, () -> {
                Compra addedPurchase = partidoService.buyTicket(partido5.getIdPartido(),
                        EMAIL, VALID_CREDIT_CARD_NUMBER, 5);
                removePurchase(addedPurchase.getIdCompra());
            });
        }finally{
            removeMatch(partido5.getIdPartido());
        }
    }

    @Test
    public void testPickup() throws TicketAlreadyGivenException, InstanceNotFoundException,
            NotEnoughTicketsException, InputValidationException, InvalidMatchDateException, WrongCreditCardException {

        Partido partido = createMatch(getValidMatch());
        Compra compra = null;

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try(Connection connection = dataSource.getConnection()){
            compra = partidoService.buyTicket(partido.getIdPartido(), EMAIL, VALID_CREDIT_CARD_NUMBER, 1);

            partidoService.pickupTicket(compra.getIdCompra(), VALID_CREDIT_CARD_NUMBER);

            compra = findPurchase(compra.getIdCompra());

            assertEquals(compra.isRecogida(), true);


        }catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (compra != null) {
                removePurchase(compra.getIdCompra());
            }
            removeMatch(partido.getIdPartido());
        }
    }

    @Test
    public void testInvalidPickup(){
        Partido partido = createMatch(getValidMatch());
        try {
            assertThrows(WrongCreditCardException.class, () -> {

                Compra compra = partidoService.buyTicket(partido.getIdPartido(), EMAIL, VALID_CREDIT_CARD_NUMBER, 1);
                partidoService.pickupTicket(compra.getIdCompra(), WRONG_CREDIT_CARD_NUMBER);
                removePurchase(compra.getIdCompra());
            });
        }finally{
            removeMatch(partido.getIdPartido());
        }

        Partido partido2 = createMatch(getValidMatch());
        try {
            assertThrows(TicketAlreadyGivenException.class, () -> {
                DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
                try(Connection connection = dataSource.getConnection()) {
                    Compra compra = partidoService.buyTicket(partido2.getIdPartido(), EMAIL, VALID_CREDIT_CARD_NUMBER, 1);
                    compra.setRecogida(true);
                    compraDao.update(connection, compra);
                    partidoService.pickupTicket(compra.getIdCompra(), VALID_CREDIT_CARD_NUMBER);
                    removePurchase(compra.getIdCompra());
                }
            });
        }finally{
            removeMatch(partido2.getIdPartido());
        }
    }

    @Test
    public void testPickupNotExistentPurchase(){
        assertThrows(InstanceNotFoundException.class, () -> findPurchase(NON_EXISTENT_COMPRA_ID));
    }

    @Test
    public void testFindPurchase() throws InstanceNotFoundException,
            NotEnoughTicketsException, InputValidationException, InvalidMatchDateException{

        Partido partido = createMatch(getValidMatch());

        Compra compra_1 = null;
        Compra compra_2 = null;
        Compra compra_3 = null;

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try(Connection connection = dataSource.getConnection()){
            compra_1 = partidoService.buyTicket(partido.getIdPartido(), EMAIL, VALID_CREDIT_CARD_NUMBER, 1);
            compra_2 = partidoService.buyTicket(partido.getIdPartido(), EMAIL, VALID_CREDIT_CARD_NUMBER, 1);
            compra_3 = partidoService.buyTicket(partido.getIdPartido(), "oscar_olvera_minino@udc.es", VALID_CREDIT_CARD_NUMBER, 1);

            List<Compra> ComprasEjemplo = new ArrayList<>();

            ComprasEjemplo.add(compra_1);
            ComprasEjemplo.add(compra_2);

            assertEquals(partidoService.findPurchase(EMAIL),ComprasEjemplo);

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (compra_1 != null) {
                removePurchase(compra_1.getIdCompra());
            }
            if (compra_2 != null) {
                removePurchase(compra_2.getIdCompra());
            }
            if (compra_3 != null) {
                removePurchase(compra_3.getIdCompra());
            }
            removeMatch(partido.getIdPartido());
        }
    }*/
}

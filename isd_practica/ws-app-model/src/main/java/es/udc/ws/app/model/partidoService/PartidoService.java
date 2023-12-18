package es.udc.ws.app.model.partidoService;

import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.compra.Compra;
import es.udc.ws.app.model.partidoService.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface PartidoService {

    public Partido addPartido(Partido partido) throws InputValidationException;

    public List<Partido> findByDate(LocalDateTime fechaIni, LocalDateTime fechaFin) throws InputValidationException;

    public Partido findById(Long idPartido) throws InstanceNotFoundException;

    public Compra buyTicket(Long idPartido, String email, String tarjeta, int cantidad)
            throws InvalidMatchDateException,InputValidationException,InstanceNotFoundException, NotEnoughTicketsException;

    public List<Compra> findPurchase(String email) throws InputValidationException;

    public void pickupTicket(Long idCompra, String tarjetaValidable) throws InputValidationException,
            InstanceNotFoundException, TicketAlreadyGivenException, WrongCreditCardException;

}

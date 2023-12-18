package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientCompraDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientInvalidMatchDateException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughTicketsException;
import es.udc.ws.app.client.service.exceptions.ClientTicketAlreadyGivenException;
import es.udc.ws.app.client.service.exceptions.ClientWrongCreditCardException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientPartidoService {

    public Long addPartido(ClientPartidoDto partido) throws InputValidationException;
    public List<ClientPartidoDto> findByDate(LocalDateTime fecha) throws InputValidationException;
    public ClientPartidoDto findById(Long idPartido) throws InstanceNotFoundException;
    public Long buyTicket(Long idPartido, String email, String tarjeta, int cantidad)
            throws ClientInvalidMatchDateException, InstanceNotFoundException, InputValidationException, ClientNotEnoughTicketsException;
    public List<ClientCompraDto> findPurchase(String email) throws InputValidationException;
    public void pickupTicket(Long idCompra, String tarjetaValidable) throws InputValidationException,
            InstanceNotFoundException, ClientTicketAlreadyGivenException, ClientWrongCreditCardException;


}



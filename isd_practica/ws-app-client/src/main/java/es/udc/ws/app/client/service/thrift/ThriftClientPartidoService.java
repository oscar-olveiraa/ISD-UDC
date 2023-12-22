package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.dto.ClientCompraDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientInvalidMatchDateException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughTicketsException;
import es.udc.ws.app.client.service.exceptions.ClientTicketAlreadyGivenException;
import es.udc.ws.app.client.service.exceptions.ClientWrongCreditCardException;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ThriftClientPartidoService implements ClientPartidoService {
    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientPartidoService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    @Override
    public Long addPartido(ClientPartidoDto partido) throws InputValidationException {

        ThriftPartidoService.Client client = getClient();


        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return client.addPartido(ClientPartidoDtoToThriftPartidoDtoConversor.toThriftPartidoDto(partido)).getIdPartido();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientPartidoDto> findByDate(LocalDateTime fecha) throws InputValidationException {
        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientPartidoDtoToThriftPartidoDtoConversor.toClientPartidoDtos(client.findByDate(fecha.toString()));

        }catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientPartidoDto findById(Long idPartido) throws InstanceNotFoundException {
        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientPartidoDtoToThriftPartidoDtoConversor.toClientPartidoDto(client.findById(idPartido));

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long buyTicket(Long idPartido, String email, String tarjeta, int cantidad) throws ClientInvalidMatchDateException, InstanceNotFoundException, InputValidationException, ClientNotEnoughTicketsException {

        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return client.buyTicket(idPartido, email, tarjeta, cantidad).getIdCompra();

        } catch (ThriftInvalidMatchDateException e) {
            throw new ClientInvalidMatchDateException(e.getIdPartido(), LocalDateTime.parse(e.getFechaPartido()));
        } catch (ThriftNotEnoughTicketsException e){
            throw new ClientNotEnoughTicketsException(e.getIdPartido(), e.getCantidad());
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientCompraDto> findPurchase(String email) throws InputValidationException {
        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientCompraDtoToThriftCompraDtoConversor.toClientCompraDtos(client.findPurchase(email));

        }catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pickupTicket(Long idCompra, String tarjetaValidable) throws InputValidationException, InstanceNotFoundException, ClientTicketAlreadyGivenException, ClientWrongCreditCardException {

        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            client.pickupTicket(idCompra,tarjetaValidable);

        }catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        }catch (ThriftInstanceNotFoundException e){
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }catch (ThriftTicketAlreadyGivenException e){
            throw new ClientTicketAlreadyGivenException(idCompra);
        }catch (ThriftWrongCreditCardException e){
            throw new ClientWrongCreditCardException(idCompra);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ThriftPartidoService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftPartidoService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
}

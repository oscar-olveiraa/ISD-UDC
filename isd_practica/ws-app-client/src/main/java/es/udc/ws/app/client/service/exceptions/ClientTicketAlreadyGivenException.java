package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientTicketAlreadyGivenException extends Exception {

    private Long idCompra;
    public ClientTicketAlreadyGivenException(Long idCompra) {
        super("Para la compra con id=\"" + idCompra +
                "\" ya han sido entregados los tickets");
        this.idCompra = idCompra;
    }

    public Long getIdCompra() {
        return idCompra;
    }
    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }
}

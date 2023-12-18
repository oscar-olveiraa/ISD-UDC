package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientNotEnoughTicketsException extends Exception{

    private Long idPartido;

    private int cantidad;


    public ClientNotEnoughTicketsException(Long idPartido, int cantidad) {
        super("Al partido con id=\"" + idPartido +
                "\" solo le quedan disponibles " +
                cantidad + " entradas.\n");
        this.idPartido = idPartido;
        this.cantidad = cantidad;
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

}

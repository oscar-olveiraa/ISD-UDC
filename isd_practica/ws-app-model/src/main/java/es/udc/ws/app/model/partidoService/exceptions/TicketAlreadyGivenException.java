package es.udc.ws.app.model.partidoService.exceptions;

public class TicketAlreadyGivenException extends Exception{
    private Long idCompra;
    public TicketAlreadyGivenException(Long idCompra) {
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

package es.udc.ws.app.model.partidoService.exceptions;

public class WrongCreditCardException extends Exception{
    private Long idCompra;
    public WrongCreditCardException(Long idCompra) {
        super("Para la compra con id=\"" + idCompra +
                "\" se utilizo una tarjeta distinta");
        this.idCompra = idCompra;
    }

    public Long getIdCompra() {
        return idCompra;
    }
    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }
}

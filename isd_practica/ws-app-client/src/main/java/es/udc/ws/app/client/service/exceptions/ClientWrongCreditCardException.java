package es.udc.ws.app.client.service.exceptions;

public class ClientWrongCreditCardException extends Exception{

    private Long idCompra;
    public ClientWrongCreditCardException(Long idCompra) {
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

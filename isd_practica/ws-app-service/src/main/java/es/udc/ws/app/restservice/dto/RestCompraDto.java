package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestCompraDto {
    private Long idCompra;
    private String email;
    private String tarjeta;
    private int cantidad;
    private String fechaCompra;
    private boolean recogida;
    private Long idPartido;
    public RestCompraDto(){}
    public RestCompraDto(Long idCompra, String email, String tarjeta, int cantidad, String fechaCompra, Long idPartido, boolean recogida) {
        this.idCompra=idCompra;
        this.email = email;
        this.tarjeta = tarjeta;
        this.cantidad = cantidad;
        this.fechaCompra = fechaCompra;
        this.idPartido = idPartido;
        this.recogida=recogida;
    }


    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public boolean isRecogida() {
        return recogida;
    }

    public void setRecogida(boolean recogida) {
        this.recogida = recogida;
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

}

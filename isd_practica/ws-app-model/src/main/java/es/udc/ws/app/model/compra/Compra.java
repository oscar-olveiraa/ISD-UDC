package es.udc.ws.app.model.compra;

import java.util.Objects;
import java.time.LocalDateTime;
import java.util.Locale;

public class Compra {

    private Long idCompra;
    private String email;
    private String tarjeta;

    private int cantidad;
    private LocalDateTime fechaCompra;

    private boolean recogida;
    private Long idPartido;

    public Compra(String email, String tarjeta, int cantidad, LocalDateTime fechaCompra, Long idPartido) {
        this.email = email;
        this.tarjeta = tarjeta;
        this.cantidad = cantidad;
        this.fechaCompra = (fechaCompra != null) ? fechaCompra.withNano(0) : null;
        this.idPartido = idPartido;
        recogida = false;
    }

    public Compra(Long idCompra, String email, String tarjeta, int cantidad, LocalDateTime fechaCompra, Long idPartido, boolean recogida) {
        this(email, tarjeta, cantidad, fechaCompra, idPartido);
        this.idCompra = idCompra;
        this.recogida = recogida;
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

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = (fechaCompra != null) ? fechaCompra.withNano(0) : null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return cantidad == compra.cantidad && recogida == compra.recogida && Objects.equals(idCompra, compra.idCompra) && Objects.equals(email, compra.email) && Objects.equals(tarjeta, compra.tarjeta) && Objects.equals(fechaCompra, compra.fechaCompra) && Objects.equals(idPartido, compra.idPartido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompra, email, tarjeta, cantidad, fechaCompra, recogida, idPartido);
    }
}

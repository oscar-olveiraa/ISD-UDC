package es.udc.ws.app.model.partido;

import jakarta.servlet.http.Part;

import java.nio.channels.FileLock;
import java.time.LocalDateTime;
import java.util.Objects;

public class Partido {
    private Long idPartido;
    private String nombre;
    private LocalDateTime fechaPartido;
    private float precio;
    private int maxEntradas;
    private LocalDateTime fechaAlta;
    private int entradasVendidas;

    public Partido(String nombre, LocalDateTime fechaPartido, float precio, int maxEntradas, int entradasVendidas){
        this.nombre=nombre;
        this.fechaPartido= (fechaPartido != null) ? fechaPartido.withNano(0) : null;
        this.precio=precio;
        this.maxEntradas=maxEntradas;
        this.entradasVendidas=entradasVendidas;
    }


    public Partido(Long idPartido, String nombre, LocalDateTime fechaPartido, float precio, int maxEntradas, int entradasVendidas){
        this(nombre,fechaPartido,precio,maxEntradas,entradasVendidas);
        this.idPartido=idPartido;
    }

    public Partido(Long idPartido, String nombre, LocalDateTime fechaPartido, float precio, int maxEntradas, int entradasVendidas, LocalDateTime fechaAlta){
        this(idPartido,nombre,fechaPartido,precio,maxEntradas,entradasVendidas);
        this.fechaAlta= (fechaAlta != null) ? fechaAlta.withNano(0) : null;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaPartido() {
        return fechaPartido;
    }
    public void setFechaPartido(LocalDateTime fechaPartido) {
        this.fechaPartido= (fechaPartido != null) ? fechaPartido.withNano(0) : null;
    }

    public float getPrecio() {
        return precio;
    }
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getMaxEntradas() {
        return maxEntradas;
    }
    public void setMaxEntradas(int maxEntradas) {
        this.maxEntradas = maxEntradas;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }
    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta= (fechaAlta != null) ? fechaAlta.withNano(0) : null;
    }

    public Long getIdPartido() {
        return idPartido;
    }
    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

    public int getEntradasVendidas() {
        return entradasVendidas;
    }
    public void setEntradasVendidas(int entradasVendidas) {
        this.entradasVendidas=entradasVendidas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partido partido = (Partido) o;
        return Float.compare(partido.precio, precio) == 0 && maxEntradas == partido.maxEntradas && entradasVendidas == partido.entradasVendidas && Objects.equals(idPartido, partido.idPartido) && Objects.equals(nombre, partido.nombre) && Objects.equals(fechaPartido, partido.fechaPartido) && Objects.equals(fechaAlta, partido.fechaAlta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPartido, nombre, fechaPartido, precio, maxEntradas, fechaAlta, entradasVendidas);
    }
}

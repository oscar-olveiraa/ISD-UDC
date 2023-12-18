package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientPartidoDto {
    private Long idPartido;
    private String nombre;
    private LocalDateTime fechaPartido;
    private float precio;
    private int maxEntradas;
    private int entradasDisponibles;

    public ClientPartidoDto(){}
    public ClientPartidoDto(Long idPartido, String nombre, LocalDateTime fechaPartido, float precio, int maxEntradas, int entradasDisponibles){
        this.idPartido=idPartido;
        this.nombre=nombre;
        this.fechaPartido= (fechaPartido != null) ? fechaPartido.withNano(0) : null;
        this.precio=precio;
        this.maxEntradas=maxEntradas;
        this.entradasDisponibles=entradasDisponibles;
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
    public Long getIdPartido() {
        return idPartido;
    }
    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }
    public int getEntradasDisponibles() {
        return entradasDisponibles;
    }
    public void setEntradasDisponibles(int entradasVendidas) {
        this.entradasDisponibles=entradasDisponibles;
    }

    @Override
    public String toString() {
        return "ClientPartidoDto {" +
                "idPartido=" + idPartido +
                ", nombre='" + nombre + '\'' +
                ", fechaPartido='" + fechaPartido + '\'' +
                ", precio=" + precio +
                ", maxEntradas=" + maxEntradas +
                ", entradasDisponibles=" + entradasDisponibles +
                '}';
    }
}

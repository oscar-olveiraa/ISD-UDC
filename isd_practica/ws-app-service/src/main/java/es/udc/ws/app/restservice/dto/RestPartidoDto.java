package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestPartidoDto {
    private Long idPartido;
    private String nombre;
    private String fechaPartido;
    private float precio;
    private int maxEntradas;
    private int entradasVendidas;

    public RestPartidoDto(){}
    public RestPartidoDto(Long idPartido, String nombre, String fechaPartido, float precio, int maxEntradas, int entradasVendidas){
        this.idPartido=idPartido;
        this.nombre=nombre;
        this.fechaPartido= fechaPartido;
        this.precio=precio;
        this.maxEntradas=maxEntradas;
        this.entradasVendidas=entradasVendidas;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaPartido() {
        return fechaPartido;
    }
    public void setFechaPartido(String fechaPartido) {
        this.fechaPartido= fechaPartido;
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
    public int getEntradasVendidas() {
        return entradasVendidas;
    }
    public void setEntradasVendidas(int entradasVendidas) {
        this.entradasVendidas=entradasVendidas;
    }

    @Override
    public String toString() {
        return "RestPartidoDto{" +
                "idPartido=" + idPartido +
                ", nombre='" + nombre + '\'' +
                ", fechaPartido=" + fechaPartido +
                ", precio=" + precio +
                ", maxEntradas=" + maxEntradas +
                ", entradasVendidas=" + entradasVendidas +
                '}';
    }
}

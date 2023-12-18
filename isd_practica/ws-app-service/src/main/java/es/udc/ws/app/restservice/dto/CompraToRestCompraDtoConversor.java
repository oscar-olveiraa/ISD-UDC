package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.compra.Compra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CompraToRestCompraDtoConversor {
    public static List<RestCompraDto> toRestCompraDtos(List<Compra> compras) {
        List<RestCompraDto> compraDtos = new ArrayList<>(compras.size());
        for (int i = 0; i < compras.size(); i++) {
            Compra compra = compras.get(i);
            compraDtos.add(toRestCompraDto(compra));
        }
        return compraDtos;
    }
    public static RestCompraDto toRestCompraDto(Compra compra) {
        return new RestCompraDto(compra.getIdCompra(),compra.getEmail(), compra.getTarjeta().substring(compra.getTarjeta().length()-4), compra.getCantidad(), compra.getFechaCompra().toString(), compra.getIdPartido(), compra.isRecogida());
    }

    public static Compra toCompra(RestCompraDto compra) {
        LocalDateTime strLocalDateTime = LocalDateTime.parse(compra.getFechaCompra());

        return new Compra(compra.getIdPartido(),compra.getEmail(), compra.getTarjeta(), compra.getCantidad(), strLocalDateTime, compra.getIdPartido(), compra.isRecogida());
    }
}

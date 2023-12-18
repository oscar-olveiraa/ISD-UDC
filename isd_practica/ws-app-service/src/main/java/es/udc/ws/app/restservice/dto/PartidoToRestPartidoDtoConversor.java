package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.partido.Partido;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PartidoToRestPartidoDtoConversor {
    public static List<RestPartidoDto> toRestPartidoDtos(List<Partido> partidos) {
        List<RestPartidoDto> partidoDtos = new ArrayList<>(partidos.size());
        for (int i = 0; i < partidos.size(); i++) {
            Partido partido = partidos.get(i);
            partidoDtos.add(toRestPartidoDto(partido));
        }
        return partidoDtos;
    }

    public static RestPartidoDto toRestPartidoDto(Partido partido) {
        return new RestPartidoDto(partido.getIdPartido(),partido.getNombre(), partido.getFechaPartido().toString(), partido.getPrecio(), partido.getMaxEntradas(), partido.getEntradasVendidas());
    }

    public static Partido toPartido(RestPartidoDto partido) {
        LocalDateTime strLocalDateTime = LocalDateTime.parse(partido.getFechaPartido());

        return new Partido(partido.getIdPartido(),partido.getNombre(), strLocalDateTime, partido.getPrecio(), partido.getMaxEntradas(), partido.getEntradasVendidas());
    }
}

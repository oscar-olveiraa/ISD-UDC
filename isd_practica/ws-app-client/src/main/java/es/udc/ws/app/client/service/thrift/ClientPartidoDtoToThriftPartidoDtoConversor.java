package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.thrift.ThriftPartidoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientPartidoDtoToThriftPartidoDtoConversor {
    public static ThriftPartidoDto toThriftPartidoDto(
            ClientPartidoDto clientPartidoDto) {

        Long idPartido = clientPartidoDto.getIdPartido();

        return new ThriftPartidoDto(
                idPartido == null ? -1 : idPartido.longValue(),
                clientPartidoDto.getNombre(),
                clientPartidoDto.getFechaPartido().toString(),
                clientPartidoDto.getPrecio(),
                clientPartidoDto.getMaxEntradas(),
                clientPartidoDto.getMaxEntradas()-clientPartidoDto.getEntradasDisponibles());

    }

    public static List<ClientPartidoDto> toClientPartidoDtos(List<ThriftPartidoDto> partidos) {

        List<ClientPartidoDto> clientPartidoDtos = new ArrayList<>(partidos.size());

        for (ThriftPartidoDto partido : partidos) {
            clientPartidoDtos.add(toClientPartidoDto(partido));
        }
        return clientPartidoDtos;

    }

    public static ClientPartidoDto toClientPartidoDto(ThriftPartidoDto partido) {

        return new ClientPartidoDto(
                partido.getIdPartido(),
                partido.getNombre(),
                LocalDateTime.parse(partido.getFechaPartido()),
                Double.valueOf(partido.getPrecio()).floatValue(),
                partido.getMaxEntradas(),
                partido.getMaxEntradas()-partido.getEntradasVendidas());

    }
}

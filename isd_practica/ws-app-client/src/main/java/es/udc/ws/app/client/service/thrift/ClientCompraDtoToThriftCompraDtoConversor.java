package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientCompraDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.thrift.ThriftCompraDto;
import es.udc.ws.app.thrift.ThriftPartidoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientCompraDtoToThriftCompraDtoConversor {

    public static ThriftCompraDto toThriftCompraDto(
            ClientCompraDto clientCompraDto) {

        Long idCompra = clientCompraDto.getIdCompra();

        return new ThriftCompraDto(
                idCompra == null ? -1 : idCompra.longValue(),
                clientCompraDto.getEmail(),
                clientCompraDto.getTarjeta(),
                clientCompraDto.getCantidad(),
                clientCompraDto.getFechaCompra().toString(),
                clientCompraDto.getIdPartido(),
                clientCompraDto.isRecogida());

    }

    public static List<ClientCompraDto> toClientCompraDtos(List<ThriftCompraDto> compras) {

        List<ClientCompraDto> clientCompraDtos = new ArrayList<>(compras.size());

        for (ThriftCompraDto compra : compras) {
            clientCompraDtos.add(toClientCompraDto(compra));
        }
        return clientCompraDtos;

    }

    public static ClientCompraDto toClientCompraDto(ThriftCompraDto compra) {

        return new ClientCompraDto(
                compra.getIdCompra(),
                compra.getEmail(),
                compra.getTarjeta(),
                compra.getCantidad(),
                LocalDateTime.parse(compra.getFechaCompra()),
                compra.getIdPartido(),
                compra.isRecogida());

    }

}

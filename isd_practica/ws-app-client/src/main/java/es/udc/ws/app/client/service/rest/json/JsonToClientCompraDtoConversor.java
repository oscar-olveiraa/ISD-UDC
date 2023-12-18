package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientCompraDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientInvalidMatchDateException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientCompraDtoConversor {
    public static ClientCompraDto toClientCompraDto(InputStream jsonCompra) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCompra);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientCompraDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientCompraDto> toClientCompraDtos(InputStream jsonCompras) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCompras);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode comprasArray = (ArrayNode) rootNode;
                List<ClientCompraDto> compraDtos = new ArrayList<>(comprasArray.size());
                for (JsonNode compraNode : comprasArray) {
                    compraDtos.add(toClientCompraDto(compraNode));
                }

                return compraDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientCompraDto toClientCompraDto(JsonNode compraNode) throws ParsingException {
        if (compraNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode partidoObject = (ObjectNode) compraNode;

            JsonNode idCompraNode = partidoObject.get("idCompra");
            Long idCompra = (idCompraNode != null) ? idCompraNode.longValue() : null;

            String email = partidoObject.get("email").textValue().trim();
            String tarjeta = partidoObject.get("tarjeta").textValue().trim();
            int cantidad = partidoObject.get("cantidad").intValue();
            String fechaCompra = partidoObject.get("fechaCompra").textValue().trim();
            Long idPartido = partidoObject.get("idPartido").longValue();
            Boolean recogida = partidoObject.get("recogida").booleanValue();

            return new ClientCompraDto(idCompra,email,tarjeta,cantidad,LocalDateTime.parse(fechaCompra), idPartido, recogida);
        }
    }
}

package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientPartidoDtoConversor {

    public static ObjectNode toObjectNode(ClientPartidoDto partido) throws IOException {

        ObjectNode partidoObject = JsonNodeFactory.instance.objectNode();

        if (partido.getIdPartido() != null) {
            partidoObject.put("idPartido", partido.getIdPartido());
        }

        String strLocalDateTime = partido.getFechaPartido().toString();
        partidoObject.put("nombre", partido.getNombre()).
                put("fechaPartido", strLocalDateTime).
                put("precio", partido.getPrecio()).
                put("maxEntradas", partido.getMaxEntradas()).
                put("entradasVendidas", partido.getMaxEntradas()-partido.getEntradasDisponibles());

        return partidoObject;
    }

    public static ClientPartidoDto toClientPartidoDto(InputStream jsonPartido) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPartido);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientPartidoDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientPartidoDto> toClientPartidoDtos(InputStream jsonPartidos) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPartidos);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode partidosArray = (ArrayNode) rootNode;
                List<ClientPartidoDto> partidoDtos = new ArrayList<>(partidosArray.size());
                for (JsonNode partidoNode : partidosArray) {
                    partidoDtos.add(toClientPartidoDto(partidoNode));
                }

                return partidoDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientPartidoDto toClientPartidoDto(JsonNode partidoNode) throws ParsingException {
        if (partidoNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode partidoObject = (ObjectNode) partidoNode;

            JsonNode partidoIdNode = partidoObject.get("idPartido");
            Long idPartido = (partidoIdNode != null) ? partidoIdNode.longValue() : null;

            String nombre = partidoObject.get("nombre").textValue().trim();
            String fechaPartido = partidoObject.get("fechaPartido").textValue().trim();
            float precio = partidoObject.get("precio").floatValue();
            int maxEntradas = partidoObject.get("maxEntradas").intValue();
            int entradasDisponibles = (partidoObject.get("maxEntradas").intValue())-(partidoObject.get("entradasVendidas").intValue());

            LocalDateTime strLocalDateTime = LocalDateTime.parse(fechaPartido);

            return new ClientPartidoDto(idPartido,nombre,strLocalDateTime,precio,maxEntradas,entradasDisponibles);
        }
    }
}

package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestPartidoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JsonToRestPartidoDtoConversor {
    public static ObjectNode toObjectNode(RestPartidoDto partido) {

        ObjectNode partidoObject = JsonNodeFactory.instance.objectNode();

        partidoObject.put("idPartido", partido.getIdPartido()).
                put("nombre", partido.getNombre()).
                put("fechaPartido", partido.getFechaPartido()).
                put("precio", partido.getPrecio()).
                put("maxEntradas", partido.getMaxEntradas()).
                put("entradasVendidas", partido.getEntradasVendidas());

        return partidoObject;
    }

    public static ArrayNode toArrayNode(List<RestPartidoDto> partidos) {

        ArrayNode partidosNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < partidos.size(); i++) {
            RestPartidoDto partidoDto = partidos.get(i);
            ObjectNode partidoObject = toObjectNode(partidoDto);
            partidosNode.add(partidoObject);
        }

        return partidosNode;
    }

    public static RestPartidoDto toRestPartidoDto(InputStream jsonPartido) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPartido);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode partidoObject = (ObjectNode) rootNode;

                JsonNode idPartidoNode = partidoObject.get("idPartido");
                Long idPartido = (idPartidoNode != null) ? idPartidoNode.longValue() : null;

                String nombre = partidoObject.get("nombre").textValue().trim();
                String fechaPartido =  partidoObject.get("fechaPartido").textValue().trim();

                float precio = partidoObject.get("precio").floatValue();
                int maxEntradas = partidoObject.get("maxEntradas").intValue();
                int entradasVendidas =  partidoObject.get("entradasVendidas").intValue();

                return new RestPartidoDto(idPartido, nombre, fechaPartido, precio, maxEntradas, entradasVendidas);
                //antes ponia solo fechaPartido
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}

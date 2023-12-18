package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestCompraDto;
import es.udc.ws.app.restservice.dto.RestCompraDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestCompraDtoConversor {
    public static ObjectNode toObjectNode(RestCompraDto compra) {

        ObjectNode compraNode = JsonNodeFactory.instance.objectNode();

        if (compra.getIdCompra() != null) {
            compraNode.put("idCompra", compra.getIdCompra());
        }
        compraNode.put("email", compra.getEmail()).
                put("tarjeta", compra.getTarjeta()).
                put("cantidad", compra.getCantidad()).
                put("fechaCompra", compra.getFechaCompra()).
                put("idPartido", compra.getIdPartido()).
                put("recogida", compra.isRecogida());

        return compraNode;
    }

    public static ArrayNode toArrayNode(List<RestCompraDto> compras) {

        ArrayNode comprasNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < compras.size(); i++) {
            RestCompraDto compraDto = compras.get(i);
            ObjectNode compraObject = toObjectNode(compraDto);
            comprasNode.add(compraObject);
        }

        return comprasNode;
    }

    public static RestCompraDto toRestCompraDto(InputStream jsonCompra) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCompra);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode partidoObject = (ObjectNode) rootNode;

                JsonNode idCompraNode = partidoObject.get("idCompra");
                Long idCompra = (idCompraNode != null) ? idCompraNode.longValue() : null;
                String email = partidoObject.get("email").textValue().trim();
                String tarjeta =  partidoObject.get("tarjeta").textValue().trim();
                int cantidad = partidoObject.get("cantidad").intValue();
                String fechaCompra =  partidoObject.get("fechaCompra").textValue().trim();
                Long idPartido = partidoObject.get("idPartido").longValue();
                boolean recogida = partidoObject.get("recogida").booleanValue();

                return new RestCompraDto(idCompra,email,tarjeta,cantidad,fechaCompra,idPartido,recogida);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}

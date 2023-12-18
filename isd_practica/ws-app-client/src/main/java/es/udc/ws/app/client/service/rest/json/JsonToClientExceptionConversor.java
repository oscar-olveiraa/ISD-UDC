package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientInvalidMatchDateException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughTicketsException;
import es.udc.ws.app.client.service.exceptions.ClientTicketAlreadyGivenException;
import es.udc.ws.app.client.service.exceptions.ClientWrongCreditCardException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InvalidMatchDate")) {
                    return toInvalidMatchDateException(rootNode);
                }
                if (errorType.equals("NotEnoughTickets")) {
                    return toNotEnoughTicketsException(rootNode);
                }
                if (errorType.equals("TicketAlreadyGiven")) {
                    return toTicketAlreadyGivenException(rootNode);
                }
                if (errorType.equals("WrongCreditCard")) {
                    return toWrongCreditCardException(rootNode);
                }
                else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientInvalidMatchDateException toInvalidMatchDateException(JsonNode rootNode) {
        Long idPartido = rootNode.get("idPartido").longValue();
        String fecha = rootNode.get("fechaPartido").textValue();
        LocalDateTime fechaPartido = null;
        if (fecha != null) {
            fechaPartido = LocalDateTime.parse(fecha);
        }
        return new ClientInvalidMatchDateException(idPartido, fechaPartido);
    }

    private static ClientNotEnoughTicketsException toNotEnoughTicketsException(JsonNode rootNode) {
        Long idPartido = rootNode.get("idPartido").longValue();
        int cantidad = rootNode.get("cantidad").intValue();
        return new ClientNotEnoughTicketsException(idPartido, cantidad);
    }

    private static ClientTicketAlreadyGivenException toTicketAlreadyGivenException(JsonNode rootNode) {
        Long idCompra = rootNode.get("idCompra").longValue();
        return new ClientTicketAlreadyGivenException(idCompra);
    }

    private static ClientWrongCreditCardException toWrongCreditCardException(JsonNode rootNode) {
        Long idCompra = rootNode.get("idCompra").longValue();
        return new ClientWrongCreditCardException(idCompra);
    }

}

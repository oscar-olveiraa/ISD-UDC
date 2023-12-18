package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.partidoService.exceptions.InvalidMatchDateException;
import es.udc.ws.app.model.partidoService.exceptions.NotEnoughTicketsException;
import es.udc.ws.app.model.partidoService.exceptions.TicketAlreadyGivenException;
import es.udc.ws.app.model.partidoService.exceptions.WrongCreditCardException;

public class AppExceptionToJsonConversor {
    public static ObjectNode toInvalidMatchDateException(InvalidMatchDateException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InvalidMatchDate");
        exceptionObject.put("idPartido", (ex.getIdPartido() != null) ? ex.getIdPartido() : null);
        if (ex.getFechaPartido() != null) {
            exceptionObject.put("fechaPartido", ex.getFechaPartido().toString());
        } else {
            exceptionObject.set("fechaPartido", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toNotEnoughTicketsException(NotEnoughTicketsException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "NotEnoughTickets");
        exceptionObject.put("idPartido", (ex.getIdPartido() != null) ? ex.getIdPartido() : null);

        if (ex.getCantidad() > 0) {
            exceptionObject.put("cantidad", ex.getCantidad());
        } else {
            exceptionObject.set("cantidad", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toTicketAlreadyGivenException(TicketAlreadyGivenException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "TicketAlreadyGiven");
        exceptionObject.put("idCompra", (ex.getIdCompra() != null) ? ex.getIdCompra() : null);

        return exceptionObject;
    }

    public static ObjectNode toWrongCreditCardException(WrongCreditCardException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "WrongCreditCard");
        exceptionObject.put("idCompra", (ex.getIdCompra() != null) ? ex.getIdCompra() : null);

        return exceptionObject;
    }
}

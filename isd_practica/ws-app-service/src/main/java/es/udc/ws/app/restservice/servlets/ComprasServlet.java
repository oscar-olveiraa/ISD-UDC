package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.compra.Compra;
import es.udc.ws.app.model.partidoService.PartidoServiceFactory;
import es.udc.ws.app.model.partidoService.exceptions.InvalidMatchDateException;
import es.udc.ws.app.model.partidoService.exceptions.NotEnoughTicketsException;
import es.udc.ws.app.model.partidoService.exceptions.TicketAlreadyGivenException;
import es.udc.ws.app.model.partidoService.exceptions.WrongCreditCardException;
import es.udc.ws.app.restservice.dto.CompraToRestCompraDtoConversor;

import es.udc.ws.app.restservice.dto.RestCompraDto;

import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestCompraDtoConversor;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.udc.ws.util.servlet.ServletUtils.normalizePath;

public class ComprasServlet extends RestHttpServletTemplate {

    public static Integer getMandatoryParameterAsInt(HttpServletRequest req, String paramName)
            throws InputValidationException {
        String paramValue;
        Integer paramValueAsInt = null;
        if ((paramValue = ServletUtils.getMandatoryParameter(req, paramName)) != null) {
            try {
                paramValueAsInt = Integer.valueOf(paramValue);
            } catch (NumberFormatException ex) {
                throw new InputValidationException("Invalid Request: " + "parameter '" + paramName + "' is invalid '" +
                        paramValue + "'");
            }
        }
        return paramValueAsInt;
    }

    public static Long getIdFromPath2(HttpServletRequest req, String resourceName) throws IOException, InputValidationException {
        Long id = null;
        String path = normalizePath(req.getPathInfo());
        if (path != null && path.length() != 0) {
            String[] partes = path.split("/");
            if(partes[2].equals("pickup")) {
                try {
                    id = Long.valueOf(partes[1]);
                    return id;
                } catch (NumberFormatException var6) {
                    throw new InputValidationException("Invalid Request: invalid " + resourceName + " id '" + partes[1] + "'");
                }
            }
            else {
                throw new InputValidationException("Invalid Request: invalid " + partes[2] + " 8operation");
            }
        } else {
            throw new InputValidationException("Invalid Request: invalid " + resourceName + " id");
        }
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        if(req.getPathInfo()==null || req.getPathInfo().isEmpty()){
            Long idPartido = ServletUtils.getMandatoryParameterAsLong(req, "idPartido");
            String email = ServletUtils.getMandatoryParameter(req, "email");
            String tarjeta = ServletUtils.getMandatoryParameter(req, "tarjeta");
            int cantidad = getMandatoryParameterAsInt(req, "cantidad");

            Compra compra;

            try {
                compra = PartidoServiceFactory.getService().buyTicket(idPartido, email, tarjeta, cantidad);
            } catch (InvalidMatchDateException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toInvalidMatchDateException(ex), null);
                return;
            } catch (NotEnoughTicketsException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toNotEnoughTicketsException(ex), null);
                return;
            }

            RestCompraDto compraDto = CompraToRestCompraDtoConversor.toRestCompraDto(compra);
            String compraURL = normalizePath(req.getRequestURL().toString()) + "/" + compra.getIdCompra().toString();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", compraURL);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonToRestCompraDtoConversor.toObjectNode(compraDto), headers);
        }else{
            String tarjeta = ServletUtils.getMandatoryParameter(req, "tarjeta");
            try {
                PartidoServiceFactory.getService().pickupTicket(getIdFromPath2(req, "compra"),tarjeta);
            } catch (WrongCreditCardException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toWrongCreditCardException(ex), null);
                return;
            } catch (TicketAlreadyGivenException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toTicketAlreadyGivenException(ex), null);
                return;
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
        }
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException{
        String keyWords = req.getParameter("email");
        List<Compra> compras = PartidoServiceFactory.getService().findPurchase(keyWords);
        List<RestCompraDto> compraDtos = CompraToRestCompraDtoConversor.toRestCompraDtos(compras);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestCompraDtoConversor.toArrayNode(compraDtos), null);
    }
}

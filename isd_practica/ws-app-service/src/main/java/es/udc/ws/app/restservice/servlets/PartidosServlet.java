package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partidoService.PartidoServiceFactory;
import es.udc.ws.app.restservice.dto.PartidoToRestPartidoDtoConversor;
import es.udc.ws.app.restservice.dto.RestPartidoDto;
import es.udc.ws.app.restservice.json.JsonToRestPartidoDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartidosServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);

        RestPartidoDto partidoDto = JsonToRestPartidoDtoConversor.toRestPartidoDto(req.getInputStream());
        Partido partido = PartidoToRestPartidoDtoConversor.toPartido(partidoDto);

        partido = PartidoServiceFactory.getService().addPartido(partido);

        partidoDto = PartidoToRestPartidoDtoConversor.toRestPartidoDto(partido);
        String partidoURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + partido.getIdPartido();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", partidoURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestPartidoDtoConversor.toObjectNode(partidoDto), headers);
    }

    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        if(req.getPathInfo()==null || req.getPathInfo().isEmpty()){
            String keyWords = req.getParameter("date");

            LocalDateTime strLocalDateTime = LocalDateTime.parse(keyWords);
            LocalDateTime now = LocalDateTime.now().withNano(0);
            String ahora = now.toString(); //antes .format(formatter)
            LocalDateTime fechaBuena = LocalDateTime.parse(ahora);

            List<Partido> partidos = PartidoServiceFactory.getService().findByDate(fechaBuena,strLocalDateTime);
            List<RestPartidoDto> partidoDtos = PartidoToRestPartidoDtoConversor.toRestPartidoDtos(partidos);

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestPartidoDtoConversor.toArrayNode(partidoDtos), null);
        }else{
            Partido partido = null;
            try {
                partido = PartidoServiceFactory.getService().findById(ServletUtils.getIdFromPath(req, "partido"));
                RestPartidoDto partidoDto = PartidoToRestPartidoDtoConversor.toRestPartidoDto(partido);

                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                        JsonToRestPartidoDtoConversor.toObjectNode(partidoDto), null);
            }catch (InstanceNotFoundException ex){
                throw new InstanceNotFoundException(partido,"partido");
            }
        }
    }

}

package es.udc.ws.app.model.partidoService;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class PartidoServiceFactory {
    private final static String CLASS_NAME_PARAMETER = "PartidoServiceFactory.className";
    private static PartidoService service = null;

    private PartidoServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static PartidoService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (PartidoService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static PartidoService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}

package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

import java.lang.reflect.InvocationTargetException;

public class ClientPartidoServiceFactory {

    private final static String CLASS_NAME_PARAMETER
            = "ClientPartidoServiceFactory.className";
    private static Class<ClientPartidoService> serviceClass = null;

    private ClientPartidoServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientPartidoService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientPartidoService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientPartidoService getService() {

        try {
            return (ClientPartidoService) getServiceClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}

package es.udc.ws.app.model.compra;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlCompraDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlCompraDaoFactory.className";
    private static SqlCompraDao dao = null;

    private SqlCompraDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlCompraDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlCompraDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlCompraDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;
    }


}

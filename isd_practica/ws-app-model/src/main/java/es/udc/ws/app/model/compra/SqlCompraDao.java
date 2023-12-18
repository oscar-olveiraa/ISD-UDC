package es.udc.ws.app.model.compra;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlCompraDao {

    public Compra create(Connection connection, Compra compra);

    public void update(Connection connection, Compra compra) throws InstanceNotFoundException;

    public Compra find(Connection connection, Long id_compra) throws InstanceNotFoundException;

    public List<Compra> findByUser(Connection connection, String email);

    public void remove(Connection connection, Long id_compra) throws InstanceNotFoundException;

}

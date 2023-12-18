package es.udc.ws.app.model.partido;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlPartidoDao {

    public Partido create(Connection connection, Partido partido);

    public void update(Connection connection, Partido partido) throws InstanceNotFoundException;

    public Partido find(Connection connection, Long id_partido) throws InstanceNotFoundException;

    public List<Partido> findByDate(Connection connection, LocalDateTime fecha_ini, LocalDateTime fecha_fin);

    public void remove(Connection connection, Long idPartido) throws InstanceNotFoundException;

}

package es.udc.ws.app.model.partido;

import es.udc.ws.app.model.compra.Compra;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlPartidoDao implements SqlPartidoDao {

    protected  AbstractSqlPartidoDao(){}

    @Override
    public Partido find(Connection connection, Long idPartido) throws InstanceNotFoundException {
                /* Create "queryString". */
                String queryString = "SELECT nombre, fechaPartido, "
                        + " precio, maxEntradas, fechaAlta, entradasVendidas FROM Partido WHERE idPartido = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

                    int i = 1;
                    preparedStatement.setLong(i++, idPartido.longValue());

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (!resultSet.next()) {
                        throw new InstanceNotFoundException(idPartido,
                                Partido.class.getName());
                    }

                    i = 1;
                    String nombre = resultSet.getString(i++);
                    Timestamp fechaPartidoAsTimestamp = resultSet.getTimestamp(i++);
                    LocalDateTime fechaPartido = fechaPartidoAsTimestamp.toLocalDateTime();
                    float precio = resultSet.getFloat(i++);
                    int maxEntradas = resultSet.getInt(i++);
                    Timestamp fechaAltaAsTimestamp = resultSet.getTimestamp(i++);
                    LocalDateTime fechaAlta = fechaAltaAsTimestamp.toLocalDateTime();
                    int entradasVendidas = resultSet.getInt(i++);

                    /* Return movie. */
                    return new Partido(idPartido, nombre, fechaPartido, precio, maxEntradas, entradasVendidas,
                            fechaAlta);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
    }

    @Override
    public List<Partido> findByDate(Connection connection, LocalDateTime fechaIni, LocalDateTime fechaFin) {

        String queryString = "SELECT idPartido, nombre, fechaPartido, precio, maxEntradas, fechaAlta, entradasVendidas"
                            + " FROM Partido";
        queryString += " WHERE fechaPartido > ? AND fechaPartido < ?";
        queryString += " ORDER BY fechaPartido";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int u=1;
            preparedStatement.setTimestamp(u++, Timestamp.valueOf(fechaIni));
            preparedStatement.setTimestamp(u++, Timestamp.valueOf(fechaFin));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Partido> partidos = new ArrayList<Partido>();
            while(resultSet.next()){
                int i = 1;
                Long idPartido = Long.valueOf(resultSet.getLong(i++));
                String nombre = resultSet.getString(i++);
                Timestamp fechaPartidoAsTimeStamp = resultSet.getTimestamp(i++);
                LocalDateTime fechaPartido = fechaPartidoAsTimeStamp.toLocalDateTime();
                Float precio = resultSet.getFloat(i++);
                int maxEntradas = resultSet.getInt(i++);
                Timestamp fechaAltaAsTimeStamp = resultSet.getTimestamp(i++);
                LocalDateTime fechaAlta = fechaAltaAsTimeStamp.toLocalDateTime();
                int entradasVendidas = resultSet.getInt(i++);
                partidos.add(new Partido(idPartido,nombre,fechaPartido,precio,maxEntradas,entradasVendidas,fechaAlta));
            }
            return partidos;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Partido partido)
            throws InstanceNotFoundException {
        String queryString = "UPDATE Partido"
                + " SET entradasVendidas = ? WHERE idPartido = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setInt(i++, partido.getEntradasVendidas());
            preparedStatement.setLong(i++, partido.getIdPartido());

            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(partido.getIdPartido(),
                        Compra.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long idPartido) throws InstanceNotFoundException {
        String queryString = "DELETE FROM Partido WHERE" + " idPartido = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, idPartido);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(idPartido,
                        Partido.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

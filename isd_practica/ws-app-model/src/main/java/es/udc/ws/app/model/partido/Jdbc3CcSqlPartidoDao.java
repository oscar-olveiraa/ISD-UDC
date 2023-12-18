package es.udc.ws.app.model.partido;

import java.sql.*;

public class Jdbc3CcSqlPartidoDao extends AbstractSqlPartidoDao {

    @Override
    public Partido create(Connection connection, Partido partido) {

        String queryString = "INSERT INTO Partido"
                + " (nombre, fechaPartido, precio, maxEntradas, fechaAlta, entradasVendidas)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i=1;
            preparedStatement.setString(i++, partido.getNombre());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(partido.getFechaPartido()));
            preparedStatement.setFloat(i++, partido.getPrecio());
            preparedStatement.setInt(i++, partido.getMaxEntradas());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(partido.getFechaAlta()));
            preparedStatement.setInt(i++, partido.getEntradasVendidas());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException("JDBC driver did not return generated key.");
            }

            Long idPartido=resultSet.getLong(1);

            return new Partido(idPartido, partido.getNombre(), partido.getFechaPartido(), partido.getPrecio(),
                               partido.getMaxEntradas(), partido.getEntradasVendidas(), partido.getFechaAlta());

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

}

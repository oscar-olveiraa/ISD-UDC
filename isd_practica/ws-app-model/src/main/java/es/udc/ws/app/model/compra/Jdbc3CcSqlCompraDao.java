package es.udc.ws.app.model.compra;

import es.udc.ws.app.model.partido.Partido;

import java.sql.*;

public class Jdbc3CcSqlCompraDao extends AbstractSqlCompraDao{
    @Override
    public Compra create(Connection connection, Compra compra) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Compra"
                + " (email, tarjeta, cantidad,"
                + " fechaCompra, recogida, idPartido) VALUES (?, ?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, compra.getEmail());
            preparedStatement.setString(i++, compra.getTarjeta());
            preparedStatement.setInt(i++, compra.getCantidad());
            Timestamp date = compra.getFechaCompra() != null ? Timestamp.valueOf(compra.getFechaCompra()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setBoolean(i++, compra.isRecogida());
            preparedStatement.setLong(i++, compra.getIdPartido());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long idCompra = resultSet.getLong(1);

            /* Return sale. */
            return new Compra(idCompra, compra.getEmail(),
                    compra.getTarjeta(), compra.getCantidad(),
                    compra.getFechaCompra(), compra.getIdPartido(), compra.isRecogida());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

package es.udc.ws.app.model.compra;

import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlCompraDao implements SqlCompraDao {
    protected AbstractSqlCompraDao(){

    }

    @Override
    public Compra find(Connection connection, Long id_compra)
            throws InstanceNotFoundException {

                String queryString = "SELECT email, tarjeta, cantidad, fechaCompra, recogida, idPartido "
                        + " FROM Compra WHERE idCompra = ? ";

                try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

                    /* Fill "preparedStatement". */
                    int i = 1;
                    preparedStatement.setLong(i++, id_compra.longValue());

                    /* Execute query. */
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (!resultSet.next()) {
                        throw new InstanceNotFoundException(id_compra,
                                Partido.class.getName());
                    }

                    /* Get results. */
                    i = 1;
                    String email = resultSet.getString(i++);
                    String tarjeta = resultSet.getString(i++);
                    int cantidad = resultSet.getInt(i++);
                    Timestamp fechaCompraAsTimestamp = resultSet.getTimestamp(i++);
                    LocalDateTime fechaCompra = fechaCompraAsTimestamp.toLocalDateTime();
                    boolean recogida = resultSet.getBoolean(i++);
                    Long idPartido = resultSet.getLong(i++);

                    /* Return compra */
                    return new Compra(id_compra,email, tarjeta, cantidad, fechaCompra, idPartido, recogida);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
    }

    @Override
    public List<Compra> findByUser(Connection connection, String emailSQL) {
        String queryString = "SELECT idCompra, email, tarjeta, cantidad, fechaCompra, recogida, idPartido"
                + " FROM Compra";
        queryString += " WHERE email = ?";
        queryString += " ORDER BY fechaCompra";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int u=1;
            preparedStatement.setString(u++,emailSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Compra> compras = new ArrayList<Compra>();

            while(resultSet.next()){
                int i = 1;
                Long idCompra = Long.valueOf(resultSet.getLong(i++));
                String email = resultSet.getString(i++);
                String tarjeta = resultSet.getString(i++);
                int cantidad = resultSet.getInt(i++);
                Timestamp fechaCompraAsTimeStamp = resultSet.getTimestamp(i++);
                LocalDateTime fechaCompra = fechaCompraAsTimeStamp.toLocalDateTime();
                boolean recogida = resultSet.getBoolean(i++);
                Long idPartido = resultSet.getLong(i++);

                compras.add(new Compra(idCompra,email,tarjeta,cantidad,fechaCompra,idPartido,recogida));
            }
            return compras;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Compra compra)
            throws InstanceNotFoundException {
        String queryString = "UPDATE Compra"
            + " SET recogida = ? WHERE idCompra = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            /* Fill "preparedStatement" */
            int i = 1;
            preparedStatement.setBoolean(i++, compra.isRecogida());
            preparedStatement.setLong(i++,compra.getIdCompra());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(compra.getIdCompra(),
                        Compra.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long idCompra) throws InstanceNotFoundException {
        String queryString = "DELETE FROM Compra WHERE" + " idCompra = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, idCompra);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(idCompra,
                        Compra.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

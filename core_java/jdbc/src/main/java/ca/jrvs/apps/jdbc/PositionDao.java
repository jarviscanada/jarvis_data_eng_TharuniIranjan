package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ca.jrvs.apps.jdbc.JsonParser.*;
import static ca.jrvs.apps.jdbc.JsonParser.convertStringToDouble;


public class PositionDao implements CrudDao<Position, String> {

    private Connection c;
    private static final String INSERT = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?);";
    private static final String GET_ONE = "SELECT * FROM position WHERE symbol=?;";
    private static final String GET_ALL = "SELECT * FROM position;";
    private static final String DELETE_ONE = "DELETE FROM position WHERE symbol=?";
    private static final String DELETE_ALL = "DELETE FROM position";
    private static final String UPDATE = "UPDATE position SET number_of_shares = ?, value_paid = ? WHERE symbol = ?";

    public PositionDao(Connection c) {
        this.c = c;
    }

    @Override
    public Position save(Position entity) throws IllegalArgumentException {
        try (PreparedStatement statement = this.c.prepareStatement(INSERT);) {

            statement.setString(1, entity.getTicker());
            statement.setInt(2, entity.getNumOfShares());
            statement.setDouble(3, entity.getValuePaid());
            statement.execute();

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Position update(Position entity) throws IllegalArgumentException {
        Position position = null;
        try{
            this.c.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.c.prepareStatement(UPDATE);) {
            statement.setInt(1, entity.getNumOfShares());
            statement.setDouble(2, entity.getValuePaid());
            statement.setString(3, entity.getTicker());
            statement.execute();
            this.c.commit();
            position = this.findById(entity.getTicker()).get();
        }catch(SQLException e){
            try{
                this.c.rollback();
            }catch (SQLException sqle){
                e.printStackTrace();
                throw new RuntimeException(sqle);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return position;
    }

    @Override
    public Optional<Position> findById(String s) throws IllegalArgumentException {
        Position position = new Position();

        try (PreparedStatement statement = this.c.prepareStatement(GET_ONE);) {
            statement.setString(1, s);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                position.setTicker(rs.getString(1));
                position.setNumOfShares(rs.getInt(2));
                position.setValuePaid(rs.getDouble(3));
            }

            return Optional.of(position);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Position> findAll() {
        List<Position> listOfPositions = new ArrayList<Position>();

        try (PreparedStatement statement = this.c.prepareStatement(GET_ALL);) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Position position = new Position();
                position.setTicker(resultSet.getString(1));
                position.setNumOfShares(resultSet.getInt(2));
                position.setValuePaid(resultSet.getDouble(3));


                listOfPositions.add(position);
            }

            return listOfPositions;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        try (PreparedStatement statement = this.c.prepareStatement(DELETE_ONE);) {
            statement.setString(1, s);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        try (PreparedStatement statement = this.c.prepareStatement(DELETE_ALL);) {
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

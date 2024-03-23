package ca.jrvs.apps.jdbc;

import kotlin.collections.builders.MapBuilder;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static ca.jrvs.apps.jdbc.JsonParser.*;


public class QuoteDao implements CrudDao<Quote, String> {
    private Connection c;
    private static final String INSERT = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String GET_ONE = "SELECT * FROM quote WHERE symbol=?;";
    private static final String GET_ALL = "SELECT * FROM quote;";
    private static final String DELETE_ONE = "DELETE FROM quote WHERE symbol = ?";
    private static final String DELETE_ALL = "DELETE FROM quote";

    public QuoteDao(Connection c) {
        this.c = c;
    }


    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {

        try (PreparedStatement statement = this.c.prepareStatement(INSERT);) {

            statement.setString(1, entity.getSymbol());
            statement.setDouble(2, convertStringToDouble(entity.getOpen()));
            statement.setDouble(3, convertStringToDouble(entity.getHigh()));
            statement.setDouble(4, convertStringToDouble(entity.getLow()));
            statement.setDouble(5, convertStringToDouble(entity.getPrice()));
            statement.setInt(6, convertStringToInt(entity.getVolume()));
            statement.setDate(7, convertStringToDate(entity.getLatestTradingDay()));
            statement.setDouble(8, convertStringToDouble(entity.getPreviousClose()));
            statement.setDouble(9, convertStringToDouble(entity.getChange()));
            statement.setString(10, entity.getChangePercent());
            statement.execute();

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException {
        Quote quote = new Quote();

        try (PreparedStatement statement = this.c.prepareStatement(GET_ONE);) {
            statement.setString(1, s);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                quote.setSymbol(rs.getString("symbol"));
                quote.setOpen(rs.getString("open"));
                quote.setHigh(rs.getString("high"));
                quote.setLow(rs.getString("low"));
                quote.setPrice(rs.getString("price"));
                quote.setVolume(rs.getString("volume"));
                quote.setLatestTradingDay(rs.getString("latest_trading_day"));
                quote.setPreviousClose(rs.getString("previous_close"));
                quote.setChange(rs.getString("change"));
                quote.setChangePercent(rs.getString("change_percent"));
            }

            return Optional.of(quote);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Quote> findAll() {
        List<Quote> listOfQuotes = new ArrayList<Quote>();

        try (PreparedStatement statement = this.c.prepareStatement(GET_ALL);) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Quote quote = new Quote();
                quote.setSymbol(resultSet.getString("symbol"));
                quote.setOpen(resultSet.getString("open"));
                quote.setHigh(resultSet.getString("high"));
                quote.setLow(resultSet.getString("low"));
                quote.setPrice(resultSet.getString("price"));
                quote.setVolume(resultSet.getString("volume"));
                quote.setLatestTradingDay(resultSet.getString("latest_trading_day"));
                quote.setPreviousClose(resultSet.getString("previous_close"));
                quote.setChange(resultSet.getString("change"));
                quote.setChangePercent(resultSet.getString("change_percent"));

                listOfQuotes.add(quote);
            }

            return listOfQuotes;
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
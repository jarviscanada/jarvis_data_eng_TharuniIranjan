package ca.jrvs.apps.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final String UPDATE = "UPDATE quote SET open = ?, high = ?, low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ? WHERE symbol = ?";
    private static Logger logger = LoggerFactory.getLogger(QuoteDao.class);

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
            logger.error("Failed to execute SQL statement for save against quote '" + entity + "' in QuoteDao-> " + e);
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Quote update(Quote entity) throws IllegalArgumentException {

        Quote quote;
        try{
            this.c.setAutoCommit(false);
        }catch(SQLException e){
            logger.error("Failed to set AutoCommit for update against quote '" + entity + "' in QuoteDao-> " + e);
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.c.prepareStatement(UPDATE);) {
            statement.setDouble(1, convertStringToDouble(entity.getOpen()));
            statement.setDouble(2, convertStringToDouble(entity.getHigh()));
            statement.setDouble(3, convertStringToDouble(entity.getLow()));
            statement.setDouble(4, convertStringToDouble(entity.getPrice()));
            statement.setInt(5, convertStringToInt(entity.getVolume()));
            statement.setDate(6, convertStringToDate(entity.getLatestTradingDay()));
            statement.setDouble(7, convertStringToDouble(entity.getPreviousClose()));
            statement.setDouble(8, convertStringToDouble(entity.getChange()));
            statement.setString(9, entity.getChangePercent());
            statement.setString(10, entity.getSymbol());
            statement.execute();
            this.c.commit();
            quote = this.findById(entity.getSymbol()).get();
        }catch(SQLException e){
            try{
                this.c.rollback();
            }catch (SQLException sqle){
                logger.error("Failed to do rollback for update against quote '" + entity + "' in QuoteDao-> " + sqle);
                throw new RuntimeException(sqle);
            }
            logger.error("Failed to execute SQL statement for update against quote '" + entity + "' in QuoteDao-> " + e);
            throw new RuntimeException(e);
        } catch (ParseException e) {
            logger.error("Failed to parse for update against quote '" + entity + "' in QuoteDao-> " + e);
            throw new RuntimeException(e);
        }
        return quote;
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
            logger.error("Failed to execute SQL statement for findByID against quote symbol '" + s + "' in QuoteDao-> " + e);
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
            logger.error("Failed to execute SQL statement for findAll quotes in QuoteDao-> " + e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {

        try (PreparedStatement statement = this.c.prepareStatement(DELETE_ONE);) {
            statement.setString(1, s);
            statement.execute();

        } catch (SQLException e) {
            logger.error("Failed to execute SQL statement for deleteByID against quote symbol '" + s + "' in QuoteDao-> " + e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteAll() {
        try (PreparedStatement statement = this.c.prepareStatement(DELETE_ALL);) {
            statement.execute();

        } catch (SQLException e) {
            logger.error("Failed to execute SQL statement for deleteAll quotes in QuoteDao-> " + e);
            throw new RuntimeException(e);
        }

    }

}
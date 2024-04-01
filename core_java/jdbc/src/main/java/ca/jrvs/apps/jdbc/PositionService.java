package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static ca.jrvs.apps.jdbc.JsonParser.convertStringToInt;

public class PositionService {
    private PositionDao dao;

    public Connection connection;
    public String databaseName = "stock_quote";

    public PositionService() {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(databaseName);

        try {
            connection = dcm.getConnection();
            dao = new PositionDao(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        PositionService ps = new PositionService();
//        ps.dao.deleteAll();
//        ps.buy("AAPL", 4, 170.9);
//        System.out.println(ps.dao.findAll());
//        ps.sell("AAPL");
//        System.out.println(ps.dao.findAll());
//    }

    public double updateValue(int numShares, double price, double oldValue) {
        return (numShares*price)+oldValue;
    }

    public boolean isValidShares(int maxAmount, int purchaseAmount) {
        return purchaseAmount < Integer.MAX_VALUE && purchaseAmount <= maxAmount;
    }

    public boolean doesExist(PositionDao pd, String s) {
        Optional<Position> posO = pd.findById(s);
        return posO.get().getTicker() != null;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        Position position = new Position();
        QuoteService qs = new QuoteService();
        Optional<Quote> quote = qs.fetchQuoteDataFromAPI(ticker);

        // check if it is a valid symbol
        if (quote.isEmpty()) {
            System.out.println("Symbol does not exist");
            return position;
        }

        String symbol = quote.get().getSymbol();

        // check if the number of shares entered is less than the total amount of shares
        int volume = convertStringToInt(quote.get().getVolume());
        if (!isValidShares(volume, numberOfShares)) {
            System.out.println("Invalid amount of shares");
            return position;
        }

        // if the symbol is already in the position db, make an update, else, create a new value
        if (doesExist(dao, ticker)) {
            Position existingPos = dao.findById(ticker).get();
            int newShares = (int) updateValue(numberOfShares, 1, existingPos.getNumOfShares());

            if (!isValidShares(volume, newShares)) {
                System.out.println("Invalid amount of shares");
                return position;
            }
            position.setTicker(symbol);
            position.setNumOfShares(newShares);
            position.setValuePaid(updateValue(numberOfShares, price, existingPos.getValuePaid()));
            dao.update(position);
        } else {
            position.setTicker(symbol);
            position.setNumOfShares(numberOfShares);
            position.setValuePaid(updateValue(numberOfShares, price, 0));
            dao.save(position);
        }

        return position;
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker
     */
    public void sell(String ticker) {
        if (!doesExist(dao, ticker)) {
            System.out.println("Symbol not found");
        } else {
            dao.deleteById(ticker);
        }
    }

}
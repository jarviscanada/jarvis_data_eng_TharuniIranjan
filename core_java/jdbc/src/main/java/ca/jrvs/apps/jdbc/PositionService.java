package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static ca.jrvs.apps.jdbc.JsonParser.convertStringToDouble;
import static ca.jrvs.apps.jdbc.JsonParser.convertStringToInt;

public class PositionService {
    private PositionDao positionDao;

    public Connection connection;
    public QuoteDao quoteDao;

    public PositionService() {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager();

        try {
            connection = dcm.getConnection();
            positionDao = new PositionDao(connection);
            quoteDao = new QuoteDao(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double updateValue(int numShares, double price, double oldValue) {
        return (numShares*price)+oldValue;
    }

    public boolean isValidShares(int maxAmount, int purchaseAmount) {
        return purchaseAmount >= 0 && purchaseAmount < Integer.MAX_VALUE && purchaseAmount <= maxAmount;
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
        double stockPrice = convertStringToDouble(quote.get().getPrice());
        if (doesExist(positionDao, ticker)) {
            Position existingPos = positionDao.findById(ticker).get();
            int newShares = (int) updateValue(numberOfShares, 1, existingPos.getNumOfShares());

            if (!isValidShares(volume, newShares)) {
                System.out.println("Invalid amount of shares");
                return position;
            }
            position.setTicker(symbol);
            position.setNumOfShares(newShares);
            position.setValuePaid(updateValue(numberOfShares, stockPrice, existingPos.getValuePaid()));
            positionDao.update(position);
        } else {
            position.setTicker(symbol);
            position.setNumOfShares(numberOfShares);
            position.setValuePaid(updateValue(numberOfShares, stockPrice, 0));
            positionDao.save(position);
        }

        return position;
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker
     */
    public void sell(String ticker) {
        if (!doesExist(positionDao, ticker)) {
            System.out.println("Symbol not found");
        } else {
            positionDao.deleteById(ticker);
        }
    }

}
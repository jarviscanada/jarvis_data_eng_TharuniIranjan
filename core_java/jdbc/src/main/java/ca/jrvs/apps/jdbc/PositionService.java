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
//        ps.buy("AAPL", 4, 170.9);
//        System.out.println(ps.dao.findAll());
//        ps.sell("AAPL");
//        System.out.println(ps.dao.findAll());
//    }

    public double calculateValuePaid(int numShares, double price, double oldValue) {
        return (numShares*price)+oldValue;
    }

    public boolean isValidShares(int maxAmount, int purchaseAmount) {
        return purchaseAmount <= maxAmount;
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

        if (quote.get().getSymbol() == null) {
            System.out.println("Symbol does not exist");
            return position;
        }

        String symbol = quote.get().getSymbol();
        int volume = convertStringToInt(quote.get().getVolume());


        if (!isValidShares(volume, numberOfShares)) {
            System.out.println("Invalid amount of shares");
            return position;
        }


        // if the symbol is already in the position db, make an update, else, create a new value
        position.setTicker(symbol);
        if (doesExist(dao, ticker)) {
            Position existingPos = dao.findById(ticker).get();
            position.setNumOfShares(numberOfShares + existingPos.getNumOfShares());
            if (!isValidShares(volume, position.getNumOfShares())) {
                System.out.println("Invalid amount of shares");
                return existingPos;
            }
            position.setValuePaid(calculateValuePaid(numberOfShares, price, existingPos.getValuePaid()));
            dao.update(position);
        } else {
            position.setNumOfShares(numberOfShares);
            position.setValuePaid(calculateValuePaid(numberOfShares, price, 0));
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
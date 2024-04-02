package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import static ca.jrvs.apps.jdbc.JsonParser.*;

/**
 * Runs the UI of the Stock Quote Application
 */
public class StockQuoteController {
    public DatabaseConnectionManager dcm;
    public Connection c;
    public QuoteDao quoteDao;
    public QuoteService quoteService;
    public PositionDao positionDao;
    public PositionService positionService;
    public int apiCalls;
    public int API_LIMIT = 6;

    /**
     * Creates the necessary method calls according to user input
     * @throws SQLException when there is an error connecting to the db
     */
    public void initClient() throws SQLException {
        System.out.println("Welcome!\nType i for user manual and q to quit");

        Scanner myObj = new Scanner(System.in);
        String requestType = "i";
        String[] request;
        apiCalls = 0;
        dbConnect();

        // Keep asking user what type of service they want as long as they don't type q to quit
        while (!requestType.equals("q")) {
            System.out.print("\nEnter service request: ");
            request = myObj.nextLine().split(" ");
            requestType = request[0];

            switch (requestType) {
                case "i" -> instructions();
                case "view" -> handleView(request);
                case "buy" -> handleBuy(request);
                case "sell" -> handleSell(request);
                case "q" -> System.out.println("Thanks, come again!");
                default -> System.out.print("Invalid request type");
            }
        }
    }

    /***
     * When i is inputted for user manual this text prompt appears on the terminal describing how to interact with the app
     */
    public void instructions() {
        String border = "********************";
        System.out.println("\n" + border);
        System.out.println("Below are a list of services we provide:");
        System.out.println("* View [type] [symbol]: displays information about certain stock(s)");
        System.out.println("[type]: determines where to get data from; information about previously purchased stocks or the current market rate of a stock");
        System.out.println("-- input types: 'old' or 'new'");
        System.out.println("[symbol]: determines what stock info to show; whether it is all purchased stocks or some specific one");
        System.out.println("-- input types: '*' for all or the stock symbol for a specific company (Ex. 'MSFT' for Microsoft)");

        System.out.println("\n* Buy [symbol] [shares]: buy a specific amount of shares of a stock at market price");
        System.out.println("[shares]: determines the number of shares you want to purchase of some stock");
        System.out.println("-- input types: a whole number greater than 0 and less than the total stocks available to purchase");

        System.out.println("\n* Sell [symbol]: sell all your shares of a specific stock");
        System.out.println("-- input types: '*' for all or the stock symbol for a specific company");

        System.out.println("\nExample Inputs:");
        System.out.println("buy MSFT 2");
        System.out.println("sell MSFT");
        System.out.println("view old *");
        System.out.println("view new GOOG");

        System.out.println("\nPlease note: this is the free trial version and there is a limited amount of transactions that can be done daily. Application will crash should this threshold be met");
        System.out.println("Thank you for using our app and good luck!");
        System.out.println(border);

    }

    /***
     * initializes connection to the database, and quote and position table services
     * @throws SQLException
     */
    public void dbConnect() throws SQLException {
        dcm = new DatabaseConnectionManager();
        c = dcm.getConnection();
        quoteDao = new QuoteDao(c);
        quoteService = new QuoteService();
        positionDao = new PositionDao(c);
        positionService = new PositionService();
    }

    /***
     * Prases user input and either shows stock data from an api call or from the database
     * @param request the line the user inputted
     */
    public void handleView(String[] request) {
        if (request.length == 3) {
            String type = request[1];
            String symbol = request[2];

            // new means they want to see the current market value of a specific stock
            // api call is made using fetchQuoteInfo
            if (type.equals("new")) {
                // since its a freemium api, there is limited calls that can be made
                if (apiCalls+1 < API_LIMIT) {
                    QuoteHTTPHelper qhh = new QuoteHTTPHelper();
                    Quote newQuote = qhh.fetchQuoteInfo(symbol);
                    apiCalls += 1;
                    if (newQuote.getSymbol() == null) {
                        System.out.println("Symbol does not exist");
                    } else {
                        System.out.println("STOCK INFO");
                        System.out.println(newQuote);
                    }
                } else {
                    System.out.println("Reached daily limit. Cannot view/buy new stocks at the moment.");
                }

            // old means user wants to look at the stock info that has been stored in the db based off their purchases
            } else if (type.equals("old")) {
                // * means all items so loop through and print those items
                if (symbol.equals("*")) {
                    Iterable<Quote> quoteList = quoteDao.findAll();
                    Iterable<Position> positionList = positionDao.findAll();
                    System.out.println("STOCK INFO");
                    for (Quote q: quoteList) { System.out.println(q);}
                    System.out.println("\nPURCHASE INFO");
                    for (Position p: positionList) { System.out.println(p);}

                } else {
                    // find a particular stock in the db and prin the Quote and Position
                    Optional<Quote> qd = quoteDao.findById(symbol);
                    Optional<Position> pd = positionDao.findById(symbol);
                    if (pd.get().getTicker() != null) {
                        System.out.println("STOCK INFO");
                        System.out.println(qd.get());
                        System.out.println("\nPURCHASE INFO");
                        System.out.println(pd.get());
                    } else {
                        System.out.println("This stock has not been purchased");
                    }
                }

            } else {
                System.out.println("Invalid type. Please enter 'old' or 'new'");
            }

        } else {
            System.out.println("Insufficient number of inputs");
        }
    }

    /***
     * Parses user input to buy more of a previously purchased stock or buy a new one and update the db accordingly
     * @param request
     */
    public void handleBuy(String[] request) {
        // check:
        // if the correct number of inputs is given
        // if the api limit is reached
        if (request.length == 3) {
            if (apiCalls+1 < API_LIMIT) {
                String symbol = request[1];
                String shareString = request[2];
                if (isNumber(shareString)){
                    int shares = convertStringToInt(shareString);
                    double defaultPrice = 0.00;

                    Position newPosition = positionService.buy(symbol, shares, defaultPrice);
                    apiCalls += 1;

                    if (newPosition.getTicker() != null) {
                        System.out.println("Purchase Complete");
                        System.out.println("\nPURCHASE INFO");
                        System.out.println(newPosition);
                    } else {
                        System.out.println("Unable to retrieve stock information. Please ensure symbol was entered correctly");
                    }
                } else {
                    System.out.println("Invalid purchase amount. Please enter positive whole numbers");
                }
            } else {
                System.out.println("Reached daily limit. Cannot view/buy new stocks.");
            }
        } else {
            System.out.println("Insufficient number of inputs");
        }
    }

    /**
     * Parses user input and removes a particular set of stock(s) from the db
     * @param request
     */
    public void handleSell(String[] request) {
        if (request.length == 2) {
            String symbol = request[1];
            String shouldSell;

            System.out.print("Are you sure you want to sell (y/n): ");
            Scanner myObj = new Scanner(System.in);
            shouldSell = myObj.nextLine();

            if (shouldSell.equals("y")) {
                if (symbol.equals("*")) {
                    positionDao.deleteAll();
                    quoteDao.deleteAll();
                    System.out.println("POS Complete");
                } else {
                    Optional<Position> deletePosition = positionDao.findById(symbol);

                    if (deletePosition.get().getTicker() != null) {
                        positionService.sell(symbol);
                        quoteDao.deleteById(symbol);
                        System.out.println("POS Complete");
                    } else {
                        System.out.println("This stock has not been purchased to sell");
                    }
                }
            } else if (!shouldSell.equals("n")) {
                System.out.println("Invalid input");
            }
        } else {
            System.out.println("Insufficient number of inputs");
        }
    }
}

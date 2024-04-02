package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import static ca.jrvs.apps.jdbc.JsonParser.*;

public class StockQuoteController {
    public DatabaseConnectionManager dcm;
    public Connection c;
    public QuoteDao quoteDao;
    public QuoteService quoteService;
    public PositionDao positionDao;
    public PositionService positionService;
    public int apiCalls;
    public int API_LIMIT = 6;

    public void initClient() throws SQLException {
        System.out.println("Welcome!\nType i for user manual and q to quit");

        Scanner myObj = new Scanner(System.in);
        String requestType = "i";
        String[] request;
        apiCalls = 0;
        dbConnect();

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
    public void dbConnect() throws SQLException {
        dcm = new DatabaseConnectionManager();
        c = dcm.getConnection();
        quoteDao = new QuoteDao(c);
        quoteService = new QuoteService();
        positionDao = new PositionDao(c);
        positionService = new PositionService();
    }

    public void handleView(String[] request) {
        if (request.length == 3) {
            String type = request[1];
            String symbol = request[2];

            if (type.equals("new")) {
                if (apiCalls+1 < API_LIMIT) {
                    Quote newQuote = quoteService.fetchQuoteDataFromAPI(symbol).get();
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
            } else if (type.equals("old")) {
                if (symbol.equals("*")) {
                    Iterable<Quote> quoteList = quoteDao.findAll();
                    Iterable<Position> positionList = positionDao.findAll();
                    System.out.println("STOCK INFO");
                    for (Quote q: quoteList) { System.out.println(q);}
                    System.out.println("\nPURCHASE INFO");
                    for (Position p: positionList) { System.out.println(p);}

                } else {
                    Optional<Quote> qd = quoteDao.findById(symbol);
                    Optional<Position> pd = positionDao.findById(symbol);
                    if (qd.get().getSymbol() != null) {
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

    public void handleBuy(String[] request) {
        if (request.length == 3) {
            if (apiCalls+2 < API_LIMIT) {
                String symbol = request[1];
                int shares = convertStringToInt(request[2]);

                Quote newQuote = quoteService.fetchQuoteDataFromAPI(symbol).get();
                apiCalls += 1;
                double price = convertStringToDouble(newQuote.getPrice());

                Position newPosition = positionService.buy(symbol, shares, price);
                apiCalls += 1;

                if (newPosition.getTicker() != null) {
                    System.out.println("Purchase Complete");
                    System.out.println("\nPURCHASE INFO");
                    System.out.println(newPosition);
                }
            } else {
                System.out.println("Reached daily limit. Cannot view/buy new stocks.");
            }

        } else {
            System.out.println("Insufficient number of inputs");
        }
    }

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

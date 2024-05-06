package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.entity.IexQuote;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.util.TradingAppTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuoteService {
    private final MarketDataDao marketDataDao;
    private final QuoteDao quoteDao;
    private static Logger logger = LoggerFactory.getLogger(QuoteService.class);

    @Autowired
    public QuoteService(MarketDataDao marketDataDao, QuoteDao quoteDao) {
        this.marketDataDao = marketDataDao;
        this.quoteDao = quoteDao;
    }

    /**
     * Find an IexQuote
     * @param ticker
     * @return IexQuote object
     * @throws IllegalArgumentException if ticker is invalid
     */
    public IexQuote findIexQuoteByTicker(String ticker) {
        logger.info("Started call on QuoteService.findIexQuoteByTicker");
        if (!TradingAppTools.validTicker(ticker)) {
            logger.error("Error on QuoteService.findIexQuoteByTicker: invalid ticker");
            throw new IllegalArgumentException("Invalid ticker");
        }
        logger.info("QuoteService.findIexQuoteByTicker: calling on MarketDataDao.findById");
        Optional<IexQuote> iexQuoteOptional = marketDataDao.findById(ticker);
        if (iexQuoteOptional.isEmpty()) {
            logger.error("Error on QuoteService.findIexQuoteByTicker: marketDataDao.findById returned empty IexQuote for ticker '" + ticker + "'");
            throw new IllegalArgumentException("No quote data found for ticker: " + ticker);
        }
        logger.info("Successful call on QuoteService.findIexQuoteByTicker for ticker '" + ticker + "'");
        return iexQuoteOptional.get();
    }

    public void updateMarketData() {
        List<Quote> quotes = quoteDao.findAll();
        List<Quote> updatedQuotes = new ArrayList<>();
        for (Quote quote : quotes) {
            Optional<IexQuote> iexQuoteOptional = marketDataDao.findById(quote.getId());
            if (iexQuoteOptional.isPresent()) {
                IexQuote iexQuote = iexQuoteOptional.get();
                Quote updatedQuote = buildQuote(iexQuote);
                updatedQuotes.add(updatedQuote);
            }
        }
        quoteDao.saveAll(updatedQuotes);
    }

    public List<Quote> saveQuotes(List<String> tickers) {
        List<Quote> savedQuotes = new ArrayList<>();
        for (String ticker : tickers) {
            IexQuote iexQuote = marketDataDao.findById(ticker).orElseThrow(IllegalArgumentException::new);
            Quote quote = buildQuote(iexQuote);
            savedQuotes.add(quoteDao.save(quote));
        }
        return savedQuotes;
    }

    public Quote saveQuote(Quote quote) {
        return quoteDao.save(quote);
    }

    public List<Quote> findAllQuotes() {
        return quoteDao.findAll();
    }

    private Quote buildQuote(IexQuote iexQuote) {
        Quote quote = new Quote();
        quote.setId(iexQuote.getSymbol());
        quote.setLastPrice(iexQuote.getLatestPrice());
        quote.setBidPrice(iexQuote.getIexBidPrice());
        quote.setAskPrice(iexQuote.getIexAskPrice());
        quote.setBidSize(iexQuote.getIexBidSize());
        quote.setAskSize(iexQuote.getIexAskSize());
        return quote;
    }

    protected static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
        if (iexQuote == null) {
            throw new IllegalArgumentException("IexQuote cannot be null");
        }

        Quote quote = new Quote();
        quote.setId(iexQuote.getSymbol());
        quote.setLastPrice(Optional.ofNullable(iexQuote.getLatestPrice()).orElse(0.0));
        quote.setBidPrice(Optional.ofNullable(iexQuote.getIexBidPrice()).orElse(0.0));
        quote.setAskPrice(Optional.ofNullable(iexQuote.getIexAskPrice()).orElse(0.0));
        quote.setBidSize(Optional.ofNullable(iexQuote.getIexBidSize()).orElse(0));
        quote.setAskSize(Optional.ofNullable(iexQuote.getIexAskSize()).orElse(0));
        return quote;
    }

    protected Quote saveQuote(String ticker) {
        if (ticker == null || ticker.isEmpty()) {
            throw new IllegalArgumentException("Ticker cannot be null or empty");
        }

        IexQuote iexQuote = marketDataDao.findById(ticker).orElseThrow(IllegalArgumentException::new);
        Quote quote = buildQuoteFromIexQuote(iexQuote);
        return quoteDao.save(quote);
    }

}
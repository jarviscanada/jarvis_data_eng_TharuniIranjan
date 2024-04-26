package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.entity.IexQuote;
import ca.jrvs.apps.trading.util.TradingAppTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuoteService {
    private final MarketDataDao marketDataDao;
    private static Logger logger = LoggerFactory.getLogger(QuoteService.class);

    @Autowired
    public QuoteService(MarketDataDao marketDataDao) {
        this.marketDataDao = marketDataDao;
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

}
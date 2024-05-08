package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.entity.IexQuote;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import com.google.common.base.Joiner;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/quote")
public class QuoteController {
    private final QuoteService quoteService;
    private static Logger logger = LoggerFactory.getLogger(QuoteController.class);

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/quote/iex/ticker/{ticker}")
    public IexQuote getQuote(String ticker) {
        logger.info("Started call on QuoteController.getQuote for ticker '" + ticker + "'");
        logger.info("QuoteController.getQuote: calling on QuoteService.findIexQuoteByTicker");
        IexQuote iexQuote = quoteService.findIexQuoteByTicker(ticker);
        logger.info("Completed call on QuoteController.getQuote for ticker '" + ticker + "'");
        return iexQuote;
    }

    @GetMapping("/quote/dailyList")
    public List<Quote> getAllQuotes() {
        logger.info("Started call on QuoteController.getAllQuotes");
        logger.info("QuoteController.getAllQuotes: calling on QuoteService.findAllQuotes");
        logger.info("Completed call on QuoteController.getAllQuotes");
        return quoteService.findAllQuotes();
    }

    @PostMapping(path="/quote/tickerId/{tickerId}")
    public void addQuote(String ticker){
        logger.info("Started call on QuoteController.addQuote for ticker " + ticker );
        logger.info("QuoteController.addQuote: calling on QuoteService.saveQuote");
        quoteService.saveQuote(ticker);
        logger.info("Completed call on QuoteController.addQuote for ticker " + ticker);
    }

    @PostMapping(path="/quote/tickerId/{tickerId}")
    public void addAllQuotes(List<String> tickers){
        String tickerListString = Joiner.on(',').join(tickers);
        logger.info("Started call on QuoteController.addAllQuotes for tickers: " + tickerListString);
        logger.info("QuoteController.addAllQuotes: calling on QuoteService.saveQuotes");
        quoteService.saveQuotes(tickers);
        logger.info("Completed call on QuoteController.addAllQuotes for tickers: " + tickerListString);
    }

    @PutMapping(path="/iexMarketData")
    @ResponseStatus(HttpStatus.OK)
    public void updateMarketData () {
        try {
            logger.info("Started call on QuoteController.updateMarketData");
            logger.info("QuoteController.updateMarketData: calling on QuoteService.updateMarketData");
            quoteService.updateMarketData();
        } catch (Exception e) {
            logger.error("Error on QuoteService.updateMarketData: HTTP request failed");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        logger.info("Completed call on QuoteController.updateMarketData");
    }

}

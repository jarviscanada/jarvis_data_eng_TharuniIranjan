package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.entity.IexQuote;
import ca.jrvs.apps.trading.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quote")
public class QuoteController {
    private final QuoteService quoteService;
    private static Logger logger = LoggerFactory.getLogger(QuoteController.class);

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/iex/ticker/{ticker}")
    public IexQuote getQuote(String ticker) {
        logger.info("Started call on QuoteController.getQuote for ticker '" + ticker + "'");
        logger.info("QuoteController.getQuote: calling on QuoteService.findIexQuoteByTicker");
        IexQuote iexQuote = quoteService.findIexQuoteByTicker(ticker);
        logger.info("Completed call on QuoteController.getQuote for ticker '" + ticker + "'");
        return iexQuote;
    }

}

package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.config.MarketDataConfig;
import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.entity.IexQuote;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MarketDataDaoIntTest {
    private MarketDataDao dao;

    @Before
    public void init(){
        CloseableHttpClient httpClient = HttpClients.createDefault();;
        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/");
        marketDataConfig.setToken(System.getenv("IEX_PUB_TOKEN"));

        dao = new MarketDataDao(httpClient, marketDataConfig);
    }

    @Test
    public void findByTicker() {
        String ticker = "AAPL";
        IexQuote iexQuote = dao.findById(ticker).get();
        assertEquals(ticker, iexQuote.getSymbol());
    }

    @Test
    public void findIexQuotesByTickers() throws IOException {
        List<IexQuote> quoteList = dao.findAllById(Arrays.asList("AAPL", "FB"));
        assertEquals(2, quoteList.size());
        assertEquals("AAPL", quoteList.get(0).getSymbol());

        try {
            dao.findAllById(Arrays.asList("AAPL", "FB2"));
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
}

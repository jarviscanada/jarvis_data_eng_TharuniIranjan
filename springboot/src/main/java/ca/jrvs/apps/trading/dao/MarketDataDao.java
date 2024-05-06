package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.config.MarketDataConfig;
import ca.jrvs.apps.trading.entity.IexQuote;
import ca.jrvs.apps.trading.service.QuoteService;
import ca.jrvs.apps.trading.util.TradingAppTools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MarketDataDao {
    private CloseableHttpClient httpClient;
    private MarketDataConfig marketDataConfig;
    private static Logger logger = LoggerFactory.getLogger(MarketDataDao.class);


    public MarketDataDao(CloseableHttpClient httpClient, MarketDataConfig marketDataConfig) {
        this.httpClient = httpClient;
        this.marketDataConfig = marketDataConfig;
    }


    /**
     * Get an IexQuote
     *
     * @param ticker
     * @throws IllegalArgumentException if a given ticker is invalid
     * @throws DataRetrievalFailureException if HTTP request failed
     */
    public Optional<IexQuote> findById(String ticker) {
        logger.info("Started call on MarketDataDao.findById");
        String url = marketDataConfig.getHost() + "stable/stock/" + ticker + "/quote?token=" + marketDataConfig.getToken();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Optional<String> responseBody = executeHttpGet(url);
            if (responseBody.isEmpty()) {
                throw new IllegalArgumentException("given ticker is invalid");
            }
            IexQuote iexQuote = objectMapper.readValue(responseBody.get(), IexQuote.class);
            logger.info("Successful call on MarketDataDao.findById");
            return Optional.of(iexQuote);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Error on MarketDataDao.findById: Empty IexQuote returned");
            return Optional.empty(); // 404 response
        } catch (IOException e) {
            logger.error("Error on MarketDataDao.findById: Failed to retrieve data from IEX API");
            throw new DataRetrievalFailureException("Failed to retrieve data from IEX API", e);
        }
    }

    /**
     * Get quotes from IEX
     * @param tickers is a list of tickers
     * @return a list of IexQuote objects
     * @throws IllegalArgumentException if a given ticker is invalid
     * @throws DataRetrievalFailureException if HTTP request failed
     */
    public List<IexQuote> findAllById(Iterable<String> tickers) {
        List<IexQuote> quotes = new ArrayList<>();
        for (String ticker : tickers) {
            Optional<IexQuote> iexQuote = findById(ticker);
            if (iexQuote.isPresent()) {
                quotes.add(iexQuote.get());
            }
            // findById(ticker).ifPresent(quotes::add);
        }
        return quotes;
    }

    /**
     * Execute a GET request and return http entity/body as a string
     * Tip: use EntitiyUtils.toString to process HTTP entity
     *
     * @param url resource URL
     * @return http response body or Optional.empty for 404 response
     * @throws DataRetrievalFailureException if HTTP failed or status code is unexpected
     */
    private Optional<String> executeHttpGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == 200) { // If the status code is 200 (OK)
                HttpEntity entity = httpResponse.getEntity(); // Get the response entity
                String responseBody = EntityUtils.toString(entity); // Convert the entity to a string
                return Optional.of(responseBody); // Return the response body
            } else if (statusCode == 404) {
                return Optional.empty(); // Return empty optional for 404 response
            } else {
                throw new DataRetrievalFailureException("Unexpected status code: " + statusCode); // Throw exception for unexpected status code
            }
        } catch (IOException e) {
            throw new DataRetrievalFailureException("Failed to execute HTTP GET request", e); // Throw exception for IO error
        }

    }

    /**
     * Borrow a HTTP client from the HttpClientConnectionManager
     *
     * @return a HttpClient
     */
    private CloseableHttpClient getHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        return httpClientBuilder.build();
        // return httpClient;
    }

}

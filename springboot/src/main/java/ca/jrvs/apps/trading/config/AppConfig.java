package ca.jrvs.apps.trading.config;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private MarketDataConfig marketDataConfig;

    @Bean
    public MarketDataConfig marketDataConfig(){
        marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/");
        marketDataConfig.setToken("pk_42e36348e3234b46b3c0186791a1ac21");
        // marketDataConfig.setToken(System.getenv("IEX_PUB_TOKEN"));

        return marketDataConfig;
    }

    @Bean
    public MarketDataDao marketDataDao(CloseableHttpClient httpClient, MarketDataConfig marketDataConfig) {
        return new MarketDataDao(httpClient, marketDataConfig);
    }

    @Bean
    public CloseableHttpClient httpClient () {
        return HttpClients.createDefault();
    }

}
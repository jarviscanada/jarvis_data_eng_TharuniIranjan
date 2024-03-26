package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.http.HttpTimeoutException;

import static ca.jrvs.apps.jdbc.JsonParser.*;

public class QuoteHTTPHelper {
    private String apiKey;
    private OkHttpClient client;

    public QuoteHTTPHelper() {
        apiKey = DatabaseConnectionManager.getProperty("api.key");
        client = new OkHttpClient();

    }

//    public static void main (String[] args) throws JsonProcessingException {
//        String symbol = "AAPL";
//        QuoteHTTPHelper qhh = new QuoteHTTPHelper();
//        Quote quote = qhh.fetchQuoteInfo(symbol);
//        System.out.println(quote);
//    }

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     * @param symbol
     * @return Quote with latest data
     * @throws IllegalArgumentException if no data was found for the given symbol
     */
    // UNCOMMENT
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {

        Request request = new Request.Builder()
                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+symbol+"&datatype=json")
                .get()
                .addHeader("X-RapidAPI-Key", apiKey)
                .addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            return toObjectFromJson(responseBody, Quote.class);

        } catch (HttpTimeoutException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

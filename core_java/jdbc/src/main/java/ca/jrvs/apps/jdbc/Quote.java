package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    @JsonProperty("Global Quote")
    GlobalQuote globalQuoteList = new GlobalQuote();

    @JsonProperty("01. symbol")
    private String symbol;

    @JsonProperty("02. open")
    private String open;

    @JsonProperty("03. high")
    private String high;

    @JsonProperty("04. low")
    private String low;

    @JsonProperty("05. price")
    private String price;

    @JsonProperty("06. volume")
    private String volume;

    @JsonProperty("07. latest trading day")
    private String latestTradingDay;

    @JsonProperty("08. previous close")
    private String previousClose;

    @JsonProperty("09. change")
    private String change;

    @JsonProperty("10. change percent")
    private String changePercent;

    // Getters and setters
    @JsonProperty("01. symbol")
    public String getSymbol() { return globalQuoteList.getSymbol(); }
    @JsonProperty("01. symbol")
    public void setSymbol(String symbol) { globalQuoteList.setSymbol(symbol); }

    @JsonProperty("02. open")
    public String getOpen() { return globalQuoteList.getOpen(); }
    @JsonProperty("02. open")
    public void setOpen(String open) { globalQuoteList.setOpen(open); }

    @JsonProperty("03. high")
    public String getHigh() { return globalQuoteList.getHigh(); }
    @JsonProperty("03. high")
    public void setHigh(String high) { globalQuoteList.setHigh(high); }

    @JsonProperty("04. low")
    public String getLow() { return globalQuoteList.getLow(); }
    @JsonProperty("04. low")
    public void setLow(String low) { globalQuoteList.setLow(low); }

    @JsonProperty("05. price")
    public String getPrice() { return globalQuoteList.getPrice(); }
    @JsonProperty("05. price")
    public void setPrice(String price) { globalQuoteList.setPrice(price); }

    @JsonProperty("06. volume")
    public String getVolume() { return globalQuoteList.getVolume(); }
    @JsonProperty("06. volume")
    public void setVolume(String volume) { globalQuoteList.setVolume(volume); }

    @JsonProperty("07. latest trading day")
    public String getLatestTradingDay() { return globalQuoteList.getLatestTradingDay(); }
    @JsonProperty("07. latest trading day")
    public void setLatestTradingDay(String latestTradingDay) { globalQuoteList.setLatestTradingDay(latestTradingDay); }

    @JsonProperty("08. previous close")
    public String getPreviousClose() { return globalQuoteList.getPreviousClose(); }
    @JsonProperty("08. previous close")
    public void setPreviousClose(String previousClose) { globalQuoteList.setPreviousClose(previousClose); }

    @JsonProperty("09. change")
    public String getChange() { return globalQuoteList.getChange(); }
    @JsonProperty("09. change")
    public void setChange(String change) { globalQuoteList.setChange(change); }

    @JsonProperty("10. change percent")
    public String getChangePercent() { return globalQuoteList.getChangePercent(); }
    @JsonProperty("10. change percent")
    public void setChangePercent(String changePercent) { globalQuoteList.setChangePercent(changePercent); }

    @JsonProperty("Global Quote")
    public GlobalQuote getGlobalQuote() { return globalQuoteList; }
    @JsonProperty("Global Quote")

    public void setGlobalQuote(GlobalQuote globalQuote) { this.globalQuoteList = globalQuote; }

    @Override
    public String toString() {
        return globalQuoteList.toString();
    }

}

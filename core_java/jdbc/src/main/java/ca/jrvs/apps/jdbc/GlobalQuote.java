package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalQuote {

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
    public String getSymbol() { return symbol; }
    @JsonProperty("01. symbol")
    public void setSymbol(String symbol) { this.symbol = symbol; }

    @JsonProperty("02. open")
    public String getOpen() { return open; }
    @JsonProperty("02. open")
    public void setOpen(String open) { this.open = open; }

    @JsonProperty("03. high")
    public String getHigh() { return high; }
    @JsonProperty("03. high")
    public void setHigh(String high) { this.high = high; }

    @JsonProperty("04. low")
    public String getLow() { return low; }
    @JsonProperty("04. low")
    public void setLow(String low) { this.low = low; }

    @JsonProperty("05. price")
    public String getPrice() { return price; }
    @JsonProperty("05. price")
    public void setPrice(String price) { this.price = price; }

    @JsonProperty("06. volume")
    public String getVolume() { return volume; }
    @JsonProperty("06. volume")
    public void setVolume(String volume) { this.volume = volume; }

    @JsonProperty("07. latest trading day")
    public String getLatestTradingDay() { return latestTradingDay; }
    @JsonProperty("07. latest trading day")
    public void setLatestTradingDay(String latestTradingDay) { this.latestTradingDay = latestTradingDay; }

    @JsonProperty("08. previous close")
    public String getPreviousClose() { return previousClose; }
    @JsonProperty("08. previous close")
    public void setPreviousClose(String previousClose) { this.previousClose = previousClose; }

    @JsonProperty("09. change")
    public String getChange() { return change; }
    @JsonProperty("09. change")
    public void setChange(String change) { this.change = change; }

    @JsonProperty("10. change percent")
    public String getChangePercent() { return changePercent; }
    @JsonProperty("10. change percent")
    public void setChangePercent(String changePercent) { this.changePercent = changePercent; }

    @Override
    public String toString() {
        return "symbol: " + symbol +
                ", open: " + open +
                ", high: " + high +
                ", low: " + low +
                ", price: " + price +
                ", volume: " + volume +
                ", latest trading day: " + latestTradingDay +
                ", previous close: " + previousClose +
                ", change: " + change +
                ", change percent: " + changePercent;
    }
}
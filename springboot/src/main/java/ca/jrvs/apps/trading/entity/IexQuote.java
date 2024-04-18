package ca.jrvs.apps.trading.entity;
import ca.jrvs.apps.trading.model.IexQuoteDOM;

public class IexQuote {
    private long avgTotalVolume;
    private double change;
    private double changePercent;
    private String companyName;
    private String currency;
    private double iexClose;
    private double iexOpen;
    private double latestPrice;
    private String latestTime;
    private String symbol;
    private double ytdChange;

    public IexQuote() {}

    public long getAvgTotalVolume() {
        return avgTotalVolume;
    }
    public void setAvgTotalVolume(long avgTotalVolume) {
        this.avgTotalVolume = avgTotalVolume;
    }

    public double getChange() {
        return change;
    }
    public void setChange(double change) {
        this.change = change;
    }

    public double getChangePercent() {
        return changePercent;
    }
    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getIexClose() {
        return iexClose;
    }

    public void setIexClose(double iexClose) {
        this.iexClose = iexClose;
    }

    public double getIexOpen() {
        return iexOpen;
    }

    public void setIexOpen(double iexOpen) {
        this.iexOpen = iexOpen;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public String getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getYtdChange() {
        return ytdChange;
    }

    public void setYtdChange(double ytdChange) {
        this.ytdChange = ytdChange;
    }


    // Method to convert IexQuoteDOM to IexQuote
    public static IexQuote fromIexQuoteDOM(IexQuoteDOM dom) {
        IexQuote quote = new IexQuote();
        quote.setAvgTotalVolume(dom.getAvgTotalVolume());
        quote.setChange(dom.getChange());
        quote.setChangePercent(dom.getChangePercent());
        quote.setCompanyName(dom.getCompanyName());
        quote.setCurrency(dom.getCurrency());
        quote.setIexClose(dom.getIexClose());
        quote.setIexOpen(dom.getIexOpen());
        quote.setLatestPrice(dom.getLatestPrice());
        quote.setLatestTime(dom.getLatestTime());
        quote.setSymbol(dom.getSymbol());
        quote.setYtdChange(dom.getYtdChange());
        return quote;
    }

    // toString() method
    @Override
    public String toString() {
        return "[symbol: " + symbol +
                ", avgTotalVolume: " + avgTotalVolume +
                ", change: " + change +
                ", changePercent: " + changePercent +
                ", companyName: " + companyName +
                ", currency: " + currency +
                ", iexClose: " + iexClose +
                ", iexOpen: " + iexOpen +
                ", latestPrice: " + latestPrice +
                ", latestTime: " + latestTime +
                ", ytdChange: " + ytdChange +
                "]\n";
    }
}

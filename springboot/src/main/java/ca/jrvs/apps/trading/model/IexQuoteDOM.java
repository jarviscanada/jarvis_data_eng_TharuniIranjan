package ca.jrvs.apps.trading.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IexQuoteDOM {
    @JsonProperty("avgTotalVolume")
    private long avgTotalVolume;
    @JsonProperty("calculationPrice")
    private String calculationPrice;
    @JsonProperty("change")
    private double change;
    @JsonProperty("changePercent")
    private double changePercent;
    @JsonProperty("close")
    private Object close;
    @JsonProperty("closeSource")
    private String closeSource;
    @JsonProperty("closeTime")
    private Object closeTime;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("delayedPrice")
    private Object delayedPrice;
    @JsonProperty("delayedPriceTime")
    private Object delayedPriceTime;
    @JsonProperty("extendedChange")
    private Object extendedChange;
    @JsonProperty("extendedChangePercent")
    private Object extendedChangePercent;
    @JsonProperty("extendedPrice")
    private Object extendedPrice;
    @JsonProperty("extendedPriceTime")
    private Object extendedPriceTime;
    @JsonProperty("high")
    private Object high;
    @JsonProperty("highSource")
    private Object highSource;
    @JsonProperty("highTime")
    private Object highTime;
    @JsonProperty("iexAskPrice")
    private long iexAskPrice;
    @JsonProperty("iexAskSize")
    private long iexAskSize;
    @JsonProperty("iexBidPrice")
    private long iexBidPrice;
    @JsonProperty("iexBidSize")
    private long iexBidSize;
    @JsonProperty("iexClose")
    private double iexClose;
    @JsonProperty("iexCloseTime")
    private long iexCloseTime;
    @JsonProperty("iexLastUpdated")
    private long iexLastUpdated;
    @JsonProperty("iexMarketPercent")
    private double iexMarketPercent;
    @JsonProperty("iexOpen")
    private double iexOpen;
    @JsonProperty("iexOpenTime")
    private long iexOpenTime;
    @JsonProperty("iexRealtimePrice")
    private double iexRealtimePrice;
    @JsonProperty("iexRealtimeSize")
    private long iexRealtimeSize;
    @JsonProperty("iexVolume")
    private long iexVolume;
    @JsonProperty("lastTradeTime")
    private long lastTradeTime;
    @JsonProperty("latestPrice")
    private long latestPrice;
    @JsonProperty("latestSource")
    private String latestSource;
    @JsonProperty("latestTime")
    private String latestTime;
    @JsonProperty("latestUpdate")
    private long latestUpdate;
    @JsonProperty("latestVolume")
    private Object latestVolume;
    @JsonProperty("low")
    private Object low;
    @JsonProperty("lowSource")
    private Object lowSource;
    @JsonProperty("lowTime")
    private Object lowTime;
    @JsonProperty("marketCap")
    private long marketCap;
    @JsonProperty("oddLotDelayedPrice")
    private Object oddLotDelayedPrice;
    @JsonProperty("oddLotDelayedPriceTime")
    private Object oddLotDelayedPriceTime;
    @JsonProperty("open")
    private Object open;
    @JsonProperty("openTime")
    private Object openTime;
    @JsonProperty("openSource")
    private String openSource;
    @JsonProperty("peRatio")
    private double peRatio;
    @JsonProperty("previousClose")
    private double previousClose;
    @JsonProperty("previousVolume")
    private long previousVolume;
    @JsonProperty("primaryExchange")
    private String primaryExchange;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("volume")
    private Object volume;
    @JsonProperty("week52High")
    private double week52High;
    @JsonProperty("week52Low")
    private double week52Low;
    @JsonProperty("ytdChange")
    private double ytdChange;
    @JsonProperty("isUSMarketOpen")
    private boolean isUSMarketOpen;

    public IexQuoteDOM() {
    }

    public IexQuoteDOM(long avgTotalVolume, String calculationPrice, double change, double changePercent, Object close, String closeSource, Object closeTime, String companyName, String currency, Object delayedPrice, Object delayedPriceTime, Object extendedChange, Object extendedChangePercent, Object extendedPrice, Object extendedPriceTime, Object high, Object highSource, Object highTime, long iexAskPrice, long iexAskSize, long iexBidPrice, long iexBidSize, double iexClose, long iexCloseTime, long iexLastUpdated, double iexMarketPercent, double iexOpen, long iexOpenTime, double iexRealtimePrice, long iexRealtimeSize, long iexVolume, long lastTradeTime, long latestPrice, String latestSource, String latestTime, long latestUpdate, Object latestVolume, Object low, Object lowSource, Object lowTime, long marketCap, Object oddLotDelayedPrice, Object oddLotDelayedPriceTime, Object open, Object openTime, String openSource, double peRatio, double previousClose, long previousVolume, String primaryExchange, String symbol, Object volume, double week52High, double week52Low, double ytdChange, boolean isUSMarketOpen) {
        this.avgTotalVolume = avgTotalVolume;
        this.calculationPrice = calculationPrice;
        this.change = change;
        this.changePercent = changePercent;
        this.close = close;
        this.closeSource = closeSource;
        this.closeTime = closeTime;
        this.companyName = companyName;
        this.currency = currency;
        this.delayedPrice = delayedPrice;
        this.delayedPriceTime = delayedPriceTime;
        this.extendedChange = extendedChange;
        this.extendedChangePercent = extendedChangePercent;
        this.extendedPrice = extendedPrice;
        this.extendedPriceTime = extendedPriceTime;
        this.high = high;
        this.highSource = highSource;
        this.highTime = highTime;
        this.iexAskPrice = iexAskPrice;
        this.iexAskSize = iexAskSize;
        this.iexBidPrice = iexBidPrice;
        this.iexBidSize = iexBidSize;
        this.iexClose = iexClose;
        this.iexCloseTime = iexCloseTime;
        this.iexLastUpdated = iexLastUpdated;
        this.iexMarketPercent = iexMarketPercent;
        this.iexOpen = iexOpen;
        this.iexOpenTime = iexOpenTime;
        this.iexRealtimePrice = iexRealtimePrice;
        this.iexRealtimeSize = iexRealtimeSize;
        this.iexVolume = iexVolume;
        this.lastTradeTime = lastTradeTime;
        this.latestPrice = latestPrice;
        this.latestSource = latestSource;
        this.latestTime = latestTime;
        this.latestUpdate = latestUpdate;
        this.latestVolume = latestVolume;
        this.low = low;
        this.lowSource = lowSource;
        this.lowTime = lowTime;
        this.marketCap = marketCap;
        this.oddLotDelayedPrice = oddLotDelayedPrice;
        this.oddLotDelayedPriceTime = oddLotDelayedPriceTime;
        this.open = open;
        this.openTime = openTime;
        this.openSource = openSource;
        this.peRatio = peRatio;
        this.previousClose = previousClose;
        this.previousVolume = previousVolume;
        this.primaryExchange = primaryExchange;
        this.symbol = symbol;
        this.volume = volume;
        this.week52High = week52High;
        this.week52Low = week52Low;
        this.ytdChange = ytdChange;
        this.isUSMarketOpen = isUSMarketOpen;
    }

    @JsonProperty("avgTotalVolume")
    public long getAvgTotalVolume() {
        return avgTotalVolume;
    }

    @JsonProperty("avgTotalVolume")
    public void setAvgTotalVolume(long avgTotalVolume) {
        this.avgTotalVolume = avgTotalVolume;
    }

    @JsonProperty("calculationPrice")
    public String getCalculationPrice() {
        return calculationPrice;
    }

    @JsonProperty("calculationPrice")
    public void setCalculationPrice(String calculationPrice) {
        this.calculationPrice = calculationPrice;
    }

    @JsonProperty("change")
    public double getChange() {
        return change;
    }

    @JsonProperty("change")
    public void setChange(double change) {
        this.change = change;
    }

    @JsonProperty("changePercent")
    public double getChangePercent() {
        return changePercent;
    }

    @JsonProperty("changePercent")
    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    @JsonProperty("close")
    public Object getClose() {
        return close;
    }

    @JsonProperty("close")
    public void setClose(Object close) {
        this.close = close;
    }

    @JsonProperty("closeSource")
    public String getCloseSource() {
        return closeSource;
    }

    @JsonProperty("closeSource")
    public void setCloseSource(String closeSource) {
        this.closeSource = closeSource;
    }

    @JsonProperty("closeTime")
    public Object getCloseTime() {
        return closeTime;
    }

    @JsonProperty("closeTime")
    public void setCloseTime(Object closeTime) {
        this.closeTime = closeTime;
    }

    @JsonProperty("companyName")
    public String getCompanyName() {
        return companyName;
    }

    @JsonProperty("companyName")
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("delayedPrice")
    public Object getDelayedPrice() {
        return delayedPrice;
    }

    @JsonProperty("delayedPrice")
    public void setDelayedPrice(Object delayedPrice) {
        this.delayedPrice = delayedPrice;
    }

    @JsonProperty("delayedPriceTime")
    public Object getDelayedPriceTime() {
        return delayedPriceTime;
    }

    @JsonProperty("delayedPriceTime")
    public void setDelayedPriceTime(Object delayedPriceTime) {
        this.delayedPriceTime = delayedPriceTime;
    }

    @JsonProperty("extendedChange")
    public Object getExtendedChange() {
        return extendedChange;
    }

    @JsonProperty("extendedChange")
    public void setExtendedChange(Object extendedChange) {
        this.extendedChange = extendedChange;
    }

    @JsonProperty("extendedChangePercent")
    public Object getExtendedChangePercent() {
        return extendedChangePercent;
    }

    @JsonProperty("extendedChangePercent")
    public void setExtendedChangePercent(Object extendedChangePercent) {
        this.extendedChangePercent = extendedChangePercent;
    }

    @JsonProperty("extendedPrice")
    public Object getExtendedPrice() {
        return extendedPrice;
    }

    @JsonProperty("extendedPrice")
    public void setExtendedPrice(Object extendedPrice) {
        this.extendedPrice = extendedPrice;
    }

    @JsonProperty("extendedPriceTime")
    public Object getExtendedPriceTime() {
        return extendedPriceTime;
    }

    @JsonProperty("extendedPriceTime")
    public void setExtendedPriceTime(Object extendedPriceTime) {
        this.extendedPriceTime = extendedPriceTime;
    }

    @JsonProperty("high")
    public Object getHigh() {
        return high;
    }

    @JsonProperty("high")
    public void setHigh(Object high) {
        this.high = high;
    }

    @JsonProperty("highSource")
    public Object getHighSource() {
        return highSource;
    }

    @JsonProperty("highSource")
    public void setHighSource(Object highSource) {
        this.highSource = highSource;
    }

    @JsonProperty("highTime")
    public Object getHighTime() {
        return highTime;
    }

    @JsonProperty("highTime")
    public void setHighTime(Object highTime) {
        this.highTime = highTime;
    }

    @JsonProperty("iexAskPrice")
    public long getIexAskPrice() {
        return iexAskPrice;
    }

    @JsonProperty("iexAskPrice")
    public void setIexAskPrice(long iexAskPrice) {
        this.iexAskPrice = iexAskPrice;
    }

    @JsonProperty("iexAskSize")
    public long getIexAskSize() {
        return iexAskSize;
    }

    @JsonProperty("iexAskSize")
    public void setIexAskSize(long iexAskSize) {
        this.iexAskSize = iexAskSize;
    }

    @JsonProperty("iexBidPrice")
    public long getIexBidPrice() {
        return iexBidPrice;
    }

    @JsonProperty("iexBidPrice")
    public void setIexBidPrice(long iexBidPrice) {
        this.iexBidPrice = iexBidPrice;
    }

    @JsonProperty("iexBidSize")
    public long getIexBidSize() {
        return iexBidSize;
    }

    @JsonProperty("iexBidSize")
    public void setIexBidSize(long iexBidSize) {
        this.iexBidSize = iexBidSize;
    }

    @JsonProperty("iexClose")
    public double getIexClose() {
        return iexClose;
    }

    @JsonProperty("iexClose")
    public void setIexClose(double iexClose) {
        this.iexClose = iexClose;
    }

    @JsonProperty("iexCloseTime")
    public long getIexCloseTime() {
        return iexCloseTime;
    }

    @JsonProperty("iexCloseTime")
    public void setIexCloseTime(long iexCloseTime) {
        this.iexCloseTime = iexCloseTime;
    }

    @JsonProperty("iexLastUpdated")
    public long getIexLastUpdated() {
        return iexLastUpdated;
    }

    @JsonProperty("iexLastUpdated")
    public void setIexLastUpdated(long iexLastUpdated) {
        this.iexLastUpdated = iexLastUpdated;
    }

    @JsonProperty("iexMarketPercent")
    public double getIexMarketPercent() {
        return iexMarketPercent;
    }

    @JsonProperty("iexMarketPercent")
    public void setIexMarketPercent(double iexMarketPercent) {
        this.iexMarketPercent = iexMarketPercent;
    }

    @JsonProperty("iexOpen")
    public double getIexOpen() {
        return iexOpen;
    }

    @JsonProperty("iexOpen")
    public void setIexOpen(double iexOpen) {
        this.iexOpen = iexOpen;
    }

    @JsonProperty("iexOpenTime")
    public long getIexOpenTime() {
        return iexOpenTime;
    }

    @JsonProperty("iexOpenTime")
    public void setIexOpenTime(long iexOpenTime) {
        this.iexOpenTime = iexOpenTime;
    }

    @JsonProperty("iexRealtimePrice")
    public double getIexRealtimePrice() {
        return iexRealtimePrice;
    }

    @JsonProperty("iexRealtimePrice")
    public void setIexRealtimePrice(double iexRealtimePrice) {
        this.iexRealtimePrice = iexRealtimePrice;
    }

    @JsonProperty("iexRealtimeSize")
    public long getIexRealtimeSize() {
        return iexRealtimeSize;
    }

    @JsonProperty("iexRealtimeSize")
    public void setIexRealtimeSize(long iexRealtimeSize) {
        this.iexRealtimeSize = iexRealtimeSize;
    }

    @JsonProperty("iexVolume")
    public long getIexVolume() {
        return iexVolume;
    }

    @JsonProperty("iexVolume")
    public void setIexVolume(long iexVolume) {
        this.iexVolume = iexVolume;
    }

    @JsonProperty("lastTradeTime")
    public long getLastTradeTime() {
        return lastTradeTime;
    }

    @JsonProperty("lastTradeTime")
    public void setLastTradeTime(long lastTradeTime) {
        this.lastTradeTime = lastTradeTime;
    }

    @JsonProperty("latestPrice")
    public long getLatestPrice() {
        return latestPrice;
    }

    @JsonProperty("latestPrice")
    public void setLatestPrice(long latestPrice) {
        this.latestPrice = latestPrice;
    }

    @JsonProperty("latestSource")
    public String getLatestSource() {
        return latestSource;
    }

    @JsonProperty("latestSource")
    public void setLatestSource(String latestSource) {
        this.latestSource = latestSource;
    }

    @JsonProperty("latestTime")
    public String getLatestTime() {
        return latestTime;
    }

    @JsonProperty("latestTime")
    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }

    @JsonProperty("latestUpdate")
    public long getLatestUpdate() {
        return latestUpdate;
    }

    @JsonProperty("latestUpdate")
    public void setLatestUpdate(long latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    @JsonProperty("latestVolume")
    public Object getLatestVolume() {
        return latestVolume;
    }

    @JsonProperty("latestVolume")
    public void setLatestVolume(Object latestVolume) {
        this.latestVolume = latestVolume;
    }

    @JsonProperty("low")
    public Object getLow() {
        return low;
    }

    @JsonProperty("low")
    public void setLow(Object low) {
        this.low = low;
    }

    @JsonProperty("lowSource")
    public Object getLowSource() {
        return lowSource;
    }

    @JsonProperty("lowSource")
    public void setLowSource(Object lowSource) {
        this.lowSource = lowSource;
    }

    @JsonProperty("lowTime")
    public Object getLowTime() {
        return lowTime;
    }

    @JsonProperty("lowTime")
    public void setLowTime(Object lowTime) {
        this.lowTime = lowTime;
    }

    @JsonProperty("marketCap")
    public long getMarketCap() {
        return marketCap;
    }

    @JsonProperty("marketCap")
    public void setMarketCap(long marketCap) {
        this.marketCap = marketCap;
    }

    @JsonProperty("oddLotDelayedPrice")
    public Object getOddLotDelayedPrice() {
        return oddLotDelayedPrice;
    }

    @JsonProperty("oddLotDelayedPrice")
    public void setOddLotDelayedPrice(Object oddLotDelayedPrice) {
        this.oddLotDelayedPrice = oddLotDelayedPrice;
    }

    @JsonProperty("oddLotDelayedPriceTime")
    public Object getOddLotDelayedPriceTime() {
        return oddLotDelayedPriceTime;
    }

    @JsonProperty("oddLotDelayedPriceTime")
    public void setOddLotDelayedPriceTime(Object oddLotDelayedPriceTime) {
        this.oddLotDelayedPriceTime = oddLotDelayedPriceTime;
    }

    @JsonProperty("open")
    public Object getOpen() {
        return open;
    }

    @JsonProperty("open")
    public void setOpen(Object open) {
        this.open = open;
    }

    @JsonProperty("openTime")
    public Object getOpenTime() {
        return openTime;
    }

    @JsonProperty("openTime")
    public void setOpenTime(Object openTime) {
        this.openTime = openTime;
    }

    @JsonProperty("openSource")
    public String getOpenSource() {
        return openSource;
    }

    @JsonProperty("openSource")
    public void setOpenSource(String openSource) {
        this.openSource = openSource;
    }

    @JsonProperty("peRatio")
    public double getPeRatio() {
        return peRatio;
    }

    @JsonProperty("peRatio")
    public void setPeRatio(double peRatio) {
        this.peRatio = peRatio;
    }

    @JsonProperty("previousClose")
    public double getPreviousClose() {
        return previousClose;
    }

    @JsonProperty("previousClose")
    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    @JsonProperty("previousVolume")
    public long getPreviousVolume() {
        return previousVolume;
    }

    @JsonProperty("previousVolume")
    public void setPreviousVolume(long previousVolume) {
        this.previousVolume = previousVolume;
    }

    @JsonProperty("primaryExchange")
    public String getPrimaryExchange() {
        return primaryExchange;
    }

    @JsonProperty("primaryExchange")
    public void setPrimaryExchange(String primaryExchange) {
        this.primaryExchange = primaryExchange;
    }

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("volume")
    public Object getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public void setVolume(Object volume) {
        this.volume = volume;
    }

    @JsonProperty("week52High")
    public double getWeek52High() {
        return week52High;
    }

    @JsonProperty("week52High")
    public void setWeek52High(double week52High) {
        this.week52High = week52High;
    }

    @JsonProperty("week52Low")
    public double getWeek52Low() {
        return week52Low;
    }

    @JsonProperty("week52Low")
    public void setWeek52Low(double week52Low) {
        this.week52Low = week52Low;
    }

    @JsonProperty("ytdChange")
    public double getYtdChange() {
        return ytdChange;
    }

    @JsonProperty("ytdChange")
    public void setYtdChange(double ytdChange) {
        this.ytdChange = ytdChange;
    }

    @JsonProperty("isUSMarketOpen")
    public boolean isIsUSMarketOpen() {
        return isUSMarketOpen;
    }

    @JsonProperty("isUSMarketOpen")
    public void setIsUSMarketOpen(boolean isUSMarketOpen) {
        this.isUSMarketOpen = isUSMarketOpen;
    }
}


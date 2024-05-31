package ca.jrvs.apps.trading.model;

public class MarketOrder {
    private int accountId;
    private String ticker;
    private int size;
    private int traderId;
    private enum Option{BUY, SELL}

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int id) {
        this.accountId = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTraderId() {
        return traderId;
    }

    public void setTraderId(int traderId) {
        this.traderId = traderId;
    }
}

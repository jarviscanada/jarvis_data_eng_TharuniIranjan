package ca.jrvs.apps.trading.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Quote {
    @Id
    private String ticker; //primary key
    @Column(name = "last_price")
    private Double lastPrice;
    @Column(name = "bid_price")
    private Double bidPrice;
    @Column(name = "bid_size")
    private Integer bidSize;
    @Column(name = "ask_price")
    private Double askPrice;
    @Column(name = "ask_size")
    private Integer askSize;

    //getters and setters

    public String getId() {
        return ticker;
    }

    public void setId(String ticker) {
        this.ticker = ticker;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Integer getBidSize() {
        return bidSize;
    }

    public void setBidSize(Integer bidSize) {
        this.bidSize = bidSize;
    }

    public Double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Double askPrice) {
        this.askPrice = askPrice;
    }

    public Integer getAskSize() {
        return askSize;
    }

    public void setAskSize(Integer askSize) {
        this.askSize = askSize;
    }
}

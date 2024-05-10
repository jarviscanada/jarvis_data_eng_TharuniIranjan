package ca.jrvs.apps.trading.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Quote {
    @Id
    private String ticker; //primary key
    private Double last_price;
    private Double bid_price;
    private Integer bid_size;
    private Double ask_price;
    private Integer ask_size;

    //getters and setters

    public String getId() {
        return ticker;
    }

    public void setId(String ticker) {
        this.ticker = ticker;
    }

    public Double getLastPrice() {
        return last_price;
    }

    public void setLastPrice(Double last_price) {
        this.last_price = last_price;
    }

    public Double getBidPrice() {
        return bid_price;
    }

    public void setBidPrice(Double bid_price) {
        this.bid_price = bid_price;
    }

    public Integer getBidSize() {
        return bid_size;
    }

    public void setBidSize(Integer bid_size) {
        this.bid_size = bid_size;
    }

    public Double getAskPrice() {
        return ask_price;
    }

    public void setAskPrice(Double ask_price) {
        this.ask_price = ask_price;
    }

    public Integer getAskSize() {
        return ask_size;
    }

    public void setAskSize(Integer ask_size) {
        this.ask_size = ask_size;
    }
}

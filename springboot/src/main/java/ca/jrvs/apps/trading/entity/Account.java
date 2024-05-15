package ca.jrvs.apps.trading.entity;

import javax.persistence.*;

@Entity
public class Account {
    @Id
    int id;
    int trader_id;
    double amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTraderId() {
        return trader_id;
    }

    public void setTraderId(int trader_id) {
        this.trader_id = trader_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
package ca.jrvs.apps.trading.entity;

import javax.persistence.*;

@Entity
@Table(name = "security_order")
public class SecurityOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long account_id;
    private String ticker;
    private String status;
    private int size;
    private double price;
    private String notes;

    public long getId() {
        return id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

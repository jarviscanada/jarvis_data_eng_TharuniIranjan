package ca.jrvs.apps.trading.entity;

<<<<<<< HEAD
import javax.persistence.*;
=======
import javax.persistence.Entity;
import javax.persistence.Id;
>>>>>>> parent of d3cd226 (quote/account/trader/order entity/dao/test imp)

@Entity
public class Account {
    @Id
    int id;
<<<<<<< HEAD
    @Column(name = "trader_id")
    private int traderId;
=======
    int traderId;
>>>>>>> parent of d3cd226 (quote/account/trader/order entity/dao/test imp)
    double amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTraderId() {
        return traderId;
    }

    public void setTraderId(int traderId) {
        this.traderId = traderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

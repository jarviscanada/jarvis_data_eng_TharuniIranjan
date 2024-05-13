package ca.jrvs.apps.trading.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity

public class Position {
    @Id
    private long id;

    public long getSecurity_order_id() {
        return security_order_id;
    }

    public void setSecurity_order_id(long security_order_id) {
        this.security_order_id = security_order_id;
    }

    public Date getDate_of_position() {
        return date_of_position;
    }

    public void setDate_of_position(Date date_of_position) {
        this.date_of_position = date_of_position;
    }

    private long security_order_id;
    private Date date_of_position;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

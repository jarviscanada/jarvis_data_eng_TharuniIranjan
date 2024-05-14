package ca.jrvs.apps.trading.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity

public class Position {
    @Setter
    @Getter
    @Id
    private long id;
    @Column(name = "account_id")
    private long accountId;
    private Date date_of_position;

}

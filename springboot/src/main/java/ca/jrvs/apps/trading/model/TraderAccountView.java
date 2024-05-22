package ca.jrvs.apps.trading.model;

import ca.jrvs.apps.trading.entity.Account;
import ca.jrvs.apps.trading.entity.Trader;

public class TraderAccountView {
    private Trader trader;
    private Account account;

    public TraderAccountView (Trader savedTrader, Account savedAccount) {
        this.trader = savedTrader;
        this.account = savedAccount;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

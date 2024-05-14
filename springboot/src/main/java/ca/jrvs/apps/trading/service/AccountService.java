package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.entity.Account;
import ca.jrvs.apps.trading.jpa.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountJpaRepository accountRepo;

    @Autowired
    public AccountService(AccountJpaRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    /**
     * Deletes the account if the balance is 0
     * @param traderId cannot be null
     * @throws IllegalArgumentException if unable to delete
     */
    @Transactional
    public void deleteAccountByTraderId(Integer traderId) {
        Optional<Account> optionalAccount = Optional.ofNullable(accountRepo.getAccountByTraderId(traderId));
        Account account = optionalAccount.orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getAmount() != 0) {
            throw new IllegalArgumentException("Balance is not 0");
        }

        accountRepo.deleteById(account.getId());
    }
}

package ca.jrvs.apps.trading.jpa;

import ca.jrvs.apps.trading.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AccountJpaRepository extends JpaRepository<Account, Integer> {
    Account getAccountByTraderId(Integer traderId);

}
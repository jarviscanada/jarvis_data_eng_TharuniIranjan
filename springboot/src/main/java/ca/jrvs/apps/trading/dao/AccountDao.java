package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountDao extends JpaRepository<Account, Integer> {
    List<Account> findAll();
    Optional<Account> findById(int id);
    boolean existsById(int id);
    void deleteById(int id);
    long count();
    void deleteAll();
}

package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.entity.SecurityOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityOrderDao extends JpaRepository<SecurityOrder, Long> {
    List<SecurityOrder> findAll();
    Optional<SecurityOrder> findById(long id);
    boolean existsById(long id);
    void deleteById(long id);
    long count();
    void deleteAll();

    void deleteAllByAccountId(long traderId);
}

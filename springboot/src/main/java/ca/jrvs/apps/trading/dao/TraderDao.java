package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.entity.Trader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraderDao extends JpaRepository<Trader, Integer> {
    Optional<Trader> findById(int id);
    List<Trader> findAll();
    List<Trader> findAllById(List<Integer> ids);
    boolean existsById(int id);
    void deleteById(int id);
    void deleteAll();
    long count();

}
package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.entity.Position;
import ca.jrvs.apps.trading.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionDao extends JpaRepository<Position, Long> {
    List<Position> findAll();
    Optional<Position> findById(long id);
    boolean existsById(long id);
    void deleteById(long id);
    long count();
    void deleteAll();

    long findByAccountId(long traderId);
}


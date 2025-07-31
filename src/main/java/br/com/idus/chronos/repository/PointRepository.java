package br.com.idus.chronos.repository;

import br.com.idus.chronos.domain.Point;
import br.com.idus.chronos.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByUserAndTimestampBetweenOrderByTimestampAsc(User user, Instant start, Instant end);
}
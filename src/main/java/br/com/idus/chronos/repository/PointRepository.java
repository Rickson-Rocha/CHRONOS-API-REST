package br.com.idus.chronos.repository;

import br.com.idus.chronos.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}

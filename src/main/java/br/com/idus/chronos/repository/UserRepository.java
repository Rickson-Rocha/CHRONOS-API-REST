package br.com.idus.chronos.repository;

import br.com.idus.chronos.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
}

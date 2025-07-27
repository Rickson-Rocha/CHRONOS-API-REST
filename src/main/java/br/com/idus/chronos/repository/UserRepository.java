package br.com.idus.chronos.repository;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.out.UserBasicResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    Page<User> findAll(Pageable pageable);
    Optional<User> findByEmail(String email);
}

package br.com.idus.chronos.repository;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.out.UserBasicResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    Page<User> findAll(Pageable pageable);
}

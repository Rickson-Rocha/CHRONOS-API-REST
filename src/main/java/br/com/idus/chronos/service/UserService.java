package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.UserCreateDTO;
import br.com.idus.chronos.dto.out.UserBasicResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User create(UserCreateDTO userCreateDTO);
    UserBasicResponseDTO findById(Long id);
    UserBasicResponseDTO findByCpf(String cpf);
    UserBasicResponseDTO findByEmail(String email);
    Page<UserBasicResponseDTO> findall(Pageable pageable);
}

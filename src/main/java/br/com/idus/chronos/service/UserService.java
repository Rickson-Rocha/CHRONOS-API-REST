package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.UserCreateDTO;
import br.com.idus.chronos.dto.out.UserBasicResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User create(UserCreateDTO userCreateDTO);
    UserBasicResponseDTO findById(Long id);
    Page<UserBasicResponseDTO> findAll(Pageable pageable);
    UserBasicResponseDTO findByCpf(String cpf);
    UserBasicResponseDTO findByEmail(String email);
    Page<UserBasicResponseDTO> findall(Pageable pageable);

}
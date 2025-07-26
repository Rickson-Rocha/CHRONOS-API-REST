package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.UserCreateDTO;

public interface UserService {
    User create(UserCreateDTO userCreateDTO);
}

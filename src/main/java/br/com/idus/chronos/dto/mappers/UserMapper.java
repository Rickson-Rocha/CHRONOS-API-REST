package br.com.idus.chronos.dto.mappers;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.UserCreateDTO;
import br.com.idus.chronos.dto.out.UserBasicResponseDTO;


public class UserMapper {

    public static User toEntity(UserCreateDTO dto, WorkJourney workJourney) {

        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .cpf(dto.cpf())
                .password(dto.password())
                .roles(dto.role())
                .workJourney(workJourney)
                .build();
    }

    public static UserBasicResponseDTO toResponseDTO(User user) {

        return new UserBasicResponseDTO(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail(),
                user.getRoles().name(),
                WorkJourneyMapper.toResponseDTO(user.getWorkJourney())
        );
    }

}

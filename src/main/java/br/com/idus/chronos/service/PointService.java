package br.com.idus.chronos.service;


import br.com.idus.chronos.domain.Point;
import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.PointCreateDTO;
import br.com.idus.chronos.dto.out.PointResponseDTO;

public interface PointService {
    PointResponseDTO create(PointCreateDTO pointCreateDTO, User authenticatedUser);
}

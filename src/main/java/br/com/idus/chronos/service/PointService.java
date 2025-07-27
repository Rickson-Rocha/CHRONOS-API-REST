package br.com.idus.chronos.service;


import br.com.idus.chronos.domain.Point;
import br.com.idus.chronos.dto.in.PointCreateDTO;

public interface PointService {
    Point create(PointCreateDTO pointCreateDTO);
}

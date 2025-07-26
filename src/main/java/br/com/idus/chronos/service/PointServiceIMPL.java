package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.Point;
import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.PointCreateDTO;
import br.com.idus.chronos.dto.mappers.PointMapper;
import br.com.idus.chronos.repository.PointRepository;
import br.com.idus.chronos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PointServiceIMPL implements PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public PointServiceIMPL(PointRepository pointRepository, UserRepository userRepository) {
        this.pointRepository = pointRepository;
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public Point create(PointCreateDTO pointCreateDTO) {
        User existingUser = userRepository.findById(pointCreateDTO.userId()).
                orElseThrow(()-> new RuntimeException("Usuário não encontrado"));
        Point newUser = PointMapper.toEntity(pointCreateDTO,existingUser);
        return pointRepository.save(newUser);
    }
}

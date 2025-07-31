package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.Point;
import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.PointCreateDTO;
import br.com.idus.chronos.dto.mappers.PointMapper;
import br.com.idus.chronos.dto.out.PointResponseDTO;
import br.com.idus.chronos.repository.PointRepository;
import br.com.idus.chronos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public PointResponseDTO create(PointCreateDTO pointCreateDTO, User detachedAuthenticatedUser) {

        User managedUser = userRepository.findById(detachedAuthenticatedUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        Point newPoint = PointMapper.toEntity(pointCreateDTO);

        managedUser.addPoint(newPoint);


        Point savedPoint = pointRepository.save(newPoint);

        return PointMapper.toResponseDTO(savedPoint);
    }
}

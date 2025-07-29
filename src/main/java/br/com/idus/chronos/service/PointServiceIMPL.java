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
    // Construtor atualizado
    public PointServiceIMPL(PointRepository pointRepository, UserRepository userRepository) {
        this.pointRepository = pointRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public PointResponseDTO create(PointCreateDTO pointCreateDTO, User detachedAuthenticatedUser) {
        // 1. Busque uma instância "viva" do usuário usando o ID do usuário desanexado.
        User managedUser = userRepository.findById(detachedAuthenticatedUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        Point newPoint = PointMapper.toEntity(pointCreateDTO);

        // 2. Agora use a instância gerenciada (managedUser) para adicionar o ponto.
        //    A coleção 'points' será inicializada corretamente dentro da transação atual.
        managedUser.addPoint(newPoint);

        // 3. Salve o ponto (se não estiver usando CascadeType.PERSIST no User)
        Point savedPoint = pointRepository.save(newPoint);

        return PointMapper.toResponseDTO(savedPoint);
    }
}

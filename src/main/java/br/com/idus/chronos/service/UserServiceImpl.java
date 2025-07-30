package br.com.idus.chronos.service;
import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.UserCreateDTO;
import br.com.idus.chronos.dto.mappers.UserMapper;
import br.com.idus.chronos.dto.out.UserBasicResponseDTO;
import br.com.idus.chronos.dto.out.UserFullResponseDTO;
import br.com.idus.chronos.repository.UserRepository;
import br.com.idus.chronos.repository.WorkJourneyRepository;
import br.com.idus.chronos.service.exceptions.ConflictException;
import br.com.idus.chronos.service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final WorkJourneyRepository workJourneyRepository;
    private  final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, WorkJourneyRepository workJourneyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;

        this.workJourneyRepository = workJourneyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User create(UserCreateDTO userCreateDTO) {
        if(userRepository.existsByEmail(userCreateDTO.email())){
            throw  new ConflictException("Email cadastrado no sistema");
        }

        if(userRepository.existsByCpf(userCreateDTO.cpf())){
            throw  new ConflictException("CPF já cadastrado no sistema");
        }
        WorkJourney workJourney = workJourneyRepository.findById(userCreateDTO.workJourneyId())
                .orElseThrow(() -> new ConflictException("Regime de jornada não encontrado com o ID: " + userCreateDTO.workJourneyId()));
        String userPwd = passwordEncoder.encode(userCreateDTO.password());
        User user = UserMapper.toEntity(userCreateDTO,workJourney);
        user.setPassword(userPwd);
        return userRepository.save(user);
    }

    @Override
    public UserBasicResponseDTO findById(Long id) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        return UserMapper.toResponseDTO(existingUser);
    }

    @Override
    public Page<UserBasicResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserMapper::toResponseDTO);
    }



    @Override
    public UserBasicResponseDTO findByCpf(String cpf) {
        return null;
    }

    @Override
    public UserBasicResponseDTO findByEmail(String email) {
        return null;
    }

    @Override
    public Page<UserBasicResponseDTO> findall(Pageable pageable) {
        Page<User> machines = this.userRepository.findAll(pageable);
        return machines.map(UserMapper::toResponseDTO);
    }


}

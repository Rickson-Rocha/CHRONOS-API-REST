package br.com.idus.chronos.config.db.seeding;



import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.UserCreateDTO;
import br.com.idus.chronos.enums.TypeUser;
import br.com.idus.chronos.enums.WorkDayStatus;
import br.com.idus.chronos.repository.UserRepository;
import br.com.idus.chronos.repository.WorkJourneyRepository;
import br.com.idus.chronos.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;
    private final WorkJourneyRepository workJourneyRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando verificação de dados iniciais (seeding)...");


        seedWorkJourneys();


        if (userRepository.count() == 0) {
            log.info("Nenhum usuário encontrado. Populando banco de dados com usuários iniciais.");
            seedUsers();
        } else {
            log.info("O banco de dados já possui usuários. Nenhuma ação de seeding de usuário necessária.");
        }
    }


    private void seedWorkJourneys() {
        log.info("Verificando e criando jornadas de trabalho padrão, se necessário...");


        findOrCreateWorkJourney(
                "Jornada Padrão 8h",
                480,
                60
        );


        findOrCreateWorkJourney(
                "Jornada Flexível 6h",
                360,
                15
        );
    }

    private void findOrCreateWorkJourney(String description, int workloadMinutes, int breakMinutes) {
        Optional<WorkJourney> existingJourney = workJourneyRepository.findByDescription(description);
        if (existingJourney.isEmpty()) {
            WorkJourney newJourney = WorkJourney.builder()
                    .description(description)
                    .daily_workload_minutes(workloadMinutes)
                    .minimum_break_minutes(breakMinutes)
                    .status(WorkDayStatus.IN_PROGRESS) // Um status padrão
                    .build();
            workJourneyRepository.save(newJourney);
            log.info("Jornada '{}' criada com sucesso.", description);
        }
    }


    private void seedUsers() {

        WorkJourney journey8h = workJourneyRepository.findByDescription("Jornada Padrão 8h")
                .orElseThrow(() -> new RuntimeException("Jornada de 8h não encontrada. Seeding falhou."));
        WorkJourney journey6h = workJourneyRepository.findByDescription("Jornada Flexível 6h")
                .orElseThrow(() -> new RuntimeException("Jornada de 6h não encontrada. Seeding falhou."));


        createManagerUser(journey8h.getId());
        createEmployeeUser(journey6h.getId());
    }



    private void createManagerUser(Long workJourneyId) {
        log.info("Criando usuário GERENTE...");
        UserCreateDTO managerDto = new UserCreateDTO(
                "Gerente de Projetos", "22222222222", "manager@chronos.com",
                "manager123", TypeUser.ROLE_MANAGER, workJourneyId
        );
        try {
            userService.create(managerDto);
            log.info("Usuário GERENTE criado com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao criar usuário gerente: ", e);
        }
    }

    private void createEmployeeUser(Long workJourneyId) {
        log.info("Criando usuário COLABORADOR...");
        UserCreateDTO employeeDto = new UserCreateDTO(
                "Colaborador Padrão", "33333333333", "employee@chronos.com",
                "employee123", TypeUser.ROLE_EMPLOYEE, workJourneyId
        );
        try {
            userService.create(employeeDto);
            log.info("Usuário COLABORADOR criado com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao criar usuário colaborador: ", e);
        }
    }
}

package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.Point;
import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.WorkJourneyCreateDTO;
import br.com.idus.chronos.dto.mappers.WorkDaySummaryMapper;
import br.com.idus.chronos.dto.mappers.WorkJourneyMapper;
import br.com.idus.chronos.dto.out.CalculationResultResponseDTO;
import br.com.idus.chronos.dto.out.WorkDaySummaryResponseDTO;
import br.com.idus.chronos.enums.WorkDayStatus;
import br.com.idus.chronos.repository.PointRepository;
import br.com.idus.chronos.repository.WorkJourneyRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkJourneyServiceIMPL  implements  WorkJourneyService {
    private final  WorkJourneyRepository workJourneyRepository;
    private final PointRepository pointRepository;
    private final PointCalculationServiceImpl pointCalculationService;

    public WorkJourneyServiceIMPL(WorkJourneyRepository workJourneyRepository, PointRepository pointRepository, PointCalculationServiceImpl pointCalculationService) {
        this.workJourneyRepository = workJourneyRepository;
        this.pointRepository = pointRepository;
        this.pointCalculationService = pointCalculationService;
    }

    @Override
    public WorkJourney create(WorkJourneyCreateDTO dto) {
        return workJourneyRepository.save(WorkJourneyMapper.toEntity(dto));

    }

    @Override
    public WorkDaySummaryResponseDTO getWorkDaySummaryForUser(User user, LocalDate date) {
        // Passo 1: Definir o intervalo de tempo para a consulta (o dia inteiro)
        // Usamos o fuso horário do servidor para definir o que é "hoje".
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Instant startOfDay = date.atStartOfDay(zoneId).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant();

        // Passo 2: Buscar os pontos do dia no repositório
        List<Point> pointsOfDay = pointRepository.findByUserAndTimestampBetweenOrderByTimestampAsc(user, startOfDay, endOfDay);

        // Se não houver pontos, retorna um resumo vazio
        if (pointsOfDay.isEmpty()) {
            return WorkDaySummaryMapper.toEmptyResponseDTO(date, user.getWorkJourney());
        }

        List<Instant> timestamps = pointsOfDay.stream().map(Point::getTimestamp).collect(Collectors.toList());


        CalculationResultResponseDTO result = pointCalculationService.calculateDurations(timestamps);
        Duration totalWork = result.totalWorkDuration();
        Duration totalBreak = result.totalBreakDuration();

        // Passo 4: Aplicar as regras de negócio para obter o saldo e o status
        WorkJourney workJourney = user.getWorkJourney();
        Duration expectedWorkload = Duration.ofMinutes(workJourney.getDaily_workload_minutes());

       // Calcula o saldo de horas (horas excedidas ou pendentes) [cite: 25, 26]
        Duration balance = totalWork.minus(expectedWorkload);

        // Determina o status final da jornada
        WorkDayStatus status = determineStatus(timestamps.size(), balance, expectedWorkload, totalWork);

        // Passo 5: Chamar o Mapper para formatar a resposta final [cite: 24]
        return WorkDaySummaryMapper.toResponseDTO(date, workJourney, timestamps, totalWork, totalBreak, balance, status);
    }

    /**
     * Helper para determinar o status da jornada com base nos pontos e no saldo.
     */
    private WorkDayStatus determineStatus(int pointsCount, Duration balance, Duration expected, Duration totalWork) {
        // Se o número de pontos for ímpar, a jornada está em andamento.
        if (pointsCount % 2 != 0) {
            return WorkDayStatus.IN_PROGRESS;
        }
        // Se a jornada terminou, verificamos o saldo
        if (totalWork.compareTo(expected) < 0) {
            return WorkDayStatus.INCOMPLETE;
        }
        if (balance.isZero()) {
            return WorkDayStatus.COMPLETED;
        }
        return WorkDayStatus.OVERTIME;
    }

}

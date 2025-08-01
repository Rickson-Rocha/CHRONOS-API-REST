package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.Point;
import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.WorkJourneyCreateDTO;
import br.com.idus.chronos.dto.mappers.UserMapper;
import br.com.idus.chronos.dto.mappers.WorkDaySummaryMapper;
import br.com.idus.chronos.dto.mappers.WorkJourneyMapper;
import br.com.idus.chronos.dto.out.CalculationResultResponseDTO;
import br.com.idus.chronos.dto.out.UserFullResponseDTO;
import br.com.idus.chronos.dto.out.WorkDaySummaryResponseDTO;
import br.com.idus.chronos.dto.out.WorkJourneyInfoResponseDTO;
import br.com.idus.chronos.enums.WorkDayStatus;
import br.com.idus.chronos.repository.PointRepository;
import br.com.idus.chronos.repository.UserRepository;
import br.com.idus.chronos.repository.WorkJourneyRepository;
import br.com.idus.chronos.service.exceptions.ResourceNotFoundException;
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
    private final UserRepository userRepository;

    public WorkJourneyServiceIMPL(WorkJourneyRepository workJourneyRepository, PointRepository pointRepository, PointCalculationServiceImpl pointCalculationService, UserRepository userRepository) {
        this.workJourneyRepository = workJourneyRepository;
        this.pointRepository = pointRepository;
        this.pointCalculationService = pointCalculationService;
        this.userRepository = userRepository;
    }

    @Override
    public WorkJourney create(WorkJourneyCreateDTO dto) {
        return workJourneyRepository.save(WorkJourneyMapper.toEntity(dto));

    }

    @Override
    public WorkDaySummaryResponseDTO getWorkDaySummaryForUser(User user, LocalDate date) {

        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Instant startOfDay = date.atStartOfDay(zoneId).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant();


        List<Point> pointsOfDay = pointRepository.findByUserAndTimestampBetweenOrderByTimestampAsc(user, startOfDay, endOfDay);


        if (pointsOfDay.isEmpty()) {
            return WorkDaySummaryMapper.toEmptyResponseDTO(date, user.getWorkJourney());
        }

        List<Instant> timestamps = pointsOfDay.stream().map(Point::getTimestamp).collect(Collectors.toList());


        CalculationResultResponseDTO result = pointCalculationService.calculateDurations(timestamps);
        Duration totalWork = result.totalWorkDuration();
        Duration totalBreak = result.totalBreakDuration();


        WorkJourney workJourney = user.getWorkJourney();
        Duration expectedWorkload = Duration.ofMinutes(workJourney.getDaily_workload_minutes());


        Duration balance = totalWork.minus(expectedWorkload);


        WorkDayStatus status = determineStatus(timestamps.size(), balance, expectedWorkload, totalWork);


        return WorkDaySummaryMapper.toResponseDTO(date, workJourney, timestamps, totalWork, totalBreak, balance, status);
    }

    @Override
    public UserFullResponseDTO getFullUserSummaryByIdAndDate(Long userId, LocalDate date) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId));


        WorkDaySummaryResponseDTO summaryDTO = this.getWorkDaySummaryForUser(user, date);

        return UserMapper.toFullResponseDTO(user, summaryDTO);
    }

    @Override
    public List<WorkJourneyInfoResponseDTO> findAll() {
        List<WorkJourney> workJourneys = workJourneyRepository.findAll();
        return WorkJourneyMapper.toResponseDTOList(workJourneys);
    }



    private WorkDayStatus determineStatus(int pointsCount, Duration balance, Duration expected, Duration totalWork) {

        if (pointsCount % 2 != 0) {
            return WorkDayStatus.IN_PROGRESS;
        } // Se a jornada terminou, verificamos o saldo
        if (totalWork.compareTo(expected) < 0) {
            return WorkDayStatus.INCOMPLETE;
        }
        if (balance.isZero()) {
            return WorkDayStatus.COMPLETED;
        }
        return WorkDayStatus.OVERTIME;
    }

}
package br.com.idus.chronos.service;

import br.com.idus.chronos.dto.out.CalculationResultResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointCalculationServiceTest {
    private final PointCalculationService calculationService = new PointCalculationServiceImpl();

    @Test
    @DisplayName("Deve calcular 8h de trabalho e 1h de pausa para jornada padrão de 4 pontos")
    void shouldCalculateStandard4PointJourney() {
        // Arrange
        Instant startWork = Instant.parse("2025-07-28T11:28:00Z"); // 08:28 BRL (-3)
        Instant startBreak = Instant.parse("2025-07-28T15:15:00Z"); // 12:15 BRL
        Instant endBreak = Instant.parse("2025-07-28T16:15:00Z");   // 13:15 BRL
        Instant endWork = Instant.parse("2025-07-28T20:28:00Z");   // 17:28 BRL
        List<Instant> timestamps = List.of(startWork, startBreak, endBreak, endWork);

        // Act
        CalculationResultResponseDTO result = calculationService.calculateDurations(timestamps);

        // Assert
        assertEquals(Duration.ofHours(8), result.totalWorkDuration(), "A duração do trabalho deve ser de 8 horas.");
        assertEquals(Duration.ofHours(1), result.totalBreakDuration(), "A duração da pausa deve ser de 1 hora.");
    }

    @Test
    @DisplayName("Deve calcular corretamente para jornada flexível de 6 pontos")
    void shouldCalculateFlexible6PointJourney() {
        // Arrange
        List<Instant> timestamps = List.of(
                Instant.parse("2025-07-28T11:28:00Z"), // 08:28
                Instant.parse("2025-07-28T15:15:00Z"), // 12:15 (Pausa 1h)
                Instant.parse("2025-07-28T16:15:00Z"), // 13:15
                Instant.parse("2025-07-28T18:30:00Z"), // 15:30 (Pausa 1h)
                Instant.parse("2025-07-28T19:30:00Z"), // 16:30
                Instant.parse("2025-07-28T21:28:00Z")  // 18:28
        );

        // Act
        CalculationResultResponseDTO result = calculationService.calculateDurations(timestamps);

        // Assert
        assertEquals(Duration.ofHours(8), result.totalWorkDuration(), "O trabalho total deve ser 8 horas.");
        assertEquals(Duration.ofHours(2), result.totalBreakDuration(), "A pausa total deve ser 2 horas.");
    }


    @Test
    @DisplayName("Deve calcular o trabalho para jornada em andamento com 3 pontos")
    void shouldCalculateInProgressJourneyWith3Points() {
        // Arrange
        List<Instant> timestamps = List.of(
                Instant.parse("2025-07-28T12:00:00Z"), // 09:00 BRL
                Instant.parse("2025-07-28T16:00:00Z"), // 13:00 BRL (Pausa...)
                Instant.parse("2025-07-28T17:00:00Z")  // 14:00 BRL
        );

        // Act
        CalculationResultResponseDTO result = calculationService.calculateDurations(timestamps);

        // Assert
        assertEquals(Duration.ofHours(4), result.totalWorkDuration(), "Apenas o primeiro período de trabalho deve ser computado.");
        assertEquals(Duration.ofHours(1), result.totalBreakDuration(), "Apenas a primeira pausa deve ser computada.");
    }

    @Test
    @DisplayName("Deve retornar zero para 2 pontos no mesmo horário")
    void shouldReturnZeroForTwoIdenticalPoints() {
        // Arrange
        Instant now = Instant.now();
        List<Instant> timestamps = List.of(now, now);

        // Act
        CalculationResultResponseDTO result = calculationService.calculateDurations(timestamps);

        // Assert
        assertEquals(Duration.ZERO, result.totalWorkDuration());
        assertEquals(Duration.ZERO, result.totalBreakDuration());
    }

    @Test
    @DisplayName("Deve retornar zero para uma lista de pontos vazia")
    void shouldReturnZeroForEmptyList() {
        // Arrange
        List<Instant> timestamps = Collections.emptyList();

        // Act
        CalculationResultResponseDTO result = calculationService.calculateDurations(timestamps);

        // Assert
        assertEquals(Duration.ZERO, result.totalWorkDuration());
        assertEquals(Duration.ZERO, result.totalBreakDuration());
    }
}

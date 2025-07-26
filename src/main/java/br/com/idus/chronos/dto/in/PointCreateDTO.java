package br.com.idus.chronos.dto.in;

import br.com.idus.chronos.enums.PointType;

public record PointCreateDTO(
        PointType pointEventType,
        Long userId
) {
}

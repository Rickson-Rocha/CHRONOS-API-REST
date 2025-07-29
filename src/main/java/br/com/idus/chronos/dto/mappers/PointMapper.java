package br.com.idus.chronos.dto.mappers;


import br.com.idus.chronos.domain.Point;
import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.PointCreateDTO;
import br.com.idus.chronos.dto.out.PointResponseDTO;

public class PointMapper {

    public static Point toEntity(PointCreateDTO pointCreateDTO) {
        Point point = new Point();
        point.setTimestamp(pointCreateDTO.timestamp());
        return point;
    };

    public static PointResponseDTO toResponseDTO(Point point) {
        return new PointResponseDTO(
                point.getId(),
                point.getUser().getId(),
                point.getTimestamp()
        );
    }
}

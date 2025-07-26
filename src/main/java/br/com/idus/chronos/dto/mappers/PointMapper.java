package br.com.idus.chronos.dto.mappers;


import br.com.idus.chronos.domain.Point;
import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.PointCreateDTO;

public class PointMapper {

    public static Point toEntity(PointCreateDTO pointCreateDTO, User user) {
        Point point = new Point();
        point.setPointEventType(pointCreateDTO.pointEventType());
        point.setUser(user);
        return point;
    };
}

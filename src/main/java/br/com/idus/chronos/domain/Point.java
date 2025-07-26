package br.com.idus.chronos.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

import br.com.idus.chronos.enums.PointType;

@Entity(name = "tb_point")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Point extends AbstractAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant dateTime = Instant.now();

    @Enumerated(EnumType.STRING)
    private PointType pointEventType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

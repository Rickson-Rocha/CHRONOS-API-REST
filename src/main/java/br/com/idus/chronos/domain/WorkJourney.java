package br.com.idus.chronos.domain;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_work_journey")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class WorkJourney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Integer daily_workload_minutes;

    private Integer minimum_break_minutes;

    @Builder.Default
    @OneToMany(mappedBy = "workJourney")
    private List<User> users = new ArrayList<>();
}

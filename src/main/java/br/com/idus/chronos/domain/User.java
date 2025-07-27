package br.com.idus.chronos.domain;

import br.com.idus.chronos.enums.TypeUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User  extends AbstractAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String cpf;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private TypeUser role;

    @ManyToOne
    @JoinColumn(name = "work_journey_id")
    private WorkJourney workJourney;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Point> points = new ArrayList<>();


}

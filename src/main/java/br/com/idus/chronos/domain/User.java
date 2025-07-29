package br.com.idus.chronos.domain;

import br.com.idus.chronos.enums.TypeUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity(name = "tb_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User  extends AbstractAuditable implements UserDetails {
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


    public void addPoint(Point point) {
        this.points.add(point);
        point.setUser(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (this.role == null) {
            return authorities;
        }

        // Você pode reusar a lógica que já tinha
        switch (this.role) {
            case ROLE_MANAGER:
                authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                break;
            case ROLE_EMPLOYEE:
                authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                break;
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}

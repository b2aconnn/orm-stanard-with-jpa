package jpql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String username;
    private int age;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}

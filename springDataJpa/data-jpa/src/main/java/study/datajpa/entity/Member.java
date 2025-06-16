package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue
//    @Column(name = "member_id")
    private Long id;

    private String username;

    public Member(String username) {
        this.username = username;
    }
}

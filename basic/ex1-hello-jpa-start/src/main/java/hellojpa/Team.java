package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Team extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    // mappedBy ("team") : team 으로 매핑이 되었다 라는 의미로 연관 관계의 주인이 아닐 때 사용.
    // 양방향이라고 하는 관계에서 한 쪽에서만 외래키를 관리(등록, 수정 등)을 해야 한다.
    // 결국 여기에다가 값을 세팅해도 DB 에 저장이 되지 않는다.
//    @OneToMany(mappedBy = "team")
//    private List<Member> members = new ArrayList<>();

}

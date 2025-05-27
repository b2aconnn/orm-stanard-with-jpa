package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter @Setter
@Entity
public class Member extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;
    // 주소
    @Embedded
    private Address homeAddress;

    // 값 타입 컬렉션 대신 엔티티 연관 관계로 푸는 게 더 나은 선택일 가능성이 높다라고 생각함.
    // cascade = ALL, orpahnRemoval = true 때문에 member 가 삭제가 되면,
    // 같이 history도 삭제되면 안 되는데 같이 삭제 되는 위험이 있지 않을까도 생각됨.
    // 또 값을 update 할 때, joinColum 관련해 delete 다 하고, 다시 insert를 하는 구조로 동작함.
    // 위와 같을 때는 전체 값을 키로 잡는 방법이 있지만, 언제 어떻게 키로 잡으면 안 되는 상황이 올 수도 있을 거 같아서..
    // 그냥 차라리 엔티티 연관 관계로 푸는 게 유지보수하기 좋지 않을까라는 생각이 듦.

    // 정말 정말 단순한 (ex. 셀렉트 박스로 필요한 값들? 크게 중요하지 않은?) 상태 저장할 때 쓰면 괜찮을 듯?
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOODS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    private List<Address> addressHistory = new ArrayList<>();

}

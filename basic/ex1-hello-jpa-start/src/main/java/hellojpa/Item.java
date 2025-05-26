package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
// 기본으로 JOINED 방식.
// 비즈니스가 단순하면 SINGLE_TABLE 방식.
// TABLE_PER_CLASS 는 사용을 지양하자.
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "DIS_TYPE") // type을 둘 수 있음
@Entity
public abstract class Item {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;
}

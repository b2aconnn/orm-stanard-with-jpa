package hellojpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@DiscriminatorValue(value = "M")
@Entity
public class Movie extends Item {
    private String director;
    private String actor;
}

package hellojpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@DiscriminatorValue(value = "B")
@Entity
public class Book extends Item {
    private String author;
    private String isbn;
}

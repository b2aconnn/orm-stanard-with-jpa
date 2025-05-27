package hellojpa;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
//@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;
}

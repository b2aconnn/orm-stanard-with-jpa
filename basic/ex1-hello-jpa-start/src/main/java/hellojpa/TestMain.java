package hellojpa;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMain {
    public static void main(String[] args) {
        Address address1 = new Address("city1", "street1", "zipcode1");
        Address address2 = new Address("city1", "street1", "zipcode1");

        log.info("equals : {}", (address1.equals(address2)));
    }
}

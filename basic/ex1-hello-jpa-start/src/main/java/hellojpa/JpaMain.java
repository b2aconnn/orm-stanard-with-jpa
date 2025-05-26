package hellojpa;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 스레드간의 공유하면 안됨.
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setCreatedBy("memberA");
            member.setUsername("memberA");
            member.setCreatedDate(LocalDateTime.now());

            em.persist(member);

//            em.flush();
//            em.clear();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
     }
}

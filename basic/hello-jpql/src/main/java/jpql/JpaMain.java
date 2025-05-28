package jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

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
            member.setUsername("userA");
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("userB");
            em.persist(member2);

            // hibernate 6.1 version 부터 from 절 서브쿼리가 가능함.
            // 서브 쿼리 안의 select 문에 꼭 alias 붙여야 함.
            String query = "select mm.username from (select m2.username as username from Member m2) as mm";
            List<String> resultList =
                    em.createQuery(query, String.class)
                    .getResultList();

            for (String findMember : resultList) {
                log.info("username : {}", findMember);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
     }
}

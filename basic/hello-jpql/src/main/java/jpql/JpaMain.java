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

            // null return 할 경우, NoResultException이 반한되는데
            // spring data jpa 에서는 추상화해서 제공하기 때문에 내부에서 exception catch 후에 return을 null 이나 Optional을 return 해준다.
//            Member findMember = em.createQuery("select m from Member m where m.id = :id", Member.class)
//                    .setParameter("id", 2)
//                    .getSingleResult();
//            log.info("username : {}", findMember.getUsername());

            List<MemberDto> resultList = em.createQuery("select new jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                    .getResultList();

            for (MemberDto memberDto : resultList) {
                log.info("memberDto : {}", memberDto.getUsername());
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

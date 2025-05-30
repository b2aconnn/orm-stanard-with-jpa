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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member = new Member();
            member.setUsername("userA");
            member.setType(MemberType.ADMIN);
            member.setTeam(team);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("userB");
            member2.setType(MemberType.USER);
            member2.setTeam(team);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("userC");
            member3.setType(MemberType.ADMIN);
            member3.setTeam(teamB);
            em.persist(member3);

//            em.flush();
//            em.clear();

            // 벌크 연산은 영속성 컨텍스트를 거치지 않아서 꼭 clear를 하고 처리해야 한다.
            int result = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();
            em.clear();

            Member findMember = em.find(Member.class, 1L);
            log.info("find Member age = {}", findMember.getAge());


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

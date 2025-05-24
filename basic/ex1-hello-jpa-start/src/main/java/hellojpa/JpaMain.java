package hellojpa;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.TemporalAccessor;

@Slf4j
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 스레드간의 공유하면 안됨.
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 저장
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("memberA");
            member.setTeam(team);
            em.persist(member);

            // 1차 캐시가 아니라 DB 에 직접 넣고 조회를 해보고 싶을 때의 방법
//            em.flush();
//            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();
            log.info("findTeam = {}", findTeam.getName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
     }
}

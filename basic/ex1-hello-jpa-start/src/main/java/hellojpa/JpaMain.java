package hellojpa;

import jakarta.persistence.*;
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
            // 저장
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("memberA");
            member.setTeam(team);
            em.persist(member);

            // DB 에 실제 flush가 안 된 상태에서,
            // 1차 캐시에서 team.getMembers() 조회를 할 때 우리가 기대하는 member가 없기 때문에 (현재 로직은 빈 값)
            // 객체 지향 관점에서 같이 넣어주는 게 맞음.
            // 테스트 케이스에서도 문제가 발생함.
            team.getMembers().add(member);

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

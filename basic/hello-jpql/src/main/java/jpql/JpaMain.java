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

            em.flush();
            em.clear();
            
//            String query = "select m2.type as username from Member m2 where m2.type = jpql.MemberType.ADMIN";
//            List<MemberType> resultList =
//                    em.createQuery(query, MemberType.class)
//                    .getResultList();
//
//            for (MemberType item : resultList) {
//                log.info("result : {}", item);
//            }

            // JPQL 사용하면 연관관계 fetch 설정이 LAZY이든, EAGER 이든 상관 없이 N + 1 이 발생
            // (EAGER : 그래프 탐색 사용(ex.member.getTeam())을 하지 않아도 N + 1 바로 발생)
            // (LAZY : 그래프 탐색을 할 때 N + 1 발생)
//            List<Member> resultList = em.createQuery("select m from Member m join fetch m.team", Member.class)
//                    .getResultList();
//            for (Member item : resultList) {
//                log.info("result : {}", item.getTeam().getName());
//            }

//            Member member1 = em.find(Member.class, 1L);
//            log.info("member1 : {}", member1.getTeam().getName());


            // 컬렉션을 페치 조인하면 페이징 api 사용하지 않는 게 좋음. (DB 에서 데이터 싹 다 긁어와서 로직으로 메모리에서 걸러서 처리함..)
            // 역으로 하면 가능할지라도 컬렉션을 페치 조인하면 어쨋든 컬렉션 만큼의 데이터가 나오기 때문에 애초에
            // 페이징이 되는 게 이상하긴 함.
            // 근데 혹시라도 필요하다면!!! 반대로 해서 가져오면 됨.
            // 또는 그냥 batchSize 설정을 통해 해결.
            List<Team> resultList = em.createQuery("select m from Team m join fetch m.members", Team.class)
                    .setFirstResult(0) // 컬렉션 페치 조인 시 페이징 api 사용 XX !! 데이터 다 가져와서 메모리에서 처리됨.
                    .setMaxResults(1)
                    .getResultList();
            for (Team item : resultList) {
                log.info("result : teamName : {}, memberSize : {}", item.getName(), item.getMembers().size());

                for (Member itemMember : item.getMembers()) {
                    log.info("  --> memberName : {}", itemMember.getUsername());
                }
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

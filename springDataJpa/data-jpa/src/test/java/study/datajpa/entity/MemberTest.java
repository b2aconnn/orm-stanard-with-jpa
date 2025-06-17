package study.datajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void jpaEventBaseEntity() throws Exception {
        // given
        Member member = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).get();


        // then
        System.out.println("findMember.getCreatedDate : " + findMember.getCreatedDate());
        System.out.println("findMember.getUpdatedDate : " + findMember.getLastModifiedDate());
        System.out.println("findMember.getCreatedBy : " + findMember.getCreatedBy());
        System.out.println("findMember.getLastModifiedBy : " + findMember.getLastModifiedBy());
    }
}
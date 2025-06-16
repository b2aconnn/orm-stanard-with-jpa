package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        // given
        Member member = new Member("userA");
        Member saveMember = memberJpaRepository.save(member);

        // when
        Member findMember = memberJpaRepository.find(saveMember.getId());

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        // 영속성 컨텍스트에 저장을 하고, 저장되어 있는 객체를 가져오는 거기 떄문에 동일한 객체임을 보장.
        assertThat(findMember).isEqualTo(member);
    }
}
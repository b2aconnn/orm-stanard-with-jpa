package jpabook.jpashop;

import jdk.jfr.StackTrace;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

//@ActiveProfiles("test")
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    // test 할 때 @Transactional 되어 있으면 테스트 끝나면 commit이 아니라 rollback을 해버림.
    @Transactional
    @Rollback(false)
    void testMember() {
        // when
        Member member = new Member();
        member.setUsername("memberA");

        // then
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // given
        assertThat(findMember.getId()).isEqualTo(saveId);
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        // 영속성 컨텍스트에 들어 있는 친구들 조회해오기 떄문에 참조 값이 같은 친구임.
        // 그래서 둘이 같은 참조 객체라서 (주소 값이 같음) true가 나옴.
        // select query 안 나감.
        assertThat(findMember).isEqualTo(member);
    }
}
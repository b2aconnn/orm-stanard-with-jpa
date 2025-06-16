package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@Rollback(false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    void testMember() {
        // given
        Member member = new Member("userA");
        Member saveMember = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        // 영속성 컨텍스트에 저장을 하고, 저장되어 있는 객체를 가져오는 거기 떄문에 동일한 객체임을 보장.
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void paging() {
        // given
        memberRepository.save(new Member("user1", 10));
        memberRepository.save(new Member("user2", 10));
        memberRepository.save(new Member("user3", 10));
        memberRepository.save(new Member("user4", 10));
        memberRepository.save(new Member("user5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> memberPage = memberRepository.findByAge(age, pageRequest);

        // then
        List<Member> members = memberPage.get().collect(Collectors.toList());
        for (Member member : members) {
            System.out.println("### member : " + member.getUsername());
        }
//        long totalPages = memberPage.getTotalElements();
//        System.out.println("### totalPages : " + totalPages);

        assertThat(members.size()).isEqualTo(3);
//        assertThat(totalPages).isEqualTo(5);
//        assertThat(memberPage.getTotalPages()).isEqualTo(2);
        assertThat(memberPage.isFirst()).isTrue();
        assertThat(memberPage.hasNext()).isTrue();
    }

    @Test
    void updateTest() {
        memberRepository.save(new Member("user1", 20));
        memberRepository.save(new Member("user2", 20));
        memberRepository.save(new Member("user3", 30));
        memberRepository.save(new Member("user4", 10));
        memberRepository.save(new Member("user5", 10));

        // update 처리하는 거까지는 좋은데, 영속성 컨텍스트에는 관리가 되지 않으므로
        // 그 다음 조회를 할 때, 데이터가 불일치하는 문제가 생길 수 있다.
        // 위와 같은 상황에선 영속성 컨텍스트를 초기화해줘야 함.
        int result = memberRepository.bulkAgePlus(20);

        assertThat(result).isEqualTo(3);
    }

    @Test
    void entityGraphTest() {
        memberRepository.save(new Member("user1", 20));
        memberRepository.save(new Member("user2", 20));
        memberRepository.save(new Member("user3", 30));
        memberRepository.save(new Member("user4", 10));
        memberRepository.save(new Member("user5", 10));

        List<Member> result = memberRepository.findAll();
    }
}
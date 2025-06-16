package study.datajpa.repository;

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
}
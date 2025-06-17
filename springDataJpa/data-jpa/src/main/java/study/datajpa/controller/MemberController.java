package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    // spring data 에서 지원해줌.
    // ex : localhost:8080/members/page?page=0&size=10&sort=username,desc
    @GetMapping("/members")
    public Page<Member> list(@PageableDefault(size = 5) Pageable pageable) {
        return memberRepository.findAll(pageable);
        // Pageable에서 지원해줌.
//                .map(MemberDto::new);
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("userA"));
    }
}

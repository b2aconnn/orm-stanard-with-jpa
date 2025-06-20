package study.querydsl.repository;

import study.querydsl.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> search();
}

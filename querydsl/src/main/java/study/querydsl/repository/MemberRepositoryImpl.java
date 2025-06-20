package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;

import java.util.List;

import static study.querydsl.entity.QMember.member;

public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Member> search() {
        return queryFactory.select(member)
                .from(member)
                .fetch();
    }
}

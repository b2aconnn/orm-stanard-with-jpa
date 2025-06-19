package study.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.match.JsonPathRequestMatchers;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.math.BigDecimal;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@Transactional
@SpringBootTest
public class QuerydslBasicTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    void startJPQL() {
//        em.createQuery();
    }

    @Test
    void startQuerydsl() {
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void sort() {
        Member findMember = queryFactory.selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)
                )
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     */
    @Test
    void joinOnFiltering() {
//        List<Tuple> teamA = queryFactory
//                .select(member, team)
//                .from(member)
//                .leftJoin(team)
//                .on(member.team.eq(team).and(team.name.eq("teamA")))
//                .fetch();

        List<Tuple> teamA = queryFactory
                .select(member, team)
                .from(member)
                .join(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        System.out.println("=== TURTLE : " + teamA);
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    void fetchJoinNo() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).isTrue();
    }

    @Test
    void subQuery() {
        QMember subMember = new QMember("subMember");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(subMember.age.max())
                            .from(subMember)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    @Test
    void basicCase() {
        QMember subMember = new QMember("subMember");

        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타").as("age"))
                .from(member)
                .where(member.age.eq(
                        select(subMember.age.max())
                                .from(subMember)
                ))
                .fetch();
    }

    @Test
    void constant() {
        List<Tuple> tuples = queryFactory
                .select(member.age, Expressions.constant("A"))
                .from(member)
                .where(member.age.eq(40))
                .fetch();

         for (Tuple tuple : tuples) {
            System.out.println("=== tuple :" + tuple);
        }
    }

    @Test
    void concat() {
        // case 1
        List<String> case1 = queryFactory
                .select(Expressions.stringTemplate("CONCAT({0}, {1}, {2})", "1, ", "2, ", "3"))
                .from(member)
                .where(member.age.eq(40))
                .fetch();

        for (String result : case1) {
            System.out.println("=== result :" + result);
        }

        // case 2
        List<String> case2 = queryFactory
                .select(member.username.concat(",").concat(member.age.stringValue()))
                .from(member)
                .where(member.age.eq(40))
                .fetch();
    }

    @Test
    void tupleProjection() {
        List<Tuple> result = queryFactory.select(member.username, member.age)
                .from(member)
                .fetch();

        result.forEach(tuple -> {
            String username = tuple.get(member.username);
            System.out.println("username = " + username);
            Integer age = tuple.get(member.age);
            System.out.println("age = " + age);
        });
    }

    @Test
    void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findDtoByField() {
        // field 로 바로 꽂아버림.(내부에서 리플렉션으로 처리함.)
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void subQueryAlias() {
        QMember subMember = new QMember("subMember");
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        ExpressionUtils.as(JPAExpressions
                                .select(subMember.age.max())
                                .from(subMember), "age")))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }
}

package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired OrderService orderService;

    @Autowired MemberRepository memberRepository;
    @Autowired ItemRepository itemRepository;

    @Test
    void 상품주문() {
        // given
        Member member = new Member();
        member.setName("kim");
        memberRepository.save(member);

        Item item = new Book();
        item.setName("jpa book");
        item.setPrice(1_000);
        item.setStockQuantity(1);
        itemRepository.save(item);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // then
        // 배송지가 잘 저장이 되었는 지?
        // 주문 상품이 잘 저장 되었는 지?
        // 주문 저장을 잘 되었는 지?

    }

    @Test
    void 주문취소() {
        // given


        // when


        // then

    }

    @Test
    void 상품주문_재고수량초과() {
        // given


        // when


        // then

    }
}
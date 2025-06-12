package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jpabook.jpashop.domain.DeliveryStatus.COMP;
import static jpabook.jpashop.domain.OrderStatus.CANCEL;
import static jpabook.jpashop.domain.OrderStatus.ORDER;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 연관 관계 메서드 (양방향일 때 세팅해두면 좋음)
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 주문 취소
    public void cancel() {
        if (delivery.getStatus().equals(COMP)) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }

        this.status = CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 조회 로직
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}

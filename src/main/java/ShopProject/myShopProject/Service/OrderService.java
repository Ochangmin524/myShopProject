package ShopProject.myShopProject.Service;

import ShopProject.myShopProject.Domain.Delivery;
import ShopProject.myShopProject.Domain.DeliveryStatus;
import ShopProject.myShopProject.Domain.Item.Item;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Domain.Order.Order;
import ShopProject.myShopProject.Domain.Order.OrderItem;
import ShopProject.myShopProject.Domain.OrderSearch;
import ShopProject.myShopProject.Repository.ItemRepository;
import ShopProject.myShopProject.Repository.MemberRepository;
import ShopProject.myShopProject.Repository.OrderRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ShopProject.myShopProject.Domain.Order.Order.createOrder;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }



    //주문//
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member =  memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setStatus(DeliveryStatus.READY);
        delivery.setAddress(member.getAddress());

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    //주문 취소
    @Transactional
    public void cancel(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }



}

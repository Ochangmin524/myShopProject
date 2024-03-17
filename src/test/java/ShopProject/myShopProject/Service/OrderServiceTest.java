package ShopProject.myShopProject.Service;


import ShopProject.myShopProject.Domain.Address;
import ShopProject.myShopProject.Domain.Item.Book;
import ShopProject.myShopProject.Domain.Item.Category;
import ShopProject.myShopProject.Domain.Item.Item;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Domain.Order.Order;
import ShopProject.myShopProject.Domain.Order.OrderStatus;
import ShopProject.myShopProject.Exception.NotEnoughStockException;
import ShopProject.myShopProject.Repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.iterable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;


    //상품 주문 테스트
    @Test
    public void ItemOrderTest() throws Exception {
        //Given
        Member member = createMember();
        Item item = createBook();
        int orderCount = 2;

        //When
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
//

        //Then
        Order getOrder = orderRepository.findOne(orderId);
        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상태는 ORDER이어야 한다");
        assertEquals(1,getOrder.getOrderItems().size(),"주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(20000,getOrder.getTotalPrice(),"주문 가격은 가격 * 수량이다.");
        assertEquals(8,item.getStockQuantity(),"주문 수량만틈 재고가 줄어야 한다.");

    }


    //상품 주문 제고 초과
    @Test(expected = NotEnoughStockException.class)
    public void OverStockTest() throws Exception {
        //Given
        Member member = createMember();
        Item item = createBook();
        int orderCount = 12;

        //When
        orderService.order(member.getId(), item.getId(), orderCount);
        //Then
        fail("재고 수량 부족 예외가 터진다.");


    }


    //주문 취소 테스트
    @Test
    public void cancelTest() {
        Member member = createMember();
        Item item = createBook();
        int orderCount = 1;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        Order getOrder = orderRepository.findOne(orderId);
        getOrder.cancel();

        assertEquals(10, item.getStockQuantity(), "수량이 돌아와야 한다.");
        assertEquals(OrderStatus.CANCEL,getOrder.getStatus(), "상태는 cancel이다.");

    }


    private Book createBook() {
        Book book = new Book();
        book.setName("BookA");
        book.setStockQuantity(10);
        book.setPrice(10000);
        em.persist(book);
        return book;
    }
    private Member createMember() {
        Member member = new Member();
        member.setName("MemberA");
        member.setAddress(new Address("서울", "강남뷰", "100"));
        em.persist(member);
        return member;
    }
}

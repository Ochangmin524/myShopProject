package ShopProject.myShopProject.Domain;

import ShopProject.myShopProject.Domain.Item.Book;
import ShopProject.myShopProject.Domain.Order.Order;
import ShopProject.myShopProject.Domain.Order.OrderItem;

import ShopProject.myShopProject.Repository.CartRepositoryJPA;
import ShopProject.myShopProject.Repository.OrderRepository;
import ShopProject.myShopProject.Service.MemberService;
import ShopProject.myShopProject.api.dto.OrderDto;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
public class TestDataInit {
    @Autowired
    InitService initService;
    @Autowired
    static
    MemberService memberService;
    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }



    //orderItem 과 item이 한번씩의 쿼리로 1: 1: 1로 접근 가능하다 기존의 1:n:m 보다 빠르다.
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private Long id = 1L;
        private final EntityManager em;

        public void dbInit1() {

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);
            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);

        }

        public void dbInit2() {

            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
            em.persist(book1);
            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
            em.persist(book2);

        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setAuthor("author test");
            book.setIsbn("isbn test");
            book.setStockQuantity(stockQuantity);
            book.setCategory("Book");
            return book;
        }


    }
}


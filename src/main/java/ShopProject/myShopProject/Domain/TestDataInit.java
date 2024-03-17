package ShopProject.myShopProject.Domain;

import ShopProject.myShopProject.Domain.Item.Book;
import ShopProject.myShopProject.Domain.Item.Item;
import ShopProject.myShopProject.Service.ItemService;
import ShopProject.myShopProject.Service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ItemService itemService;
    private final MemberService memberService;

    @PostConstruct
    public void init() {
        log.info("초기 등록 시작");
        Book book = new Book();
        book.setPrice(10000);
        book.setName("testBook");
        book.setStockQuantity(100);
        book.setAuthor("작가");
        book.setIsbn("isbn");

        Address address = new Address("서울","거리", "zipcoded");

        Member member = new Member();
        member.setLoginId("test");
        member.setPassword("test");
        member.setName("이름입니다");
        member.setAddress(address);
        itemService.saveItem(book);
        memberService.join(member);

        Member admin = new Member();
        admin.setLoginId("admin");
        admin.setPassword("a");
        admin.setName("관리자");
        admin.setAddress(address);
        memberService.join(admin);
    }
}

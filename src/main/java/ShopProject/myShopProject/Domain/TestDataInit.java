package ShopProject.myShopProject.Domain;

import ShopProject.myShopProject.Domain.Item.Album;
import ShopProject.myShopProject.Domain.Item.Book;
import ShopProject.myShopProject.Service.ItemService;
import ShopProject.myShopProject.Service.MemberService;
import ShopProject.myShopProject.Service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestDataInit {
    private  Long id = 1L;

    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderService orderService;
    @PostConstruct
    public void init() {
        log.info("초기 등록 시작");
        //아이템 등록
        Book book = new Book();
        book.setPrice(10000);
        book.setName("testBook");
        book.setStockQuantity(100);
        book.setAuthor("작가");
        book.setIsbn("isbn");

        Album album = new Album();
        album.setPrice(20000);
        album.setName("testAlbum");
        album.setStockQuantity(2000);
        album.setArtist("아티스트");
        album.setEtc("ETC");
        //사용자 등록
        Address address = new Address("서울","거리", "zipcoded");

        Member member = createTestMember();
        Member member2 = createTestMember();

        itemService.saveItem(book);
        itemService.saveItem(album);
        memberService.join(member);
        memberService.join(member2);


        Member admin = new Member();
        admin.setLoginId("admin");
        admin.setPassword("a");
        admin.setName("관리자");
        admin.setAddress(address);
        memberService.join(admin);

        //사용자 주문 등록
        orderService.order(1L, 1L, 2);
        orderService.order(1L, 2L, 2);

        orderService.order(2L, 1L, 3);

    }

    public Member createTestMember() {
        Address address = new Address("서울"+id,"거리"+id, "zipcoded"+id);
        Member member = new Member();
        member.setLoginId("test"+id);
        member.setPassword("test"+id);
        member.setName("이름입니다"+id);
        member.setAddress(address);

        this.id += 1;
        return member;

    }

}

package ShopProject.myShopProject.web.Form;


import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class OrderForm {
    private Long id;
    private Long memberId;
    private Long itemId; // 배송 상품
    private Integer count;
}

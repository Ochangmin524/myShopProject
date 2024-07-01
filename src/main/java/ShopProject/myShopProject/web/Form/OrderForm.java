package ShopProject.myShopProject.web.Form;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
public class OrderForm {



    private Long memberId;
    private Long itemId; // 배송 상품

    @NotNull
    @Min(value = 1 , message = "주문 수량은 1 이상이어야 합니다.")
    private Integer count;
}

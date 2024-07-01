package ShopProject.myShopProject.web.Form;

import ShopProject.myShopProject.Domain.Cart;
import ShopProject.myShopProject.Domain.Item.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
public class CartForm   {

        @Id
        @GeneratedValue
        @Column(name = "cart_form_id")
        private Long cartFormId;

        private Long memberId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "item_id")
        private Item item; // 배송 상품

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cart_id")
        private Cart cart;

        @NotNull
        @Min(value = 1 , message = "주문 수량은 1 이상이어야 합니다.")
        private Integer count;

        public CartForm() {

        }
    }



package ShopProject.myShopProject.Domain;

import ShopProject.myShopProject.Domain.Order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;


}

package ShopProject.myShopProject.web.Form;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CartDTO {
    private ArrayList<OrderForm> forms;
    private Long cartFormId;
}

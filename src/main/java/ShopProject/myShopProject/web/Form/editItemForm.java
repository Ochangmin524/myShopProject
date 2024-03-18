package ShopProject.myShopProject.web.Form;

import ShopProject.myShopProject.Domain.Item.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class editItemForm {

    private Long id;

    @NotEmpty
    private String name;
    @NotNull
    private int price;
    @NotNull
    private int stockQuantity;

    private String author;
    private String isbn;

}

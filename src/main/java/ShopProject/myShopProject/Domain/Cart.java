package ShopProject.myShopProject.Domain;

import ShopProject.myShopProject.web.Form.CartForm;
import ShopProject.myShopProject.web.Form.OrderForm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long CartId;

    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<CartForm> carts = new ArrayList<>();
}

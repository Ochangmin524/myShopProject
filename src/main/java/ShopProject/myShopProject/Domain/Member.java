package ShopProject.myShopProject.Domain;
import ShopProject.myShopProject.Domain.Item.Item;
import ShopProject.myShopProject.Domain.Order.Order;
import ShopProject.myShopProject.web.Form.OrderForm;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member",cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();





    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private String loginId;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<LikedItem> likedItems = new ArrayList<>();


}

package ShopProject.myShopProject.Domain;
import ShopProject.myShopProject.Domain.Item.Item;
import ShopProject.myShopProject.Domain.Order.Order;
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


    private String loginId;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<LikedItem> likedItems = new ArrayList<>();


}

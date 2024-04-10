package ShopProject.myShopProject.Domain;

import ShopProject.myShopProject.Domain.Item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LikedItem {

    @Id
    @GeneratedValue
    @Column(name = "likedItem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;



}

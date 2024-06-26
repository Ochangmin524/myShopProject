package ShopProject.myShopProject.Domain.Item;


import ShopProject.myShopProject.Domain.LikedItem;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Exception.NotEnoughStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public class Item  implements Serializable {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long Id;

    private String name;
    private int price; //가격

    private int stockQuantity; //재고수량

    private int likes = 0; // 좋아요 수

    private String category;

    private String embeddingScore; // 임베딩 값

    //비즈니스 로직//
    //제고 수량 더하기
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity ) {
        int restStock = this.stockQuantity - quantity;

        if (restStock < 0) {
            throw new NotEnoughStockException("재고 수량이 부족합니다.");
        }
        this.stockQuantity = restStock;
    }
}

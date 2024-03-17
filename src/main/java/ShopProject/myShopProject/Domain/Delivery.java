package ShopProject.myShopProject.Domain;

import ShopProject.myShopProject.Domain.Order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Column(name ="delivery_id")
    @Id @GeneratedValue
    private Long Id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}

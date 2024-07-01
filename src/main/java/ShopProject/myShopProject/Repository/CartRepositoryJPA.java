package ShopProject.myShopProject.Repository;

import ShopProject.myShopProject.Domain.Cart;
import ShopProject.myShopProject.Domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepositoryJPA extends JpaRepository<Cart, Long> {
    Cart findByMember(Member member);

}

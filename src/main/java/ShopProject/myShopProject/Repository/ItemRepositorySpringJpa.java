package ShopProject.myShopProject.Repository;

import ShopProject.myShopProject.Domain.Item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepositorySpringJpa extends JpaRepository<Item, Long> {
    List<Item> findAllByOrderByLikesDesc();

    List<Item> findAllByOrderByPriceDesc();

    List<Item> findAllByOrderByNameDesc();
}

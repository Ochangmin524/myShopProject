package ShopProject.myShopProject.Repository;

import ShopProject.myShopProject.Domain.Item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepositorySpringJpa extends JpaRepository<Item, Long> {

    Page<Item> findAll(Pageable pageable);

}

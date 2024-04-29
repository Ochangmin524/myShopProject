package ShopProject.myShopProject.Service;

import ShopProject.myShopProject.Domain.Item.Item;
import ShopProject.myShopProject.Repository.ItemRepository;
import ShopProject.myShopProject.Repository.ItemRepositorySpringJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    //    private final ItemRepository itemRepository;
    private final ItemRepositorySpringJpa jpaItemRepository;

    @Transactional
    public void saveItem(Item item) {
        jpaItemRepository.save(item);
    }

    public List<Item> findItems() {
        return jpaItemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return jpaItemRepository.findById(itemId).get();
    }


    @Transactional
    public List<Item> findItemsByPrice() {
        return jpaItemRepository.findAllByOrderByPriceDesc();
    }

//    @Transactional
//    public List<Item> findByPrice() {
//        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,
//                "username"));
//    }

    @Transactional
    public List<Item> findItemsByLikes() {
        return jpaItemRepository.findAllByOrderByLikesDesc();
    }

    @Transactional
    public List<Item> findItemsByName() {
        return jpaItemRepository.findAllByOrderByNameDesc();
    }

    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity) {
        Item item = jpaItemRepository.findById(id).get();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);

    }
}

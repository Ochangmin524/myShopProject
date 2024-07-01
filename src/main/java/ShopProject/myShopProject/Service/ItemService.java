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

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final EmbeddingService embeddingService;

    //스프링 데이터 JPA로 리펙토링
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
    public void updateItem(Long id, String name, int price, int stockQuantity) throws IOException {
        Item item = jpaItemRepository.findById(id).get();
        item.setName(name);
        String embeddingScore = embeddingService.getEmbeddingScore(name);
        item.setEmbeddingScore(embeddingScore);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);

    }
}

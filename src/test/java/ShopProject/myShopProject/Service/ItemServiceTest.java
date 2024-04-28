package ShopProject.myShopProject.Service;


import ShopProject.myShopProject.Domain.Item.Item;
import ShopProject.myShopProject.Repository.ItemRepository;
import ShopProject.myShopProject.Repository.ItemRepositorySpringJpa;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Autowired
    ItemRepositorySpringJpa itemRepository;
    @Autowired
    ItemService itemService;

    @Test()
    public void saveTest() throws Exception {
        List<Item> items = itemService.findItems();
        Item item = new Item();
        item.setName("ItemA");
        itemRepository.save(item);
        for (Item i : items) {
            System.out.println(i.getName() + "  "+ i.getPrice());

        }
        assertThat(item).isEqualTo(itemService.findOne(item.getId()));
    }


    @Test()
    public void findItemsByPrice() {
        Item item = new Item();
        item.setPrice(1000);
        itemRepository.save(item);

        Item item1 = new Item();
        item1.setPrice(2000);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setPrice(3000);
        itemRepository.save(item2);

        Item item3 = new Item();
        item3.setPrice(4000);
        itemRepository.save(item3);

        Item item4 = new Item();
        item4.setPrice(5000);
        itemRepository.save(item4);

        List<Item> findItems = itemService.findItemsByPrice();

        for (Item i : findItems) {
            System.out.println(i.getName() + "  "+ i.getPrice());
        }


    }
}

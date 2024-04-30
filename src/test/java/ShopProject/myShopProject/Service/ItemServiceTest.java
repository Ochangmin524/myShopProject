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
            System.out.println(i.getName() + "  " + i.getPrice());

        }
        assertThat(item).isEqualTo(itemService.findOne(item.getId()));
    }



    }


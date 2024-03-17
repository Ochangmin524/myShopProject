package ShopProject.myShopProject.Service;


import ShopProject.myShopProject.Domain.Item.Item;
import ShopProject.myShopProject.Repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemService itemService;

    @Test()
    @Rollback(value = false)
    public void saveTest() throws Exception {
        Item item = new Item();
        item.setName("ItemA");
        itemRepository.save(item);
        assertThat(item).isEqualTo(itemService.findOne(item.getId()));
    }
}

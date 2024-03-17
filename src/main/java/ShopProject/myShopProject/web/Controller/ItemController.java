package ShopProject.myShopProject.web.Controller;

import ShopProject.myShopProject.Domain.Item.*;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Service.ItemService;
import ShopProject.myShopProject.Service.MemberService;
import ShopProject.myShopProject.web.Form.ItemForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping(value = "/items/new")
    public String createForm(Model model) {
        ItemForm itemForm = new ItemForm();
        itemForm.setCategoriesMap(categoryInit());
        model.addAttribute("form", itemForm);
        return "items/createItemForm";
    }


    //지금은 book이지만 item으로 바꿀 필요 있으며, 로직 수정해야함
    @PostMapping(value = "/items/new")
    public String create(ItemForm form) {
        createAndJoin(form);
        return "redirect:/items";
    }

    private void createAndJoin(ItemForm form) {
        String category = form.getCategory();
        if(category.equals("Book")){
            //도서 객체 생성
            Book book = new Book();
            book.setName(form.getName());
            book.setPrice(form.getPrice());
            book.setStockQuantity(form.getStockQuantity());
            itemService.saveItem(book);
        }
        if(category.equals("Album")){
            Album album = new Album();
            album.setName(form.getName());
            album.setPrice(form.getPrice());
            album.setStockQuantity(form.getStockQuantity());
            itemService.saveItem(album);
        }
        if(category.equals("Movie")){
            Movie movie = new Movie();
            movie.setName(form.getName());
            movie.setPrice(form.getPrice());
            movie.setStockQuantity(form.getStockQuantity());
            itemService.saveItem(movie);
        }
    }

    private void extracted(Long itemId, List<Category> categories) {
        Book book = new Book();
        book.setId(itemId);
        Category Book = new Category();
        Book.setName("Book");
        categories.add(Book);
        itemService.saveItem(book);
    }


    //아이템 상세 정보
    @GetMapping("item")
    public String itemDetail(@RequestParam("itemId") Long itemId,
                             @RequestParam("memberId") Long memberId,
    Model model) {


        Item item = itemService.findOne(itemId);
        Member member = memberService.findOne(memberId);
        model.addAttribute("item", item);
        model.addAttribute("member", member);
        return "items/item";

    }
    //상품 목록
    //여기에 맴버 id가 pathvariable로 들어온다.
    @GetMapping(value = "/items")
    public String list(@RequestParam("memberId") Long memberId, Model model) {

        List<Item> items = itemService.findItems();
        //모델에 멤버 id 도 넣기
        model.addAttribute("memberId", memberId);
        model.addAttribute("items", items);
        return "items/itemList";
    }



    // 상품 수정 폼 접근
    @GetMapping(value = "items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        ItemForm form = new ItemForm();
        Book item = (Book) itemService.findOne(itemId);
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    //날라오는 bookform은 준영속객체체
   @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") ItemForm form, @PathVariable("itemId") Long itemId) {
       itemService.updateItem(itemId, form.getName(),form.getPrice(), form.getStockQuantity());
       return "redirect:/items";
    }


    @ModelAttribute("categoriesMap")
    public static Map<String, String> categoryInit() {
        Map<String, String> categoriesMap = new LinkedHashMap<>();
        categoriesMap.put("Book", "도서");
        categoriesMap.put("Album", "앨범");
        categoriesMap.put("Movie", "영화");
        return categoriesMap;
    }

}

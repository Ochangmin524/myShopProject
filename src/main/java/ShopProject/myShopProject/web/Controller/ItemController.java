package ShopProject.myShopProject.web.Controller;

import ShopProject.myShopProject.Domain.Item.*;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Repository.ItemRepositorySpringJpa;
import ShopProject.myShopProject.Service.EmbeddingService;
import ShopProject.myShopProject.Service.ItemService;
import ShopProject.myShopProject.Service.MemberService;
import ShopProject.myShopProject.web.Form.ItemForm;
import ShopProject.myShopProject.web.Form.editItemForm;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    private final ItemRepositorySpringJpa jpaItemRepository;
    private final EmbeddingService embeddingService;
    @GetMapping(value = "/items/new")
    public String createForm(Model model) {
        ItemForm itemForm = new ItemForm();
        itemForm.setCategoriesMap(categoryInit());
        model.addAttribute("itemForm", itemForm);
        return "items/createItemForm";
    }

    @PostMapping(value = "/items/new")
    public String create(@Valid ItemForm form, BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            return "items/createItemForm";
        }
        createAndJoin(form);
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/admin/adminItemList";
    }

    @GetMapping(value = "items/adminItemList")
    private String adminItemList(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/admin/adminItemList";
    }


    private void createAndJoin(ItemForm form) throws IOException {
        String category = form.getCategory();
        if (category.equals("Book")) {
            //상품명의 임베딩 값 생성



            //도서 객체 생성
            log.info("도서 객체 생성");
            Book book = new Book();
            book.setName(form.getName());
            String embeddingScore = embeddingService.getEmbeddingScore(form.getName());
            book.setEmbeddingScore(embeddingScore);
            book.setPrice(form.getPrice());
            book.setAuthor(form.getAuthor());
            book.setIsbn(form.getIsbn());
            book.setStockQuantity(form.getStockQuantity());
            book.setCategory("Book");
            itemService.saveItem(book);
        }
        if (category.equals("Album")) {
            log.info("앨범 객체 생성");

            Album album = new Album();
            album.setName(form.getName());
            album.setPrice(form.getPrice());
            album.setEtc(form.getEtc());
            String embeddingScore = embeddingService.getEmbeddingScore(form.getName());
            album.setEmbeddingScore(embeddingScore);
            album.setArtist(form.getArtist());
            album.setStockQuantity(form.getStockQuantity());
            album.setCategory("Album");

            itemService.saveItem(album);
        }
        if (category.equals("Movie")) {
            log.info("영화 객체 생성");
            Movie movie = new Movie();
            movie.setName(form.getName());
            String embeddingScore = embeddingService.getEmbeddingScore(form.getName());
            movie.setEmbeddingScore(embeddingScore);
            movie.setDirector(form.getDirector());
            movie.setActor(form.getActor());
            movie.setPrice(form.getPrice());
            movie.setStockQuantity(form.getStockQuantity());
            movie.setCategory("Movie");
            itemService.saveItem(movie);
        }
    }


    //좋아요 버튼 기능

    //멤버 id, 아이템 id, -> 좋아요 기능 수행 후 여부 추가 + itemgetmmapin으로 redirect
    @PostMapping("item/like")
    public String like(@SessionAttribute(name = "loginMember") Member member,
                       @RequestParam("itemId") Long itemId, Model model,
                       RedirectAttributes re) {
        log.info("like post 시작");
        Item item = itemService.findOne(itemId);


        memberService.likes(member, item);
        log.info("likes 메소드 완료");
        re.addAttribute("itemId", item.getId());
        return "redirect:/item";
    }


    //아이템 상세 정보
    @GetMapping("item")
    public String itemDetail(@RequestParam("itemId") Long itemId,
                             @SessionAttribute(name = "loginMember") Member loginMember,
                             Model model) {
        log.info("item 호출");
        Item item = itemService.findOne(itemId);
        log.info(loginMember.getLoginId() + " " + loginMember.getName());
        // 좋아요 여부 추가
        model.addAttribute("isLiked", memberService.isliked(loginMember, item));
        model.addAttribute("item", item);
        model.addAttribute("member", loginMember);


        if (item.getCategory().equals("Book")) {
            return "items/item/book";
        }
        if (item.getCategory().equals("Album")) {
            return "items/item/album";
        }
        if (item.getCategory().equals("Movie")) {
            return "items/item/movie";
        }
        return "items/item";
    }



    @GetMapping("/admin/item")
    public String AdminItemDetail(@RequestParam("itemId") Long itemId, Model model) {
        Item item = itemService.findOne(itemId);
        model.addAttribute("item", item);
        return "items/admin/adminItem";
    }

    //상품 목록
    @GetMapping(value = "/items")
    public String list(@SessionAttribute(name = "loginMember") Member loginMember,
                       Model model,
                       @RequestParam(value = "sortBy", required = false) String sortBy,
                       @RequestParam(value = "page", required = false) String pageNum,
                       RedirectAttributes redirectAttributes,
                       Pageable pageable) {
        log.info("sortBy = {}",sortBy);
        if (sortBy == null || sortBy.equals("")) {
            Page<Item> page = jpaItemRepository.findAll(pageable);
            List<Item> items = page.getContent();

            int totalPages = page.getTotalPages();
            model.addAttribute("totalPages", totalPages);

            model.addAttribute("page", 1);

            model.addAttribute("items", items);
            model.addAttribute("memberId", loginMember.getId());
            return "items/itemList";
        } else {

            ArrayList sort = new ArrayList();
            sort.add(sortBy);
            sort.add("id");
            sort.add("desc");

            redirectAttributes.addAttribute("page", pageNum);
            redirectAttributes.addAttribute("sortBy", sortBy);

            redirectAttributes.addAttribute("sort", sort);
            return "redirect:/sortedItems";
        }

    }

    @GetMapping(value = "/sortedItems")
    public String getItemList(@SessionAttribute(name = "loginMember") Member loginMember,
                              @RequestParam(value = "sortBy", required = false) String sortBy,
                              Model model,
                              Pageable pageable)
     {
         Page<Item> page = jpaItemRepository.findAll(pageable);
         List<Item> items = page.getContent();


         int totalPages = page.getTotalPages();
         model.addAttribute("sortBy",sortBy);
         model.addAttribute("totalPages", totalPages);
         model.addAttribute("items", items);
         model.addAttribute("memberId", loginMember.getId());
        return "items/itemList";

     }


    // 상품 수정 폼 접근
    @GetMapping(value = "/items/{itemId}/edit")
    public String updateItemForm(@RequestParam("itemId") Long itemId, Model model) {
        editItemForm form = new editItemForm();

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




    //날라오는 bookform은 준영속객체
    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@Valid @ModelAttribute("form") editItemForm form, BindingResult bindingResult,
                             @RequestParam("itemId") Long itemId) throws IOException {

        if (bindingResult.hasErrors()) {
            log.info("문제 발생");
            return "items/updateItemForm";
        }
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items/adminItemList";
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

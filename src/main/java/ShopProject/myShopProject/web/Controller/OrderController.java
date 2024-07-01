package ShopProject.myShopProject.web.Controller;

import ShopProject.myShopProject.Domain.Item.Item;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Domain.Order.Order;
import ShopProject.myShopProject.Domain.Order.OrderStatus;
import ShopProject.myShopProject.Domain.OrderSearch;
import ShopProject.myShopProject.Repository.OrderRepository;
import ShopProject.myShopProject.Service.ItemService;
import ShopProject.myShopProject.Service.MemberService;
import ShopProject.myShopProject.Service.OrderService;
import ShopProject.myShopProject.web.Form.OrderForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static ShopProject.myShopProject.Domain.Order.OrderStatus.ORDER;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping(value = "/order")
    public String createForm(@SessionAttribute(name = "loginMember") Member loginMember,
            @RequestParam("itemId") Long itemId,
                             Model model) {
        Item item = itemService.findOne(itemId);
        OrderForm orderForm = new OrderForm();
        model.addAttribute("orderForm", orderForm);
        model.addAttribute("member", loginMember);
        model.addAttribute("item", item);
        return "order/orderForm";
    }


    @GetMapping(value = "/admin/order")
    public String getAdminOrderForm(Model model){
        List<Member> members = memberService.findAll();
        List<Item> items = itemService.findItems();
        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/adminOrderForm";
    }

    @PostMapping(value = "/order")
    public String order(@ModelAttribute @Valid OrderForm orderForm,
                        BindingResult bindingResult,
                        RedirectAttributes re,
                        Model model) {
        Long memberId = orderForm.getMemberId();
        Long itemId = orderForm.getItemId();
        Integer count = orderForm.getCount();
        log.info("count ="+ count);
        Item item = itemService.findOne(itemId);

        if (bindingResult.hasErrors()) {
            Member member = memberService.findOne(memberId);
            model.addAttribute("member", member);
            model.addAttribute("item", item);
            return "order/orderForm";
        }
        if (count > item.getStockQuantity()) {
            bindingResult.reject("overStock","주문수량이 재고보다 많습니다.");
            Member member = memberService.findOne(memberId);
            model.addAttribute("member", member);
            model.addAttribute("item", item);
            return "order/orderForm";
        }

        orderService.order(memberId, itemId, count);

        String memberName = memberService.findOne(memberId).getName();


        re.addAttribute("memberId", memberId);
        re.addAttribute("memberName",memberName);

        return "redirect:/orders";
    }

    @GetMapping(value = "/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch,
                            @RequestParam(value = "memberName" ,required = false) String memberName,
                            Model model) {
        log.info("이름 ==="+memberName);

        //처음 요청
        if (orderSearch == null) {
            //주문한 맴버 정보로 orderSearch 생성
            List<Order> orders = orderService.getOrdersByNameWithStatus(memberName, ORDER);
            model.addAttribute("orders", orders);
            return "order/orderList";
        }

        //검색 요청이 들어왔을 경우
        List<Order> orders = this.orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);


        return "order/orderList";
    }



    @PostMapping(value = "/orders/{orderId}/cancel")
    public String cancel(@PathVariable("orderId") Long orderId,
                         RedirectAttributes re) {
        Order order = orderRepository.findOne(orderId);
        Member member = order.getMember();

        String memberName = member.getName();
        Long memberId = member.getId();
        re.addAttribute("memberName", memberName);
        re.addAttribute("memberId", memberId);
        orderService.cancel(orderId);

        return "redirect:/orders";
    }
}

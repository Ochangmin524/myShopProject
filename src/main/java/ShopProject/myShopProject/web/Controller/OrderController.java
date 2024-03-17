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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String createForm(@RequestParam("itemId")Long itemId,
            @RequestParam("memberId") Long memberId,  Model model) {
        Member member = memberService.findOne(memberId);
        Item item = itemService.findOne(itemId);
        model.addAttribute("member", member);
        model.addAttribute("item", item);
        return "order/orderForm";
    }

    @PostMapping(value = "/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count,
                        RedirectAttributes re,
                        Model model) {
        orderService.order(memberId, itemId, count);
        String memberName = memberService.findOne(memberId).getName();

        re.addAttribute("memberName",memberName);
        log.info("이름 dddddddd==="+memberName);

        return "redirect:/orders";
    }

    @GetMapping(value = "/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch,
                            @RequestParam(value = "memberName" ,required = false) String memberName, Model model) {
        log.info("이름 ==="+memberName);

        //처음 요청
        if (orderSearch == null) {
            //주문한 맴버 정보로 orderSearch 생성
            OrderSearch preOrderSearch = new OrderSearch();
            preOrderSearch.setMemberName(memberName);
            preOrderSearch.setOrderStatus(ORDER);

            //맴버의 주문 내역 가져와 보내기
            List<Order> orders = this.orderService.findOrders(preOrderSearch);
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
        String memberName = order.getMember().getName();
        re.addAttribute("memberName", memberName);
        orderService.cancel(orderId);

        return "redirect:/orders";
    }
}

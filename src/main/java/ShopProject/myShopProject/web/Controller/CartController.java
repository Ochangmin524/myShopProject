package ShopProject.myShopProject.web.Controller;

import ShopProject.myShopProject.Domain.Cart;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Exception.NotEnoughStockException;
import ShopProject.myShopProject.Service.CartService;
import ShopProject.myShopProject.Service.ItemService;
import ShopProject.myShopProject.Service.OrderService;
import ShopProject.myShopProject.web.Form.CartDTO;
import ShopProject.myShopProject.Domain.CartForm;
import ShopProject.myShopProject.web.Form.OrderForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {
    private final OrderService orderService;
    private final ItemService itemService;
    private final CartService cartService;

    //장바구니 불러오기, 멤버의 cart 리스트 객체를 불러온다. cart 리스트에는 ORderForm 들이 들어 있다.
    //장바구니에 cart 엔티티를 보낸다. -> 장바구니에서 orderitem 들을 출력및 수정하고 구매한다.
    @GetMapping("/cart")
    public String getCart(@SessionAttribute(name = "loginMember") Member loginMember,
                          Model model) {
        Cart cart = cartService.findCartByMember(loginMember);
        model.addAttribute("cart", cart);
        return "Order/cartList";
    }

    //장바구니에 아이템 담기. OrderForm이  파라미터로 들어온다.
    //OrderForm을 CarForm으로 변환 후 장바구니로 리다이렉트
    @PostMapping("/addCart")
    public String addCart(@SessionAttribute(name = "loginMember") Member loginMember,
                          @ModelAttribute @Valid OrderForm orderForm
                          ) {
        CartForm cartForm = cartService.orderToCart(orderForm);

        Cart cart = cartService.findCartByMember(loginMember);
        cartService.addCart(cart, cartForm);


        return "redirect:/cart"; //장바구니로 리다이렉트


    }

    //장바구니에서 상품 삭제 발생
    //수정된 cart를 다시 보낸다.
    @PostMapping("/deleteCartItem/{formId}")
    public String deleteCart(@SessionAttribute(name = "loginMember") Member loginMember,
                             @PathVariable("formId") Long formId){
        log.info("폼 아이디 = : {}", formId);

        cartService.deleteCart(loginMember, formId);
        return "redirect:/cart"; //장바구니로 리다이렉트
    }

    //장바구니 수정 메소드 쇼핑 계속하기 할 때 변경사항을 수정한다
    //장바구니에서 상품 구매했을 때 파라미터로 itemid, count가온다.
    //modelattribute로 채워넣는다.
    //따라서 cartDTO를 받는다.
    @PostMapping("/updateCart")
    public String buyCart(@SessionAttribute(name = "loginMember") Member loginMember,
                          @ModelAttribute CartDTO cartDTO) {
        //카트에서 orderform을 꺼낸다.
        List<OrderForm> carts = cartDTO.getForms();
        //하나하나 수정하기
        if(carts != null){
            for (OrderForm cartForm : carts) {
                Cart cart = cartService.findCartByMember(loginMember);
                cartService.updateCart(cart, cartDTO.getCartFormId(), cartForm.getCount());

            }
        }

        //수정 완료 후 아이템 리스트로 간다.
        return "redirect:/items";
    }

    //장바구니에서 상품 구매했을 때 파라미터로 itemid, count가온다.
    //modelattribute로 채워넣는다.
    //따라서 cartDTO를 받는다.
    @PostMapping("/buyCart")
    public String buyCart(@SessionAttribute(name = "loginMember") Member loginMember,
                          @ModelAttribute CartDTO cartDTO,
                          BindingResult bindingResult,
                          Model model) {
        //카트에서 orderform을 꺼낸다.
        List<OrderForm> carts = cartDTO.getForms();
        //하나하나 결재 진행시키기

        try {
            orderService.deleteCarts(carts, loginMember.getId());
        } catch (NotEnoughStockException e) {
            bindingResult.reject("overstock","주문 수량이 재고보다 많습니다.");
            return "redirect:/cart";
        }


        model.addAttribute("memberName", loginMember.getName());
        //결재 다 하면 결제 리스트 페이지로 이동
        return "redirect:/orders";
    }
}
// 장바니 엔티티 생성 필요 및, orderform과 연결필요
// 장바구니 리포지토리 생성 필요.. -> 빠르게 jpa data 사용하기

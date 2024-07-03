package ShopProject.myShopProject.Service;


import ShopProject.myShopProject.Domain.Cart;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Repository.CartRepositoryJPA;
import ShopProject.myShopProject.Repository.ItemRepository;
import ShopProject.myShopProject.Domain.CartForm;
import ShopProject.myShopProject.web.Form.OrderForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {
    private final CartRepositoryJPA cartRepositoryJPA;
    private final ItemRepository itemRepository;

    @Transactional
    public Cart createCart() {
        Cart cart = new Cart();
        Cart savedCart = cartRepositoryJPA.save(cart);
        return savedCart;
    }

    @Transactional
    public Cart findCartByMember(Member member) {
        return cartRepositoryJPA.findByMember(member);

    }


    @Transactional
    public void addCart(Cart cart, CartForm cartForm) {

        cart.getCarts().add(cartForm);
        cartForm.setCart(cart);
        cartRepositoryJPA.save(cart);
    }

    @Transactional
    public void updateCart(Cart cart,Long cartFormId, Integer count) {
        List<CartForm> carts = cart.getCarts();
        for (CartForm cartForm : carts) {
            if (cartForm.getCartFormId().equals(cartFormId)) {
                cartForm.setCount(count);
                break;
            }
        }
    }


    @Transactional
    public void deleteCart(Member member, Long formId) {
        log.info("삭제 시작");
        Cart cart = cartRepositoryJPA.findByMember(member);
        List<CartForm> carts = cart.getCarts();

        log.info("삭제 전 크기 = {}", carts.size());
        for (CartForm cartForm : carts) {
            if (cartForm.getCartFormId().equals(formId)) {
                log.info("아이디는 : {}", cartForm.getCartFormId());
                carts.remove(cartForm);
                break;
            }
        }


        log.info("삭제 후 크기 = {}", carts.size());


    }

    @Transactional
    public CartForm orderToCart(OrderForm orderForm) {
        CartForm cartForm = new CartForm();
        cartForm.setCount(orderForm.getCount());
        cartForm.setMemberId(orderForm.getMemberId());
        cartForm.setItem(itemRepository.findOne(orderForm.getItemId()));
        return cartForm;
    }

}

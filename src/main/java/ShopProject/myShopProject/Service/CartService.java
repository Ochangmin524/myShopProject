package ShopProject.myShopProject.Service;


import ShopProject.myShopProject.Domain.Cart;
import ShopProject.myShopProject.Domain.Member;
import ShopProject.myShopProject.Repository.CartRepositoryJPA;
import ShopProject.myShopProject.Repository.ItemRepository;
import ShopProject.myShopProject.web.Form.CartForm;
import ShopProject.myShopProject.web.Form.OrderForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public void deleteCart(Cart cart, CartForm cartForm) {
        List<CartForm> carts = cart.getCarts();

        carts.remove(cartForm);

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

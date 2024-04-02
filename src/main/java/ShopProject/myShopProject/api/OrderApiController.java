package ShopProject.myShopProject.api;

import ShopProject.myShopProject.Domain.Address;
import ShopProject.myShopProject.Domain.Order.Order;
import ShopProject.myShopProject.Domain.Order.OrderItem;
import ShopProject.myShopProject.Domain.Order.OrderStatus;
import ShopProject.myShopProject.Domain.OrderSearch;
import ShopProject.myShopProject.Repository.OrderRepository;
import ShopProject.myShopProject.api.dto.OrderDto;
import ShopProject.myShopProject.api.dto.Result;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository ;

    //주문, 회원, 배송 정보만 전달
    @GetMapping("/api/orders")
    public Result getOrders(){
        List<Order> orderList = orderRepository.findAllWithMemberDelivery();
        List<OrderDto> result = orderList.stream().
                map(o -> new OrderDto(o)).
                collect(toList());
        return new Result(result);
    }




    //주문, 회원, 배송정보, 주문 상품, 상품 정보 전달
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }



    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,
                limit);
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(toList());
        return result;
    }



}


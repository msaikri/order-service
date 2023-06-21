package com.codemindsinfo.orderservice.service;

import com.codemindsinfo.orderservice.dto.OrderLineItemsDto;
import com.codemindsinfo.orderservice.dto.OrderRequest;
import com.codemindsinfo.orderservice.model.Order;
import com.codemindsinfo.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(getOrderLineItemsList(orderRequest))
                .build();

        orderRepository.save(order);
    }

    private List<OrderLineItemsDto> getOrderLineItemsList(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderReq -> mapToDto(orderReq))
                .toList();
    }

    private OrderLineItemsDto mapToDto(OrderLineItemsDto orderReq) {
        return OrderLineItemsDto.builder()
                .skuCode(orderReq.getSkuCode())
                .price(orderReq.getPrice())
                .quantity(orderReq.getQuantity())
                .build();
    }
}

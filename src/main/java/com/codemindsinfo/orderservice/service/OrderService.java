package com.codemindsinfo.orderservice.service;

import com.codemindsinfo.orderservice.model.InventoryResponse;
import com.codemindsinfo.orderservice.config.WebConfig;
import com.codemindsinfo.orderservice.dto.OrderLineItemsDto;
import com.codemindsinfo.orderservice.dto.OrderRequest;
import com.codemindsinfo.orderservice.exception.OutOfStockException;
import com.codemindsinfo.orderservice.model.Order;
import com.codemindsinfo.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebConfig webconfig;

    public void placeOrder(OrderRequest orderRequest) {
        try {
            Order order = Order.builder()
                    .orderDate(new Date().toInstant().toString())
                    .orderNumber(UUID.randomUUID().toString())
                    .orderLineItemsList(getOrderLineItemsList(orderRequest))
                    .build();

            List<String> skuCodes = order.getOrderLineItemsList().stream()
                    .map(product -> product.getSkuCode())
                    .toList();

            //Before placing the Order, we need to check if the product is available/in Stock
            //So, we will do a Synchronous Call to inventory service to fetch the data.

            InventoryResponse[] inventoryResponsesArray = webconfig
                    .webClient().build().get()
                    .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean isInStock = Arrays.stream(Objects.requireNonNull(inventoryResponsesArray)).allMatch(InventoryResponse::isInStock);

            if (isInStock) {//available in Stock
                orderRepository.save(order);
            } else {//Not available in Stock
                throw new OutOfStockException("Selected Product is Out of Stock, Please try again later!!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

package com.codemindsinfo.orderservice.model;

import com.codemindsinfo.orderservice.dto.OrderLineItemsDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "order")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    private String id;
    private String orderNumber;
    private List<OrderLineItemsDto> orderLineItemsList;

}

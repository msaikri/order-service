package com.codemindsinfo.orderservice.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "t_order_line_items")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderLineItemsDto {

    private String skuCode;
    private BigDecimal price;
    private Integer quantity;


}

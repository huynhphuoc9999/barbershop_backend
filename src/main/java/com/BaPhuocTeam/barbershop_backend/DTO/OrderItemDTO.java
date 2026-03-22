package com.BaPhuocTeam.barbershop_backend.DTO;

import com.BaPhuocTeam.barbershop_backend.Entity.Orders;
import com.BaPhuocTeam.barbershop_backend.Enum.OrderType;
import lombok.Data;

@Data
public class OrderItemDTO {

    private Orders order;

    private OrderType orderType;
    private String itemName;

    private Double price;
    private Integer quantity;
}

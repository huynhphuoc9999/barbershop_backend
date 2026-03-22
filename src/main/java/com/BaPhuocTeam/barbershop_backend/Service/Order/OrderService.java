package com.BaPhuocTeam.barbershop_backend.Service.Order;

import com.BaPhuocTeam.barbershop_backend.DTO.OrderDTO;
import com.BaPhuocTeam.barbershop_backend.Response.APIResponse;
import org.apache.coyote.BadRequestException;

public interface OrderService {
    APIResponse addOrder(OrderDTO orderDTO) throws BadRequestException;
    APIResponse getOrderByPage(int page, int size);
    APIResponse getOrderByCustomerId(Long customerId, int page, int size);
    APIResponse getOrderByShopId(Long shopId, int page, int size);
    APIResponse searchOrder(String keyword,int page, int size);
}

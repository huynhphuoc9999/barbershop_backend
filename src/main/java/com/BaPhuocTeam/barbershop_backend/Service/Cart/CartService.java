package com.BaPhuocTeam.barbershop_backend.Service.Cart;

import com.BaPhuocTeam.barbershop_backend.Entity.Products;
import com.BaPhuocTeam.barbershop_backend.Response.APIResponse;
import org.springframework.security.core.Authentication;

public interface CartService {
    APIResponse addCart(Long userId);
    APIResponse addItem(Authentication authentication,Long productId, Integer quantity);
    APIResponse getCartItemByCart(Long cartId);
    APIResponse updateItem(Long id,Integer quantity);
    APIResponse deleteItem(Long id);
    APIResponse deleteCart(Long id);
}

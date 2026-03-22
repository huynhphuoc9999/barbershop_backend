package com.BaPhuocTeam.barbershop_backend.Service.Product;

import com.BaPhuocTeam.barbershop_backend.DTO.ProductsDTO;
import com.BaPhuocTeam.barbershop_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    APIResponse addProduct(ProductsDTO productsDTO,MultipartFile img) throws IOException;
    APIResponse getProductByPage(int page, int size);
    APIResponse getProductById(Long id);
    APIResponse getProductByShopId(Long shopId);
    APIResponse updateProduct(Long id, ProductsDTO productsDTO, MultipartFile img) throws IOException;
    APIResponse deleteProduct(Long id);
    APIResponse restoreProduct(Long id);
}

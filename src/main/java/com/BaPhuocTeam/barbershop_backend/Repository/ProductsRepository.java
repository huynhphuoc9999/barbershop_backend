package com.BaPhuocTeam.barbershop_backend.Repository;

import com.BaPhuocTeam.barbershop_backend.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products,Long> {
    List<Products> findByShopId(Long shopId);
}

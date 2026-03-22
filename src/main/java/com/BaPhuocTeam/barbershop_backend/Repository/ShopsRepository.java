package com.BaPhuocTeam.barbershop_backend.Repository;

import com.BaPhuocTeam.barbershop_backend.Entity.Shops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopsRepository extends JpaRepository<Shops,Long>, JpaSpecificationExecutor<Shops> {
    List<Shops> findByOwnerId(Long ownerId);
}

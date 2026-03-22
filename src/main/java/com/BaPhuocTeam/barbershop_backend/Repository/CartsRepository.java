package com.BaPhuocTeam.barbershop_backend.Repository;

import com.BaPhuocTeam.barbershop_backend.Entity.Carts;
import com.BaPhuocTeam.barbershop_backend.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartsRepository extends JpaRepository<Carts,Long> {
    Carts findByUser(Users user);
}

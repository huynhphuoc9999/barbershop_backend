package com.BaPhuocTeam.barbershop_backend.Repository;

import com.BaPhuocTeam.barbershop_backend.Entity.RefreshTokens;
import com.BaPhuocTeam.barbershop_backend.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokensRepository extends JpaRepository<RefreshTokens,Long> {
    RefreshTokens findByUser(Users user);
    RefreshTokens findByToken(String token);
}

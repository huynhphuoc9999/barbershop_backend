package com.BaPhuocTeam.barbershop_backend.Repository;

import com.BaPhuocTeam.barbershop_backend.Entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments,Long> {
}

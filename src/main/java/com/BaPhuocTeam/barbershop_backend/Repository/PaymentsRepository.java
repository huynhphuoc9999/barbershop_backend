package com.BaPhuocTeam.barbershop_backend.Repository;

import com.BaPhuocTeam.barbershop_backend.Entity.Appointments;
import com.BaPhuocTeam.barbershop_backend.Entity.Orders;
import com.BaPhuocTeam.barbershop_backend.Entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments,Long> {
    Optional<Payments> findByAppointments(Appointments appointments);
    Optional<Payments> findByOrders(Orders orders);
}

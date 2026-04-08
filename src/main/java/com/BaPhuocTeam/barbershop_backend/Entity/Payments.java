package com.BaPhuocTeam.barbershop_backend.Entity;

import com.BaPhuocTeam.barbershop_backend.Enum.PaymentMethod;
import com.BaPhuocTeam.barbershop_backend.Enum.PaymentStatus;
import com.BaPhuocTeam.barbershop_backend.Enum.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Users customer;

    @ManyToOne()
    @JoinColumn(name = "appointment_id")
    private Appointments appointments;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Orders orders;

    private Double amount;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private PaymentType paymentType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

package com.BaPhuocTeam.barbershop_backend.Entity;

import com.BaPhuocTeam.barbershop_backend.Enum.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private AppointmentStatus appointmentStatus;

    private Double price;

    private boolean paid;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Users customer;

    @ManyToOne
    @JoinColumn(name = "barber_id")
    private Users barber;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "appointment_service",
    joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Services> services;

    @ManyToOne()
    @JoinColumn(name = "shop_id")
    private Shops shop;

    @OneToOne(mappedBy = "appointment")
    private Feedbacks feedback;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

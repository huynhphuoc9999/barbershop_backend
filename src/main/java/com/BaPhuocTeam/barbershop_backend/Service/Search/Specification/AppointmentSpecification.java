package com.BaPhuocTeam.barbershop_backend.Service.Search.Specification;

import com.BaPhuocTeam.barbershop_backend.Entity.Appointments;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AppointmentSpecification {
    public static Specification<Appointments> searchByKeyword(String keyword) {
        return (root,query,criteriaBuilder) -> {
            if(keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.toString(root.get("price")),pattern)
            );
        };
    }
}

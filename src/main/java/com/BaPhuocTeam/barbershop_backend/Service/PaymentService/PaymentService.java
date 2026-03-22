package com.BaPhuocTeam.barbershop_backend.Service.PaymentService;

import com.BaPhuocTeam.barbershop_backend.DTO.PaymentDTO;
import com.BaPhuocTeam.barbershop_backend.Response.APIResponse;

public interface PaymentService {
    APIResponse addPayment(PaymentDTO paymentDTO);
    APIResponse getPaymentByPage(int page, int size);
    APIResponse getPaymentById(Long id);
}

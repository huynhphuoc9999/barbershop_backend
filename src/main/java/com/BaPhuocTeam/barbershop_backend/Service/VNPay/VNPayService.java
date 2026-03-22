package com.BaPhuocTeam.barbershop_backend.Service.VNPay;

import com.BaPhuocTeam.barbershop_backend.DTO.PaymentDTO;
import com.BaPhuocTeam.barbershop_backend.Response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface VNPayService {
    APIResponse createPayment(HttpServletRequest request, PaymentDTO paymentDTO) throws NoSuchAlgorithmException, InvalidKeyException;
    APIResponse executePayment(HttpServletRequest request);
}

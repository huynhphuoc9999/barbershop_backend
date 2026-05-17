package com.BaPhuocTeam.barbershop_backend.Controller;

import com.BaPhuocTeam.barbershop_backend.DTO.PaymentDTO;
import com.BaPhuocTeam.barbershop_backend.Response.APIResponse;
import com.BaPhuocTeam.barbershop_backend.Service.PaymentService.PaymentService;
import com.BaPhuocTeam.barbershop_backend.Service.VNPay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController()
@RequestMapping("/api/customer/payments")
public class PaymentsController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addPayment(@Valid @RequestBody PaymentDTO paymentDTO) throws Exception {
        return ResponseEntity.ok(paymentService.addPayment(paymentDTO));
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse> createPayment(HttpServletRequest request, @Valid @RequestBody PaymentDTO paymentDTO) throws Exception {
        APIResponse apiResponse = new APIResponse();

        if (paymentDTO.getUserId() == null) {
            apiResponse.setStatusCode(400L);
            apiResponse.setMessage("userId is required");
            apiResponse.setData(null);
            apiResponse.setTimestamp(LocalDateTime.now());
            return ResponseEntity.badRequest().body(apiResponse);
        }

        if (paymentDTO.getAmount() == null || paymentDTO.getAmount() <= 0) {
            apiResponse.setStatusCode(400L);
            apiResponse.setMessage("amount must be a positive number");
            apiResponse.setData(null);
            apiResponse.setTimestamp(LocalDateTime.now());
            return ResponseEntity.badRequest().body(apiResponse);
        }

        if ((paymentDTO.getOrderId() == null && paymentDTO.getAppointmentId() == null) ||
                (paymentDTO.getOrderId() != null && paymentDTO.getAppointmentId() != null)) {
            apiResponse.setStatusCode(400L);
            apiResponse.setMessage("Payment must belong to exactly one of orderId or appointmentId");
            apiResponse.setData(null);
            apiResponse.setTimestamp(LocalDateTime.now());
            return ResponseEntity.badRequest().body(apiResponse);
        }

        return ResponseEntity.ok(vnPayService.createPayment(request, paymentDTO));
    }

    @GetMapping("/execute/vnpay")
    public ResponseEntity<APIResponse> executePaymentVNPay(
            HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(vnPayService.executePayment(request));
    }
}

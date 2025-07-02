package org.umlpractice.backend_fooddeliverysystem.controller.api.payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.umlpractice.backend_fooddeliverysystem.pojo.DeliveryOrder;
import org.umlpractice.backend_fooddeliverysystem.pojo.Payment;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceDeliveryOrderService;
import org.umlpractice.backend_fooddeliverysystem.service.InterfacePaymentSimulationService;
import org.umlpractice.backend_fooddeliverysystem.util.Authenticator;
import org.umlpractice.backend_fooddeliverysystem.util.InterfaceAuthenticator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

/**
 * PaymentController 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/30 21:30
 */
@RestController
@RequestMapping("/api/payment")
@EnableScheduling
public class PaymentController {
    @Autowired
    private InterfacePaymentSimulationService paymentSimulationService;

    @Autowired
    private InterfaceDeliveryOrderService deliveryOrderService;

    @Autowired
    private InterfaceAuthenticator authenticator;

    @Scheduled(fixedRate = 5000)
    public void permitPayment()
    {
        System.out.println("Automatically permit payment.");
        List<Payment> payments = this.paymentSimulationService.getPaymentsByStatus("Waiting");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Payment payment : payments)
        {
            LocalDateTime specifiedTime = LocalDateTime.parse(payment.getStrCreatedTime(), formatter);
            if(ChronoUnit.SECONDS.between(specifiedTime, LocalDateTime.now()) > 10)
                this.paymentSimulationService.permitPayment(payment);
        }
    }

    @GetMapping("/create/{user_id}/{order_id}")
    public Object createPaymentForOrder(@PathVariable("user_id") String strUserId,
                                        @PathVariable("order_id") String strOrderId,
                                        Authentication authentication)
    {
        try {
            Integer userId = Integer.parseInt(strOrderId);
            authenticator.authUser(userId,authentication);
            DeliveryOrder order = this.deliveryOrderService.getOrderById(userId);
            Payment ret = this.paymentSimulationService.makePayment(order, "Waiting");
            return ResponseEntity.ok(ret);
        }
        catch(IllegalArgumentException e)
        {
            HashMap<String,Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

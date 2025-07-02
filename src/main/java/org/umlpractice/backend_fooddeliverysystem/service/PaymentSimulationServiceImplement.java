package org.umlpractice.backend_fooddeliverysystem.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.umlpractice.backend_fooddeliverysystem.DAO.InterfacePaymentDAO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DeliveryOrder;
import org.umlpractice.backend_fooddeliverysystem.pojo.Payment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * PaymentSimulationServiceImplement 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/30 21:38
 */
@Service
public class PaymentSimulationServiceImplement implements InterfacePaymentSimulationService{
    @Autowired
    private InterfacePaymentDAO interfacePaymentDAO;

    @Override
    public Payment makePayment(DeliveryOrder order,String paymentType) throws IllegalArgumentException
    {
        Payment payment = new Payment();
        payment.setdAmount(order.getdTotalPrice());
        payment.setStrPayment(paymentType);
        payment.setiRelatedOrderId(order.getiOrderId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);
        payment.setStrCreatedTime(now);
        payment.setStrPaymentStatus("Waiting");
        return this.interfacePaymentDAO.save(payment);
    }

    @Override
    public Payment permitPayment(Payment payment) throws IllegalArgumentException
    {
        if(!payment.getStrPaymentStatus().equals("Waiting"))
            throw new IllegalArgumentException("Payment status is wrong: " + payment.getStrPaymentStatus());
        payment.setStrPaymentStatus("Done");
        return this.interfacePaymentDAO.save(payment);
    }

    @Override
    public Payment getPaymentByOderId(Integer orderId) throws IllegalArgumentException {
        Optional<Payment> queryResult = this.interfacePaymentDAO.findById(orderId);
        if(!queryResult.isPresent())
            throw new IllegalArgumentException("No such order id: " + orderId);
        return queryResult.get();
    }

    @Override
    public Payment cancelPayment(Payment payment) throws IllegalArgumentException
    {
        payment.setStrPaymentStatus("Canceled");
        return this.interfacePaymentDAO.save(payment);
    }

    @Override
    public List<Payment> getPaymentsByStatus(String status) throws IllegalArgumentException
    {
        if(!status.equals("Waiting")&&!status.equals("Canceled")&&!status.equals("Done"))
            throw new IllegalArgumentException("Status is wrong: " + status);
        return this.interfacePaymentDAO.findByStatus(status);
    }
}

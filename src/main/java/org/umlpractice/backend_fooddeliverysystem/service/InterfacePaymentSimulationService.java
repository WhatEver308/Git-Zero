package org.umlpractice.backend_fooddeliverysystem.service;

import org.umlpractice.backend_fooddeliverysystem.pojo.DeliveryOrder;
import org.umlpractice.backend_fooddeliverysystem.pojo.Payment;

import java.util.List;

public interface InterfacePaymentSimulationService
{
    public Payment makePayment(DeliveryOrder deliveryOrder,String payment) throws IllegalArgumentException;
    public Payment getPaymentByOderId(Integer orderId) throws IllegalArgumentException;
    public Payment permitPayment(Payment payment) throws IllegalArgumentException;
    public Payment cancelPayment(Payment payment) throws IllegalArgumentException;
    public List<Payment> getPaymentsByStatus(String status) throws IllegalArgumentException;
}

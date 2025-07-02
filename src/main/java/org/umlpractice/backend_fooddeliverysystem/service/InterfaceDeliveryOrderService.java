package org.umlpractice.backend_fooddeliverysystem.service;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.DeliveryOrderDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.DeliveryOrderQueryResponseDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DeliveryOrder;


import java.util.List;


public interface InterfaceDeliveryOrderService {
    public DeliveryOrder createOrder(DeliveryOrderDTO deliveryOrderDTO);
    public DeliveryOrder updateOrderStatusbyMerchant(Integer merchantId,Integer orderId,String status);
    public DeliveryOrder updateOrderStatusbyUser(Integer userId,Integer orderId,String status);
    public DeliveryOrderQueryResponseDTO getOrderDTOById(Integer orderId);
    public List<DeliveryOrderQueryResponseDTO> getOrderDTOsByUserId(Integer userId);
    public List<DeliveryOrderQueryResponseDTO> getOrderDTOsByMerchantId(Integer merchantId);
    public DeliveryOrderQueryResponseDTO EntityToDTO(DeliveryOrder deliveryOrder);
    public DeliveryOrder getOrderById(Integer orderId);
}

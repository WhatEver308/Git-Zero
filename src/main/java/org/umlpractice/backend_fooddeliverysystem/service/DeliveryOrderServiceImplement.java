package org.umlpractice.backend_fooddeliverysystem.service;
import org.springframework.beans.BeanUtils;
import org.umlpractice.backend_fooddeliverysystem.DAO.InterfaceAddressDAO;
import org.umlpractice.backend_fooddeliverysystem.DAO.InterfaceDeliveryOrderDAO;
import org.umlpractice.backend_fooddeliverysystem.DAO.InterfaceOrderItemDAO;
import org.umlpractice.backend_fooddeliverysystem.DAO.UserDAO;
import org.umlpractice.backend_fooddeliverysystem.pojo.*;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.nio.channels.FileLockInterruptionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeliveryOrderServiceImplement implements InterfaceDeliveryOrderService {
    @Autowired
    private InterfaceAddressDAO addressDAO;
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private InterfaceDeliveryOrderDAO deliveryOrderDAO;

    @Autowired
    private InterfaceOrderItemDAO orderItemDAO;

    @Autowired
    private UserServiceImplement userServiceImplement;

    @Autowired
    private MerchantServiceImplement merchantServiceImplement;

    @Autowired
    private AddressServiceImplement addressServiceImplement;

    @Autowired
    private MenuItemServiceImplement menuItemServiceImplement;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeliveryOrder createOrder(DeliveryOrderDTO deliveryOrderDTO) {
        if(deliveryOrderDTO == null) {
            throw new IllegalArgumentException("DeliveryOrderDTO cannot be null");
        }

        if(!deliveryOrderDTO.isValidForCreate()) {
            throw new IllegalArgumentException("DeliveryOrderDTO is not valid. Please check the fields.");
        }
        DeliveryOrder newOrder = new DeliveryOrder();

        User user = null;
        Merchant merchant = null;

        // 设置用户
        try{
        user = userServiceImplement.getUserById(deliveryOrderDTO.getiUserId());
        newOrder.setUser(user);}
        catch (Exception e) {
            throw new IllegalArgumentException("User with ID " + deliveryOrderDTO.getiUserId() + " not found.");
        }

        // 设置地址
        if(deliveryOrderDTO.getAddress().getiAddressId() == null) { //如果地址id为空，则表明这是一个临时地址
            Address temporaryAddress = new Address();
            if(!deliveryOrderDTO.getAddress().isValidForOrderCreate())
                throw new IllegalArgumentException("Address attributes not completed.");
            BeanUtils.copyProperties(deliveryOrderDTO.getAddress(), temporaryAddress, "iAddressId", "bDefault");
            temporaryAddress.setUser(user);
            temporaryAddress.setbDefault(false); // 临时地址不设置为默认地址
            addressDAO.save(temporaryAddress); // 保存临时地址
            newOrder.setAddAddress(temporaryAddress);
        }
        else{
            Address address = addressServiceImplement.getAddressById(deliveryOrderDTO.getAddress().getiAddressId());
            newOrder.setAddAddress(address); // 设置已有地址
        }

        // 设置商家
        try {
            merchant = merchantServiceImplement.getMerchantByMerchantId(deliveryOrderDTO.getiMerchantId());
            newOrder.setMerchant(merchant);
        } catch (Exception e) {
            throw new IllegalArgumentException("Merchant with ID " + deliveryOrderDTO.getiMerchantId() + " not found.");
        }

        // 设置订单状态
        try{
            newOrder.setStrStatus("Pending");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + "unknown error", e);
        }

        // 设置创建时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);
        newOrder.setStrCreatedAt(now);

        // 设置配送费
        newOrder.setdDeliverFee(merchant.getdDeliveryFee()); // 设置配送费为商家的配送费



        List<OrderItem> orderItems = new ArrayList<>();
        Double totalPrice = 0.0;

        // 检查订单项列表是否为空或未初始化
        if(deliveryOrderDTO.getOrderItemList() == null || deliveryOrderDTO.getOrderItemList().isEmpty()) {
            throw new IllegalArgumentException("OrderItemList cannot be null or empty.");
        }

        // 遍历订单项列表，创建订单项并添加到订单中
        for(OrderItemDTO orderItemDTO: deliveryOrderDTO.getOrderItemList()) {
            MenuItem menuItem = menuItemServiceImplement.getMenuItemById(orderItemDTO.getiMenuItemId());

            if(menuItem.getbDirtyBit()==true){
                throw new IllegalArgumentException("MenuItem with ID " + orderItemDTO.getiMenuItemId() + " is dirty and cannot be ordered. Please update it first.");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem); // 设置菜单项
            Integer quantity = orderItemDTO.getiQuantity();
            orderItem.setiQuantity(quantity); // 设置数量
            Double priceAtOrder = menuItem.getdMenuItemPrice() * quantity; // 计算下单时的总价
            orderItem.setdPriceAtOrder(priceAtOrder); // 设置下单时的总价
            totalPrice +=priceAtOrder; // 累加到总价
            newOrder.addOrderItem(orderItem); // 将订单项添加到订单中
            // 这里不需要单独保存订单项，因为它们会在保存订单时自动保存
        }

        // 设置订单总价
        newOrder.setdTotalPrice(totalPrice + newOrder.getdDeliverFee()); // 设置订单总价为所有订单项的总价加上配送费

        // 保存订单到数据库
        deliveryOrderDAO.save(newOrder);

        return newOrder;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeliveryOrder updateOrderStatusbyMerchant(Integer merchantId,Integer orderId,String status){
        DeliveryOrder updatedOrder = getOrderById(orderId);

        if(merchantId == null){
            throw  new IllegalArgumentException("MerchantId cannot be null");
        }

        if(!merchantId.equals(updatedOrder.getMerchant().getiMerchantId())) {
            throw new IllegalArgumentException("MerchantId does not match the order's merchant.");
        }

        updatedOrder.setStrStatus(status); // 更新订单状态

        deliveryOrderDAO.save(updatedOrder); // 保存更新后的订单
        return updatedOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeliveryOrder updateOrderStatusbyUser(Integer userId,Integer orderId,String status){
        DeliveryOrder updatedOrder = getOrderById(orderId);

        if(userId == null){
            throw  new IllegalArgumentException("UserId cannot be null");
        }

        if(!userId.equals(updatedOrder.getUser().getiUserId())) {
            throw new IllegalArgumentException("UserId does not match the order's user.");
        }

        updatedOrder.setStrStatus(status); // 更新订单状态

        deliveryOrderDAO.save(updatedOrder); // 保存更新后的订单
        return updatedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryOrderQueryResponseDTO getOrderDTOById(Integer orderId) {
        Optional<DeliveryOrder> optionalOrder = deliveryOrderDAO.findById(orderId);
        if (optionalOrder.isPresent()) {
            return EntityToDTO(optionalOrder.get());
        } else {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryOrder getOrderById(Integer orderId) {
        Optional<DeliveryOrder> optionalOrder = deliveryOrderDAO.findById(orderId);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryOrderQueryResponseDTO> getOrderDTOsByUserId(Integer userId) {
        List<DeliveryOrder> orders = null;
        List<DeliveryOrderQueryResponseDTO> orderDTOs = new ArrayList<>();
        try {
            orders = deliveryOrderDAO.findOrdersByUserId(userId);
            for (DeliveryOrder order : orders) {
                orderDTOs.add(EntityToDTO(order));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("User with ID " + userId + " not found or has no orders.");
        }
        return orderDTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryOrderQueryResponseDTO> getOrderDTOsByMerchantId(Integer merchantId) {
        List<DeliveryOrder> orders = null;
        List<DeliveryOrderQueryResponseDTO> orderDTOs = new ArrayList<>();
        try {
            orders = deliveryOrderDAO.findOrdersByMerchantId(merchantId);
            for (DeliveryOrder order : orders) {
                orderDTOs.add(EntityToDTO(order));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Merchant with ID " + merchantId + " not found or has no orders.");
        }
        return orderDTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryOrderQueryResponseDTO EntityToDTO(DeliveryOrder deliveryOrder) {
        if (deliveryOrder == null) {
            throw new IllegalArgumentException("Delivery_Order cannot be null");
        }

        DeliveryOrderQueryResponseDTO dto = new DeliveryOrderQueryResponseDTO();

        // 订单主信息
        dto.setiOrderId(deliveryOrder.getiOrderId());
        dto.setiUserId(deliveryOrder.getUser() != null ? deliveryOrder.getUser().getiUserId() : null);
        dto.setiMerchantId(deliveryOrder.getMerchant() != null ? deliveryOrder.getMerchant().getiMerchantId() : null);
        dto.setdDeliverFee(deliveryOrder.getdDeliverFee());
        dto.setdTotalPrice(deliveryOrder.getdTotalPrice());
        dto.setStrCreatedAt(deliveryOrder.getStrCreatedAt());
        dto.setStrStatus(deliveryOrder.getStrStatus() != null ? deliveryOrder.getStrStatus().name() : null);

        // 地址信息
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(deliveryOrder.getAddAddress(), addressDTO);
        dto.setAddressDTO(addressDTO);

        // 订单项信息
        for(OrderItem orderItem : deliveryOrder.getOrderItems()) {
            MenuItemQueryResponseDTO menuItemQueryResponseDTO = new MenuItemQueryResponseDTO();
            BeanUtils.copyProperties(orderItem.getMenuItem(), menuItemQueryResponseDTO, "striMenuItemCategory");
            dto.addOrderItemDTO(menuItemQueryResponseDTO, orderItem.getiQuantity());
        }

        return dto;
    }
}

package org.umlpractice.backend_fooddeliverysystem.controller.api.orders;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.umlpractice.backend_fooddeliverysystem.pojo.Address;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.DeliveryOrderDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.DeliveryOrderQueryResponseDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DeliveryOrder;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceDeliveryOrderService;
import org.umlpractice.backend_fooddeliverysystem.service.InterfacePaymentSimulationService;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceUserService;
import org.umlpractice.backend_fooddeliverysystem.util.InterfaceAuthenticator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderController 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/30 01:11
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private InterfaceDeliveryOrderService orderService;

    @Autowired
    private InterfacePaymentSimulationService paymentSimulationService;

    @Autowired
    private InterfaceUserService userService;

    @Autowired
    private InterfaceAuthenticator authenticator;

    @PostMapping
    public Object submitOrder(@RequestBody DeliveryOrderDTO input,
                              Authentication authentication)
    {
        try {
            Integer userId = input.getiUserId();
            if(authenticator.authUser(userId,authentication))
            {
                DeliveryOrder order = orderService.createOrder(input);
                HashMap<String, Object> response = new HashMap<>();
                response.put("order_id", order.getiOrderId());
                return ResponseEntity.ok(response);
            }
            throw new IllegalArgumentException("Username:"+authentication.getName()+" not found");
        }
        catch(Exception e)
        {
            HashMap<String,String> error = new HashMap<>();
            error.put("error",e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{merchant_id}")
    public Object getMerchantOrders(@PathVariable("merchant_id") String strMerchantId,
                                    @RequestParam("status") String status,
                                    Authentication authentication)
    {
        try
        {
            Integer merchantId = Integer.parseInt(strMerchantId);
            authenticator.authUser(merchantId,authentication);
            List<DeliveryOrderQueryResponseDTO> orders = orderService.getOrderDTOsByMerchantId(merchantId);
            DeliveryOrder.StrStatus stat = DeliveryOrder.StrStatus.valueOf(status);
            ArrayList<DeliveryOrderQueryResponseDTO> response = new ArrayList<>();
            for(DeliveryOrderQueryResponseDTO order : orders)
            {
                if(status.equals(order.getStrStatus()))
                {
                    response.add(order);
                }
            }
            HashMap<String,Object> responseMap = new HashMap<>();
            responseMap.put("arrOrders",response);
            return ResponseEntity.ok(responseMap);
        }
        catch(IllegalArgumentException e)
        {
            HashMap<String,String> error = new HashMap<>();
            error.put("error",e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/payment/query/{merchant_id}/{order_id}")
    public Object queryMerchantDeliveryOrderPayment(@PathVariable("merchant_id")String strMerchantId,
                                                    @PathVariable("order_id")String strOrderId,
                                                    Authentication authentication)
    {
        try
        {
            Integer merchantId = Integer.parseInt(strMerchantId);
            authenticator.authMerchant(merchantId,authentication);
            Integer orderId = Integer.parseInt(strOrderId);
            return this.paymentSimulationService.getPaymentByOderId(orderId);
        }
        catch(IllegalArgumentException e)
        {
            HashMap<String,String> error = new HashMap<>();
            error.put("error",e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

}

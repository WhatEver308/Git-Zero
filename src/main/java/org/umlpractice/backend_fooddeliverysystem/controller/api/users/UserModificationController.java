package org.umlpractice.backend_fooddeliverysystem.controller.api.users;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.UserDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.UserModificationDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.umlpractice.backend_fooddeliverysystem.security.provider.DAOAuthenticationProviderForUsers;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceDeliveryOrderService;
import org.umlpractice.backend_fooddeliverysystem.service.UserServiceImplement;

import java.util.ArrayList;
import java.util.HashMap;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.OrderStatusUpdateBody;
import org.umlpractice.backend_fooddeliverysystem.util.Authenticator;
import org.umlpractice.backend_fooddeliverysystem.util.InterfaceAuthenticator;

/**
 * UserModificationController 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/29 23:31
 */
@Controller
@RequestMapping("/api/users")
public class UserModificationController {
    @Autowired
    private UserServiceImplement userService;
    @Autowired
    private InterfaceDeliveryOrderService deliveryOrderService;
    @Autowired
    private InterfaceAuthenticator authenticator;
    @Autowired
    private DAOAuthenticationProviderForUsers daoAuthenticationProviderForUsers;
    @PutMapping("/{user_id}/modify")
    public Object modifyUser(@PathVariable("user_id") String strUserId,
                             @RequestBody UserDTO userDTO,
                             Authentication authentication)
    {
        try
        {
            Integer userId = Integer.parseInt(strUserId);
            this.authenticator.authUser(userId,authentication);
            boolean needReturn = false;
            if(userDTO==null)
            {
                HashMap<String,Object> ret = new HashMap<>();
                ret.put("error","No user DTO input");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ret);
            }
            HashMap<String,Object> ret = new HashMap<>();
            ArrayList<String> errors = new ArrayList<>();
            if(userDTO.getStrUserName()==null)
            {
                errors.add("UserName is null");
                needReturn = true;
            }
            if(userDTO.getStrEmail()==null)
            {
                errors.add("Email is null");
                needReturn = true;
            }
            if(userDTO.getStrPhone()==null)
            {
                errors.add("Phone is null");
                needReturn = true;
            }
            if(userDTO.getStrUserCategory()==null)
            {
                errors.add("UserCategory is null");
                needReturn = true;
            }
            if(userDTO.getStrUserGender()==null)
            {
                errors.add("UserGender is null");
                needReturn = true;
            }
            ret.put("error",errors);
            if(needReturn)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ret);

            User originalUser = this.userService.getUserById(userId);
            if(originalUser==null)
                throw new IllegalArgumentException("User not found");

            originalUser.setStrUserName(userDTO.getStrUserName());
            originalUser.setStrEmail(userDTO.getStrEmail());
            originalUser.setStrPhone(userDTO.getStrPhone());
            originalUser.setStrUserCategory(userDTO.getStrUserCategory());
            originalUser.setStrUserGender(userDTO.getStrUserGender());
            UserDTO toBeSaved = new UserDTO();
            BeanUtils.copyProperties(userDTO, toBeSaved);
            toBeSaved.setStrPassword(originalUser.getStrPassword());
            User savedUser = this.userService.updateUser(toBeSaved);
            savedUser.setAddresses(null);
            savedUser.setDeliveryOrders(null);
            return ResponseEntity.status(HttpStatus.OK).body(savedUser);
        }
        catch(Exception e)
        {
            HashMap<String,Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/orders/{user_id}/{order_id}")
    public Object updateOrderStatus(@PathVariable("user_id")String strUserId,
                                    @PathVariable("order_id")String strOrderId,
                                    @RequestBody OrderStatusUpdateBody body,
                                    Authentication authentication)
    {
        try
        {
            Integer userId = Integer.valueOf(strUserId);
            this.authenticator.authUser(userId,authentication);
            Integer orderId = Integer.valueOf(strOrderId);
            String status = body.getStatus();
            this.deliveryOrderService.updateOrderStatusbyUser(userId, orderId, status);
            return ResponseEntity.ok().body(this.deliveryOrderService.getOrderDTOById(orderId));
        }
        catch(IllegalArgumentException e)
        {
            HashMap<String,Object> response = new HashMap<>();
            response.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

package org.umlpractice.backend_fooddeliverysystem.controller.api.merchants.menu;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
//import org.umlpractice.backend_fooddeliverysystem.exceptions.NotFoundException;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MenuItemDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.MenuItem;
//import org.umlpractice.backend_fooddeliverysystem.pojo.Merchant;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceMenuItemService;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceMerchantService;
import org.umlpractice.backend_fooddeliverysystem.util.Authenticator;
import org.umlpractice.backend_fooddeliverysystem.util.InterfaceAuthenticator;

import java.util.HashMap;
import java.util.Map;

/**
 * MenuController 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/2 12:56
 */
@RestController
@RequestMapping("/api/merchants/menu")
public class MenuController {
    @Autowired
    InterfaceMenuItemService interfaceMenuItemService;
    @Autowired
    InterfaceAuthenticator authenticator;

    @Autowired
    InterfaceMerchantService interfaceMerchantService;
    @PostMapping("/{merchant_id}")
    public Object addMenuItem(@PathVariable String merchant_id,
                              @RequestBody MenuItemDTO menuItemDTO,
                              Authentication authentication)
    {
        try
        {
            Integer merchantId = Integer.parseInt(merchant_id);
            authenticator.authMerchant(merchantId,authentication);
            MenuItem retValue = interfaceMenuItemService.addMenuItem(merchantId,menuItemDTO);

            Map<String,Object> response = new HashMap<>();
            response.put("item_id",retValue.getiMenuItemId());

            return ResponseEntity.ok(response);
        }
        catch(IllegalArgumentException e)
        {
            Map<String,Object> response = new HashMap<>();
            response.put("error",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{merchant_id}/{item_id}")
    public Object modifyMenuItem(@PathVariable String merchant_id,
                                 @PathVariable String item_id,
                                 @RequestBody MenuItemDTO menuItemDTO,
                                 Authentication authentication)
    {
        try
        {
            Integer merchantId = Integer.parseInt(merchant_id);
            authenticator.authMerchant(merchantId,authentication);
            Integer itemId = Integer.parseInt(item_id);
            menuItemDTO.setiMenuItemId(itemId);
            MenuItem retValue = interfaceMenuItemService.updateMenuItem(merchantId,menuItemDTO);

            Map<String,Object> response = new HashMap<>();
            response.put("item_id",retValue.getiMenuItemId());

            return ResponseEntity.ok(response);
        }
        catch(IllegalArgumentException e)
        {
            Map<String,Object> response = new HashMap<>();
            response.put("error",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{merchant_id}/{item_id}")
    public Object deleteMenuItem(@PathVariable String merchant_id,
                                 @PathVariable String item_id,
                                 Authentication authentication)
    {
        try
        {
            Integer merchantId = Integer.parseInt(merchant_id);
            authenticator.authMerchant(merchantId,authentication);
            Integer itemId = Integer.parseInt(item_id);
            boolean retValue = interfaceMenuItemService.deleteMenuItem(merchantId,itemId);

            Map<String,Object> response = new HashMap<>();

            if(retValue)
                return ResponseEntity.ok(response);
            response.put("error","Can't delete menu item");
            return ResponseEntity.badRequest().body(response);
        }
        catch(IllegalArgumentException e)
        {
            Map<String,Object> response = new HashMap<>();
            response.put("error",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

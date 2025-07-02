package org.umlpractice.backend_fooddeliverysystem.controller.api.users.addresses;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
//import org.umlpractice.backend_fooddeliverysystem.exceptions.NotFoundException;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.AddressDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.Address;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceAddressService;
import org.umlpractice.backend_fooddeliverysystem.util.InterfaceAuthenticator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users/addresses")
public class AddressController {
    @Autowired
    private InterfaceAddressService interfaceAddressService;
    @Autowired
    private InterfaceAuthenticator interfaceAuthenticator;

    @PostMapping("/{user_id}")
    public Object addAddress(@PathVariable String user_id, @RequestBody AddressDTO addressDTO, Authentication authentication)
    {
        try{
            Integer userId = Integer.parseInt(user_id);
            interfaceAuthenticator.authUser(userId,authentication);
            Address retValue = interfaceAddressService.addAddress(userId, addressDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("address_id", retValue.getIAddressId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String,Object> response = new HashMap<>();
            response.put("error",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{user_id}/{address_id}")
    public Object modifyAddress(@PathVariable String user_id,
                                @PathVariable String address_id,
                                @RequestBody AddressDTO addressDTO,
                                Authentication authentication) {
        try {
            Integer userId = Integer.parseInt(user_id);
            Integer addressId = Integer.parseInt(address_id);
            this.interfaceAuthenticator.authUser(userId,authentication);
            addressDTO.setiAddressId(addressId);

            Address retValue = interfaceAddressService.updateAddress(userId, addressDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("address_id", retValue.getIAddressId());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{user_id}/{address_id}")
    public Object deleteAddress(@PathVariable String user_id,
                                @PathVariable String address_id,
                                Authentication authentication)
    {
        Integer userId = Integer.parseInt(user_id);
        this.interfaceAuthenticator.authUser(userId,authentication);
        Integer addressId = Integer.parseInt(address_id);
        try
        {
            boolean retValue = interfaceAddressService.deleteAddress(userId, addressId);
            Map<String, Object> response = new HashMap<>();
            if(retValue)
                return ResponseEntity.ok(response);
            response.put("error","Can't delete address");
            return ResponseEntity.badRequest().body(response);
        }
        catch(IllegalArgumentException e)
        {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/{user_id}")
    public Object getAllAddresses(@PathVariable String user_id,
                                  Authentication authentication)
    {
        Integer userId = Integer.parseInt(user_id);
        try
        {
            this.interfaceAuthenticator.authUser(userId,authentication);
            List<AddressDTO> addresses = interfaceAddressService.getAllAddressesByUserId(userId);
            HashMap<String,Object> response = new HashMap<>();
            response.put("addresses", addresses);
            return ResponseEntity.ok(response);
        }
        catch(IllegalArgumentException e)
        {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{user_id}/{address_id}/default")
    public Object setDefaultAddress(@PathVariable String user_id,
                                    @PathVariable String address_id,
                                    Authentication authentication)
    {
        try {
            Integer userId = Integer.parseInt(user_id);
            this.interfaceAuthenticator.authUser(userId,authentication);
            Integer addressId = Integer.parseInt(address_id);
            User user = interfaceAddressService.getUserById(userId);
            user.setDefaultAddress(addressId);
            interfaceAddressService.setDefaultAddress(userId, addressId);
            Map<String, Object> response = new HashMap<>();
            response.put("msg", "set default address for user "+user.getiUserId()+" address "+addressId+" successfully");
            return ResponseEntity.ok(response);
        }
        catch(IllegalArgumentException e)
        {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

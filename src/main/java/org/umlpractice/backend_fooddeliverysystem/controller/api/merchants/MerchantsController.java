package org.umlpractice.backend_fooddeliverysystem.controller.api.merchants;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.umlpractice.backend_fooddeliverysystem.exceptions.AlreadyExistException;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MerchantDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MerchantModificationDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.OrderStatusUpdateBody;
import org.umlpractice.backend_fooddeliverysystem.pojo.Merchant;
import org.umlpractice.backend_fooddeliverysystem.pojo.ResponseMessage;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceDeliveryOrderService;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceMerchantService;
import org.umlpractice.backend_fooddeliverysystem.util.InterfaceAuthenticator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MerchantRegisterController 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/1 15:44
 */
@RestController
@RequestMapping("/api/merchants")
public class MerchantsController {

    @Autowired
    private InterfaceMerchantService merchantService;

    @Autowired
    private InterfaceDeliveryOrderService deliveryOrderService;

    @Autowired
    private InterfaceAuthenticator authenticator;

    @Getter
    @Setter
    private static class MerchantRegisterBody
    {
        private String strMerchantName;
        private String strPassword;
        private String strPhone;

        @Override
        public String toString() {
            return "MerchantRegisterBody{" +
                    "strMerchantName='" + strMerchantName + '\'' +
                    ", strPassword='" + strPassword + '\'' +
                    ", strPhone='" + strPhone + '\'' +
                    '}';
        }
    }
    @PostMapping("/register")
    public Object merchantRegister(@RequestBody MerchantRegisterBody body)
    {
        try
        {
            MerchantDTO merchantDTO = new MerchantDTO();
            merchantDTO.setStrMerchantName(body.getStrMerchantName());
            merchantDTO.setStrPassword(body.getStrPassword());
            merchantDTO.setStrPhone(body.getStrPhone());

            Merchant res = merchantService.registerMerchant(merchantDTO);
            class registerResponseObject
            {
                private Integer merchant_id;
                public registerResponseObject()
                {

                }
                @Override
                public String toString() {
                    return "registerResponseObject{" +
                            "merchant_id=" + merchant_id +
                            '}';
                }

                public Integer getMerchant_id() {
                    return merchant_id;
                }

                public void setMerchant_id(Integer merchant_id) {
                    this.merchant_id = merchant_id;
                }
            }
            registerResponseObject ret = new registerResponseObject();
            ret.setMerchant_id(res.getiMerchantId());
            return ResponseEntity.status(HttpStatus.OK).body(ret);
        }
        catch(AlreadyExistException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE.value()).body(new ResponseMessage<>(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage(),"null"));
        }
    }

    private boolean merchantValidation(MerchantDTO merchantDTO)
    {
        if(merchantDTO==null)
            return false;
        return merchantDTO.getdAveragePrice()!=null
                &&merchantDTO.getStrMerchantName()!=null
                &&merchantDTO.getStrPassword()!=null
                &&merchantDTO.getStrPhone()!=null
                &&merchantDTO.getdDeliveryFee()!=null
                &&merchantDTO.getStrCoverImageUrl()!=null
                &&merchantDTO.getdMinOrder()!=null
                &&merchantDTO.getdDeliveryTime()!=null
                &&merchantDTO.getdRating()!=null
                &&merchantDTO.getStrLogoUrl()!=null;
    }

    @PatchMapping("/orders/{merchant_id}/{order_id}")
    public Object updateOrderStatus(@PathVariable("merchant_id")String strMerchantId,
                                    @PathVariable("order_id")String strOrderId,
                                    @RequestBody OrderStatusUpdateBody body,
                                    Authentication authentication)
    {
        try
        {
            Integer merchantId = Integer.valueOf(strMerchantId);
            Integer orderId = Integer.valueOf(strOrderId);
            this.authenticator.authMerchant(merchantId, authentication);
            String status = body.getStatus();
            this.deliveryOrderService.updateOrderStatusbyMerchant(merchantId, orderId, status);
            return ResponseEntity.ok().body(this.deliveryOrderService.getOrderDTOById(orderId));
        }
        catch(IllegalArgumentException e)
        {
            HashMap<String,Object> response = new HashMap<>();
            response.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PatchMapping("/{merchant_id}/update")
    public Object updateMerchant(@PathVariable("merchant_id")String strMerchantId,
                                 @RequestBody MerchantModificationDTO merchantDTO,
                                 Authentication authentication)
    {
        try
        {
            Integer merchantId = Integer.valueOf(strMerchantId);
            this.authenticator.authMerchant(merchantId, authentication);
            boolean needReturn = false;
            if(merchantDTO==null)
            {
                HashMap<String,Object> ret = new HashMap<>();
                ret.put("error","No merchant DTO input");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ret);
            }
            HashMap<String,Object> ret = new HashMap<>();
            ArrayList<String> errors = new ArrayList<>();
            if(merchantDTO.getStrMerchantName()==null)
            {
                errors.add("UserName is null");
                needReturn = true;
            }
            if(merchantDTO.getStrPhone()==null)
            {
                errors.add("Phone is null");
                needReturn = true;
            }
            if(merchantDTO.getStrMerchantCategory()==null)
            {
                errors.add("UserCategory is null");
                needReturn = true;
            }
            ret.put("error",errors);
            if(needReturn)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ret);

            Merchant originalMerchant = this.merchantService.getMerchantByMerchantId(merchantId);
            if(originalMerchant==null)
                throw new IllegalArgumentException("User not found");

            originalMerchant.setStrMerchantName(merchantDTO.getStrMerchantName());
            //originalMerchant.setStrPhone(merchantDTO.getStrPhone());
            originalMerchant.setStrLogoUrl(merchantDTO.getStrLogoUrl());
            //originalMerchant.setStrPassword(merchantDTO.getStrPassword());
            originalMerchant.setStrCoverImageUrl(merchantDTO.getStrCoverImageUrl());
            originalMerchant.setStrMerchantCategory(Merchant.StrMerchantCategory.valueOf(merchantDTO.getStrMerchantCategory()));
            originalMerchant.setdMinOrder(merchantDTO.getdMinOrder());
            originalMerchant.setdDeliveryFee(merchantDTO.getdDeliveryFee());
            originalMerchant.setdDeliveryTime(merchantDTO.getdDeliveryTime());

            MerchantDTO toBeSaved = new MerchantDTO();
            BeanUtils.copyProperties(merchantDTO,toBeSaved);
            Merchant saveRes = this.merchantService.updateMerchant(toBeSaved);
            saveRes.setDeliveryOrders(null);
            return ResponseEntity.status(HttpStatus.OK).body(saveRes);
        }
        catch(Exception e)
        {
            HashMap<String,Object> response = new HashMap<>();
            response.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

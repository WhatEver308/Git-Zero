package org.umlpractice.backend_fooddeliverysystem.controller.api.merchants.auth;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.*;
import org.umlpractice.backend_fooddeliverysystem.pojo.LoginError;
import org.umlpractice.backend_fooddeliverysystem.pojo.Merchant;
import org.umlpractice.backend_fooddeliverysystem.pojo.ResponseMessage;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.umlpractice.backend_fooddeliverysystem.security.provider.DAOAuthenticationProviderForMerchants;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceMerchantService;
import org.umlpractice.backend_fooddeliverysystem.service.MerchantServiceImplement;
import org.umlpractice.backend_fooddeliverysystem.util.JWTUtil;

import java.util.HashMap;

/**
 * MerchantsController 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/1 15:38
 */
@RestController
@RequestMapping("/api/merchants/auth")
public class MerchantsLoginController {
    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    DAOAuthenticationProviderForMerchants daoAuthenticationProviderForMerchants;
    @Autowired
    private MerchantServiceImplement merchantServiceImplement;

    @PostMapping("/login")
    public Object merchantLogin(@RequestBody MerchantLoginDTO merchantLoginDTO)
    {
        UserLoginDTO userLoginDTO = (UserLoginDTO) merchantLoginDTO;
        try {
            //
            Authentication authentication = daoAuthenticationProviderForMerchants.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getStrUserName(), userLoginDTO.getStrPassword()));
            String username = (String)authentication.getPrincipal();
            String token = jwtUtil.generateToken(username);

            Merchant merchant = merchantServiceImplement.getMerchantByMerchantName(username);
            MerchantDTO dto = new MerchantDTO();
            BeanUtils.copyProperties(merchant, dto);

            class merchant_info
            {
                public merchant_info(String merchant_name,Integer merchant_id) {
                    this.merchant_name = merchant_name;
                    this.merchant_id = merchant_id;
                }

                private String merchant_name;

                private Integer merchant_id;

                public Integer getMerchant_id() {
                    return merchant_id;
                }

                public void setMerchant_id(Integer merchant_id) {
                    this.merchant_id = merchant_id;
                }

                public String getMerchant_name() {
                    return merchant_name;
                }

                public void setMerchant_name(String merchant_name) {
                    this.merchant_name = merchant_name;
                }

                @Override
                public String toString() {
                    return "merchant_info{" +
                            "merchant_name='" + merchant_name + '\'' +
                            ", merchant_id=" + merchant_id +
                            '}';
                }
            }
            class merchantLoginResponseObject
            {
                merchant_info merchant_info;

                private String token;

                @Override
                public String toString() {
                    return "merchantLoginResponseObject{" +
                            "merchant_info=" + merchant_info +
                            ", token='" + token + '\'' +
                            '}';
                }

                public merchant_info getMerchant_info() {
                    return merchant_info;
                }

                public void setMerchant_info(merchant_info merchant_info) {
                    this.merchant_info = merchant_info;
                }

                public String getToken() {
                    return token;
                }

                public void setToken(String token) {
                    this.token = token;
                }

                public merchantLoginResponseObject(merchant_info merchant_info, String token) {
                    this.merchant_info = merchant_info;
                    this.token = token;
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new merchantLoginResponseObject(new merchant_info(username,merchant.getiMerchantId()),token));
        }
        catch (BadCredentialsException e)
        {
            //return LoginError.loginError("api/users/auth/login");
            //return new ResponseMessage<>(HttpStatus.UNAUTHORIZED.value(),"Invalid Credential","/api/users/auth/login");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(LoginError.loginError("/api/users/auth/login"));
            //return ResponseMessage.failure("Bad credentials");
        }
        catch(UsernameNotFoundException e)
        {
            HashMap<String,Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}

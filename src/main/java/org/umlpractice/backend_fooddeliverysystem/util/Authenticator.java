package org.umlpractice.backend_fooddeliverysystem.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceMerchantService;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceUserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticator 类说明
 *
 * @author 刘陈文君
 * @date 2025/7/1 14:05
 */
@Component
public class Authenticator implements InterfaceAuthenticator
{
    @Autowired
    InterfaceUserService userService;
    @Autowired
    InterfaceMerchantService merchantService;

    @Override
    public boolean authUser(Integer userId, Authentication authentication) throws IllegalArgumentException
    {
        boolean shouldPass = false;
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        if(roles.contains("ROLE_WHITELISTED"))
        {
            System.out.println("Allowed SUBMIT_ORDER for WHITELISTED :"+String.valueOf(userId));
            shouldPass = true;
        }
        else
        {
            shouldPass = userService.verifyUser(userId,authentication.getName());
        }
        return shouldPass;
    }

    @Override
    public boolean authMerchant(Integer merchantId, Authentication authentication) throws IllegalArgumentException
    {
        boolean shouldPass = false;
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        if(roles.contains("ROLE_WHITELISTED"))
        {
            System.out.println("Allowed SUBMIT_ORDER for WHITELISTED :"+String.valueOf(merchantId));
            shouldPass = true;
        }
        else
        {
            shouldPass = merchantService.verifyMerchant(merchantId,authentication.getName());
        }
        return shouldPass;
    }
}

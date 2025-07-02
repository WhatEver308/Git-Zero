package org.umlpractice.backend_fooddeliverysystem.util;

import org.springframework.security.core.Authentication;

public interface InterfaceAuthenticator {
    public boolean authUser(Integer userId, Authentication auth) throws IllegalArgumentException;
    public boolean authMerchant(Integer merchantId,Authentication auth) throws IllegalArgumentException;
}

package org.umlpractice.backend_fooddeliverysystem.security.provider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.umlpractice.backend_fooddeliverysystem.pojo.Merchant;
import org.umlpractice.backend_fooddeliverysystem.service.MerchantServiceImplement;

/**
 * DAOAuthenticationProviderForMerchants 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/1 16:11
 */
@Component
public class DAOAuthenticationProviderForMerchants{
    @Autowired
    private MerchantServiceImplement merchantServiceImplement;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Authentication authenticate(Authentication authentication) throws BadCredentialsException
    {
        String username = (String)authentication.getName();
        String password = (String)authentication.getCredentials().toString();

        UserDetails userDetails = merchantServiceImplement.loadByMerchantName(username);
        if(!passwordEncoder.matches(password, userDetails.getPassword()))
        {
            throw new BadCredentialsException("Bad credentials");
        }
        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }
}

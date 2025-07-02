package org.umlpractice.backend_fooddeliverysystem.util.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.umlpractice.backend_fooddeliverysystem.DAO.IPWhitelistDAO;
import org.umlpractice.backend_fooddeliverysystem.DAO.APIWhitelistDAO;
import org.umlpractice.backend_fooddeliverysystem.pojo.APIWhitelistItem;
import org.umlpractice.backend_fooddeliverysystem.pojo.IPWhitelistItem;

import java.io.IOException;
import java.util.List;

/**
 * WhitelistFilter 类说明
 *
 * @author 刘陈文君
 * @date 2025/5/29 00:06
 */
@Component
public class WhitelistFilter extends OncePerRequestFilter {

    @Autowired
    IPWhitelistDAO ipWhitelistDAO;

    @Autowired
    APIWhitelistDAO apiWhitelistDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException
    {
        String ipAddress = request.getRemoteAddr();
        String path = request.getServletPath();

        if(ipIsInWhitelist(ipAddress))
        {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    "whitelisted", null, List.of(new SimpleGrantedAuthority("ROLE_WHITELISTED"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("Allowed API Path: " + path+" at IP Address: " + ipAddress);
        }
        else if(apiIsInWhiteList(path))
        {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    "whitelisted", null, List.of(new SimpleGrantedAuthority("ROLE_WHITELISTED"))
            );
            System.out.println("Allowed API Path: " + path+" at IP Address: " + ipAddress);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    private boolean ipIsInWhitelist(String ipAddress)
    {
        Iterable<IPWhitelistItem> ipWhitelistItems = ipWhitelistDAO.findAll();
        for(IPWhitelistItem ipWhitelistItem : ipWhitelistItems)
        {
            if(ipWhitelistItem.getStrIP().equals(ipAddress))
                return true;
        }
        return false;
        //return ipWhitelistDAO.findAll().stream().anyMatch(ip -> ip.getStrIP().equals(ipAddress));
    }

    private boolean apiIsInWhiteList(String apiPath)
    {
        Iterable<APIWhitelistItem> apiWhitelistItems = apiWhitelistDAO.findAll();
        for(APIWhitelistItem apiWhitelistItem : apiWhitelistItems)
        {
            if(apiWhitelistItem.getStrPath().equals(apiPath))
                return true;
        }
        return false;
        //return apiWhitelistDAO.findAll().stream().anyMatch(api -> api.getStrPath().equals(apiPath));
    }
}

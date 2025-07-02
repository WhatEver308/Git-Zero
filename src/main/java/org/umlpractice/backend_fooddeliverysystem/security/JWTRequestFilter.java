package org.umlpractice.backend_fooddeliverysystem.security;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.umlpractice.backend_fooddeliverysystem.service.UserDetailsServiceImplement;
import org.umlpractice.backend_fooddeliverysystem.util.JWTUtil;
import java.io.IOException;
import java.security.SignatureException;

/**
 * JWTRequestFilter 类说明
 * 默认JWT附加在Header中
 * 默认格式:Authorization Bearer ${token}
 * @author 刘陈文君
 * @date 2025 /5/28 18:01
 */
@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImplement userService; // 需实现此接口

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        final String authorizationHeader = request.getHeader("Authorization");

        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            chain.doFilter(request, response);

        String username = null;
        String jwt = null;

        // 从请求头提取 Token（格式：Bearer <token>）
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.parseUserNameFromToken(jwt);
            }
            catch (SecurityException e) {
                // Token 无效，后续处理会拦截
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(e.getMessage());
            }
            catch(Exception e)
            {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(e.getMessage());
            }

        }
        // 如果 Token 有效，则设置认证信息
        if (username != null) {
            UserDetails user = userService.loadUserByUsername(username);
            if (jwtUtil.verifyToken(jwt)) {
                System.out.println("JWT verified for user " + username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}

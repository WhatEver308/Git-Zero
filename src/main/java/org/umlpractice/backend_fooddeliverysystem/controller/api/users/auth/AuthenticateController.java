package org.umlpractice.backend_fooddeliverysystem.controller.api.users.auth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.UserDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.UserLoginDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.UserLoginResponseDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.LoginError;
import org.umlpractice.backend_fooddeliverysystem.pojo.ResponseMessage;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.umlpractice.backend_fooddeliverysystem.security.provider.DAOAuthenticationProviderForUsers;
import org.umlpractice.backend_fooddeliverysystem.service.UserServiceImplement;
import org.umlpractice.backend_fooddeliverysystem.util.JWTUtil;

/**
 * AuthenticateController 类说明
 *
 * @author 刘陈文君
 * @date 2025 /5/27 20:26
 */
@RestController
@RequestMapping("/api/users/auth")
public class AuthenticateController
{
    //private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserServiceImplement userServiceImplement;

    @Autowired
    private DAOAuthenticationProviderForUsers daoAuthenticationProviderForUsers;

    /**
     * User login response message.
     *
     * @param userLoginDTO the user dto
     * @return the response message
     */
    @PostMapping("/login")
    public Object userLogin(@RequestBody UserLoginDTO userLoginDTO)
    {
        try {
            //
            Authentication authentication = daoAuthenticationProviderForUsers.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getStrUserName(), userLoginDTO.getStrPassword()));
            String username = (String)authentication.getPrincipal();
            String token = jwtUtil.generateToken(username);
            UserLoginResponseDTO ret = new UserLoginResponseDTO();
            ret.setToken(token);
            User user = userServiceImplement.getUserByUserName(username);
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(user, dto);
            ret.setUser_info(dto);
            return ResponseMessage.success(ret);
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
    }
}

package org.umlpractice.backend_fooddeliverysystem.controller;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.UserDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.ResponseMessage;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceUserService;

/**
 * The type User controller.
 */
@RestController             //返回JSON
@RequestMapping("/debug/user") //访问URL:localhost:8080/user
/**
 * UserRegisterController 类说明
 * 接受来自前端的POST/GET/PUT等请求
 * 自动装配接口interfaceUserService的实现类UserService实例userService
 * 接口位置:http://localhost:8080/user
 * @author 刘陈文君
 * @date 2025/5/27 14:30
 */
public class UserController
{
    @Autowired
    private InterfaceUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Add user to MySQL server.
     *
     * @param user the user
     * @return the response message
     */
    @PostMapping
    public ResponseMessage<UserDTO> add(@RequestBody UserDTO user)
    {
        User ret = userService.addUser(user);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(ret, userDTO);
        return ResponseMessage.success(userDTO);
    }
}

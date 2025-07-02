package org.umlpractice.backend_fooddeliverysystem.controller.api.users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.UserDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.ResponseMessage;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.umlpractice.backend_fooddeliverysystem.service.UserServiceImplement;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * APIUserRegisterController 类说明
 *
 * @author 刘陈文君
 * @date 2025/5/27 20:27
 */
@RestController
@RequestMapping("/api/users/register")
public class UserRegisterController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserServiceImplement userServiceImplement;

    @PostMapping()
    public Object registerUser(@RequestBody UserDTO userDTO)
    {
        if(!userDTOValidation(userDTO))
        {
            if(userDTO==null)
            {
                HashMap<String,Object> ret = new HashMap<>();
                ret.put("error","No user DTO input");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ret);
            }
            HashMap<String,Object> ret = new HashMap<>();
            ArrayList<String> errors = new ArrayList<>();

            if(userDTO.getStrUserName()==null)
                errors.add("UserName is null");
            if(userDTO.getStrPassword()==null)
                errors.add("Password is null");
            if(userDTO.getStrEmail()==null)
                errors.add("Email is null");
            if(userDTO.getStrPhone()==null)
                errors.add("Phone is null");
            if(userDTO.getStrUserCategory()==null)
                errors.add("UserCategory is null");
            if(userDTO.getStrUserGender()==null)
                errors.add("UserGender is null");
            ret.put("error",errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ret);
        }
        User ret = userServiceImplement.registerUser(userDTO);
        if(ret == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage<>(HttpStatus.CONFLICT.value(),"User already registered",""));

        class registerReturn
        {
            private Integer user_id;

            public Integer getUser_id() {
                return user_id;
            }

            public void setUser_id(Integer user_id) {
                this.user_id = user_id;
            }

            @Override
            public String toString() {
                return "registerReturn{" +
                        "user_id=" + user_id +
                        '}';
            }

            public registerReturn(Integer user_id) {
                this.user_id = user_id;
            }
        };
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new registerReturn(ret.getiUserId()));
        //return ResponseMessage.success(userDTOReturn);
    }

    private boolean userDTOValidation(UserDTO userDTO)
    {
        if(userDTO == null)
            return false;
        return userDTO.getStrUserName()!=null
                &&userDTO.getStrPassword()!=null
                &&userDTO.getStrEmail()!=null
                &&userDTO.getStrPhone()!=null
                &&userDTO.getStrUserCategory()!=null
                &&userDTO.getStrUserGender()!=null;
    }

    /*

    */

}

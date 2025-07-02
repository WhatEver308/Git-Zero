package org.umlpractice.backend_fooddeliverysystem.debug.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.DeliveryOrderDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.OrderItemDTO;

/**
 * debug_arrayInputTest 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/28 16:51
 */
@Controller
@RequestMapping("/debug/arrayInputTest")
public class debug_arrayInputTest {
    @PostMapping
    public Object foo(@RequestBody DeliveryOrderDTO input)
    {
        System.out.println(input.toString());
        return ResponseEntity.ok().body(input);
    }
}

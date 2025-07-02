package org.umlpractice.backend_fooddeliverysystem.debug.controller;


import jdk.jfr.Registered;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.umlpractice.backend_fooddeliverysystem.debug.service.debug_add_api_whitelist_service;
import org.umlpractice.backend_fooddeliverysystem.pojo.APIWhitelistItem;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.APIWhitelistItemDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.ResponseMessage;

/**
 * debug_add_api_whitelist 类说明
 *
 * @author 刘陈文君
 * @date 2025/5/29 20:25
 */
@RestController
@RequestMapping("debug/apiwhitelist")
public class debug_add_api_whitelist {

    @Autowired
    debug_add_api_whitelist_service debug_add_api_whitelist_service;

    @PostMapping
    public ResponseMessage<APIWhitelistItemDTO> debug_add_api_whitelist(@RequestBody APIWhitelistItemDTO apiWhitelistItemDTO)
    {
        APIWhitelistItemDTO dto = new APIWhitelistItemDTO();
        APIWhitelistItem returnVal = debug_add_api_whitelist_service.add_api_whitelist_service(apiWhitelistItemDTO);
        BeanUtils.copyProperties(returnVal, dto);
        return ResponseMessage.success(dto);
    }
}

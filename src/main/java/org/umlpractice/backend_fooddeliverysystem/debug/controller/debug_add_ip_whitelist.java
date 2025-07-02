package org.umlpractice.backend_fooddeliverysystem.debug.controller;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.umlpractice.backend_fooddeliverysystem.pojo.IPWhitelistItem;
import org.umlpractice.backend_fooddeliverysystem.pojo.ResponseMessage;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.IPWhitelistItemDTO;
import org.umlpractice.backend_fooddeliverysystem.debug.service.*;
/**
 * debug_add_ip_whitelist 类说明
 *
 * @author 刘陈文君
 * @date 2025/5/29 02:22
 */
@RestController
@RequestMapping("/debug/ipwhitelist")
public class debug_add_ip_whitelist {

    @Autowired
    interface_debug_add_ip_whitelist_service ipWhitelistService;

    @PostMapping
    public ResponseMessage<IPWhitelistItemDTO> add_ip_whitelist(@RequestBody IPWhitelistItemDTO ipWhitelistItemDTO)
    {
        IPWhitelistItemDTO ret = new IPWhitelistItemDTO();
        IPWhitelistItem returnVal = ipWhitelistService.add_ip_whitelist(ipWhitelistItemDTO);
        BeanUtils.copyProperties(returnVal, ret);
        return ResponseMessage.success(ret);
    }
}

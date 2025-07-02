package org.umlpractice.backend_fooddeliverysystem.debug.service;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.umlpractice.backend_fooddeliverysystem.DAO.APIWhitelistDAO;
import org.umlpractice.backend_fooddeliverysystem.pojo.APIWhitelistItem;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.APIWhitelistItemDTO;

/**
 * debug_add_api_whitelist_service 类说明
 *
 * @author 刘陈文君
 * @date 2025/5/29 20:29
 */
@Service
public class debug_add_api_whitelist_service implements interface_debug_add_api_whitelist_service{
    @Autowired
    APIWhitelistDAO apiWhitelistDAO;

    @Override
    public APIWhitelistItem add_api_whitelist_service(APIWhitelistItemDTO apiWhitelistItem)
    {
        APIWhitelistItem pojo = new APIWhitelistItem();
        BeanUtils.copyProperties(apiWhitelistItem, pojo);
        return apiWhitelistDAO.save(pojo);
    }
}

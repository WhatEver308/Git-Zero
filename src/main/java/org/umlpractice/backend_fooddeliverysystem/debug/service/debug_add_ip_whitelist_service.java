package org.umlpractice.backend_fooddeliverysystem.debug.service;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.umlpractice.backend_fooddeliverysystem.DAO.IPWhitelistDAO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.IPWhitelistItemDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.IPWhitelistItem;

/**
 * debug_add_ip_whitelist_service 类说明
 *
 * @author 刘陈文君
 * @date 2025/5/29 15:00
 */
@Service
public class debug_add_ip_whitelist_service implements interface_debug_add_ip_whitelist_service{
    @Autowired
    private IPWhitelistDAO ipWhitelistDAO;

    @Override
    public IPWhitelistItem add_ip_whitelist(IPWhitelistItemDTO itemDTO)
    {
        IPWhitelistItem item = new IPWhitelistItem();
        BeanUtils.copyProperties(itemDTO,item);
        return ipWhitelistDAO.save(item);
    }

    @Override
    public void delete_ip_whitelist_by_IP(String ip)
    {
        IPWhitelistItem item = ipWhitelistDAO.findIPWhitelistItemByIp(ip);
        ipWhitelistDAO.delete(item);
    }

    @Override
    public IPWhitelistItem get_ip_whitelist_item_by_IP(String item)
    {
        return ipWhitelistDAO.findIPWhitelistItemByIp(item);
    }

    @Override
    public IPWhitelistItem update_ip_whitelist(IPWhitelistItemDTO itemDTO)
    {
        IPWhitelistItem findResult = ipWhitelistDAO.findIPWhitelistItemByIp(itemDTO.getStrIP());
        if(findResult != null)
        {
            BeanUtils.copyProperties(itemDTO,findResult);
            return ipWhitelistDAO.save(findResult);
        }
        IPWhitelistItem pojoItem = new IPWhitelistItem();
        BeanUtils.copyProperties(itemDTO,pojoItem);
        return ipWhitelistDAO.save(pojoItem);
    }
}

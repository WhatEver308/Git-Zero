package org.umlpractice.backend_fooddeliverysystem.debug.service;

import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.IPWhitelistItemDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.IPWhitelistItem;

public interface interface_debug_add_ip_whitelist_service {
    public IPWhitelistItem add_ip_whitelist(IPWhitelistItemDTO item);
    public void delete_ip_whitelist_by_IP(String ip);
    public IPWhitelistItem get_ip_whitelist_item_by_IP(String item);
    public IPWhitelistItem update_ip_whitelist(IPWhitelistItemDTO ip_whitelist_service);
}

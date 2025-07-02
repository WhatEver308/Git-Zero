package org.umlpractice.backend_fooddeliverysystem.service;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MenuItemDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.MenuItem;
import org.umlpractice.backend_fooddeliverysystem.pojo.Merchant;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MenuItemQueryResponseDTO;


/**
 * The interface Interface menu item service.
 * 菜品相关操作接口
 * 提供新增、删除和修改菜品的功能
 * @author LiJunjie
 * @date 2025/5/30
 */
public interface InterfaceMenuItemService {
    public Merchant getMerchantById(Integer iMerchantId);

    public MenuItem addMenuItem(Integer iMerchantId, MenuItemDTO menuItemDTO);

    public MenuItem updateMenuItem(Integer iMerchantId, MenuItemDTO menuItemDTO);

    public boolean deleteMenuItem(Integer iMerchantId, Integer iMenuItemId);

    public MenuItem getMenuItemById(Integer iMenuItemId);

    public MenuItemQueryResponseDTO getMenuItemDTOById(Integer iMerchantId, Integer iMenuItemId);

    public MenuItemQueryResponseDTO getMenuItemDTOById(Integer iMenuItemId);
}

package org.umlpractice.backend_fooddeliverysystem.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.umlpractice.backend_fooddeliverysystem.DAO.InterfaceMenuItemDAO;
import org.umlpractice.backend_fooddeliverysystem.DAO.InterfaceMerchantDAO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MenuItemDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.MenuItem;
import org.umlpractice.backend_fooddeliverysystem.pojo.Merchant;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MenuItemQueryResponseDTO;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MenuItemServiceImplement implements InterfaceMenuItemService {

    @Autowired
    InterfaceMerchantDAO merchantDAO;
    @Autowired
    InterfaceMenuItemDAO menuItemDAO;


    @Override
    @Transactional(readOnly = true)
    public Merchant getMerchantById(Integer iMerchantId) {
        Optional<Merchant> optionalMerchant = merchantDAO.findById(iMerchantId);
        if (optionalMerchant.isPresent()) {
            Merchant merchant = optionalMerchant.get();
            return merchant;
        } else {
            throw new IllegalArgumentException("Merchant with ID " + iMerchantId + " not found.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuItem addMenuItem(Integer iMerchantId,MenuItemDTO menuItemDTO) {
        final Merchant merchant = getMerchantById(iMerchantId);

        if(menuItemDTO == null) {
            throw new IllegalArgumentException("MenuItemDTO cannot be null.");
        }

        if(!menuItemDTO.isValidFORAdd()){
            throw new IllegalArgumentException("MenuItemDTO is not valid. Please check the fields.");
        }

        MenuItem newMenuItem = new MenuItem();
        BeanUtils.copyProperties(menuItemDTO, newMenuItem, "strMenuItemCategory");

        // 安全处理枚举转换
        String category = menuItemDTO.getStrMenuItemCategory();
        try {
            newMenuItem.setStriMenuItemCategory(MenuItem.StriMenuItemCategory.valueOf(category));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + category, e);
        }

        // 设置关联商户
        newMenuItem.setMerchant(merchant);

        // 将新菜品添加到商户的菜单中
        merchant.addMenuItem(newMenuItem);

        // 注意，这里仅是内存中操作，实际保存到数据库需要通过 DAO 层进行持久化操作

        // 保存商户和菜品
        merchantDAO.save(merchant);
        return menuItemDAO.save(newMenuItem);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuItem updateMenuItem(Integer iMerchantId, MenuItemDTO menuItemDTO) {
        Merchant merchant = getMerchantById(iMerchantId);

        if(menuItemDTO == null) {
            throw new IllegalArgumentException("MenuItemDTO cannot be null.");
        }

        if(!menuItemDTO.isValidForUpdate()){
            throw new IllegalArgumentException("MenuItemDTO is not valid for update. Please check the fields.");
        }

        MenuItem existingMenuItem = getMenuItemById(menuItemDTO.getiMenuItemId());
        MenuItem updatedMenuItem = new MenuItem();

        if(! existingMenuItem.getMerchant().getiMerchantId().equals(iMerchantId)) {
            throw new IllegalArgumentException("MenuItem does not belong to the specified Merchant.");
        }

        if(existingMenuItem.getbDirtyBit()== true) {
            throw new IllegalArgumentException("MenuItem is dirty and cannot be updated directly. Please update it first.");
        }

        BeanUtils.copyProperties(menuItemDTO, updatedMenuItem, "strMenuItemCategory","iMenuItemId");

        // 安全处理枚举转换
        String category = menuItemDTO.getStrMenuItemCategory();
        try {
            updatedMenuItem.setStriMenuItemCategory(MenuItem.StriMenuItemCategory.valueOf(category));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + category, e);
        }

        existingMenuItem.setbDirtyBit(true); // 设置脏位，表示需要更新

        merchant.removeMenuItem(existingMenuItem.getiMenuItemId());
        merchant.addMenuItem(updatedMenuItem);

        updatedMenuItem.setMerchant(merchant);

        merchantDAO.save(merchant);

        menuItemDAO.save(existingMenuItem); // 保存脏位状态

        return menuItemDAO.save(updatedMenuItem);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenuItem(Integer iMerchantId, Integer iMenuItemId) {
        Merchant merchant = getMerchantById(iMerchantId);
        MenuItem menuItem = getMenuItemById(iMenuItemId);

        if(!menuItem.getMerchant().getiMerchantId().equals(iMerchantId) ) {
            throw new IllegalArgumentException("MenuItem does not belong to the specified Merchant.");
        }

        if(menuItem.getbDirtyBit()==true) {
            throw new IllegalArgumentException("MenuItem is dirty and cannot be deleted directly. Please update it first.");
        }

        menuItem.setbDirtyBit(true); // 设置脏位，表示需要更新

        merchant.removeMenuItem(iMenuItemId);
        merchantDAO.save(merchant);

        menuItemDAO.save(menuItem); // 保存脏位状态

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItem getMenuItemById(Integer iMenuItemId) {
        Optional<MenuItem> optionalMenuItem = menuItemDAO.findById(iMenuItemId);
        if (optionalMenuItem.isPresent()) {
            return optionalMenuItem.get();
        } else {
            throw new IllegalArgumentException("MenuItem with ID " + iMenuItemId + " not found.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemQueryResponseDTO getMenuItemDTOById(Integer iMerchantId, Integer iMenuItemId) {
        Merchant merchant = getMerchantById(iMerchantId);
        MenuItem menuItem = getMenuItemById(iMenuItemId);

        MenuItemQueryResponseDTO responseDTO = new MenuItemQueryResponseDTO();

        if (menuItem.getMerchant().getiMerchantId().equals(iMerchantId)) {
            throw new IllegalArgumentException("MenuItem does not belong to the specified Merchant.");
        }

        BeanUtils.copyProperties(menuItem, responseDTO,"strMenuItemCategory");

        // 安全处理枚举转换
        String category = menuItem.getStriMenuItemCategory().name();
        responseDTO.setStrMenuItemCategory(category);

        return responseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemQueryResponseDTO getMenuItemDTOById(Integer iMenuItemId) {
        MenuItem menuItem = getMenuItemById(iMenuItemId);
        MenuItemQueryResponseDTO responseDTO = new MenuItemQueryResponseDTO();

        BeanUtils.copyProperties(menuItem, responseDTO,"strMenuItemCategory");

        if(menuItem.getStriMenuItemCategory() != null) {
            String category = menuItem.getStriMenuItemCategory().name();
            responseDTO.setStrMenuItemCategory(category);
        }

        return responseDTO;
    }
}
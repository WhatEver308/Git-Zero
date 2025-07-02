package org.umlpractice.backend_fooddeliverysystem.DAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.umlpractice.backend_fooddeliverysystem.pojo.MenuItem;

import java.util.List;

/**
 * The interface Interface menu item dao.
 * <T,ID> implies operations on MenuItem pojo from Id:Integer
 * 提供菜品相关的数据库操作
 *
 * @author LiJunjie
 * @date 2025/5/30
 */
@Repository
public interface InterfaceMenuItemDAO extends CrudRepository<MenuItem, Integer> {

    /**
     * Find all menu items by merchant id.
     *
     * @param iMerchantId 商家 ID
     * @return 菜品列表
     */
    @Query("SELECT m FROM MenuItem m WHERE m.merchant.iMerchantId = :iMerchantId")
    public List<MenuItem> findMenuItemsByMerchantId(Integer iMerchantId);

    /**
         * Find menu item by ID.
         *
         * @param iMenuItemId 菜品 ID
         * @return 菜品对象
         */
        @Query("SELECT m FROM MenuItem m WHERE m.iMenuItemId = :iMenuItemId")
        public MenuItem findMenuItemById(Integer iMenuItemId);


}
package org.umlpractice.backend_fooddeliverysystem.DAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.umlpractice.backend_fooddeliverysystem.pojo.MenuItem;
import org.umlpractice.backend_fooddeliverysystem.pojo.Merchant;

import java.util.ArrayList;
import java.util.List;

/**
 * The interface Interface merchant dao.
 * <T,ID> implies operations on Merchant pojo from Id:Integer
 *
 * @author LiJunjie
 * @date 2025/5/30
 */
@Repository
public interface InterfaceMerchantDAO extends CrudRepository<Merchant, Integer> {

    /**
     * Find merchant by merchant name.
     *
     * @param strMerchantName the merchant name
     * @return the merchant
     */
    @Query("SELECT m FROM Merchant m WHERE m.strMerchantName = :strMerchantName")
    public Merchant findByMerchantName(String strMerchantName);

    /**
     * Find merchant by merchant id.
     *
     * @param iMerchantId the merchant id
     * @return the merchant
     */
    @Query("SELECT m FROM Merchant m WHERE m.iMerchantId = :iMerchantId")
    public Merchant findByMerchantId(Integer iMerchantId);

    @Query("SELECT 1 FROM Merchant m WHERE m.strMerchantName = :merchantName")
    public Boolean existsByMerchantName(@Param("merchantName")String merchant);
//    /**
//     * Find all menu items by merchant id.
//     *
//     * @param iMerchantId the merchant id
//     * @return the list of menu items
//     */
//    @Query("SELECT m FROM MenuItem m WHERE m.merchant.iMerchantId = :iMerchantId")
//    public List<MenuItem> findMenuItemsByMerchantId(Integer iMerchantId);
    @Query("SELECT m FROM Merchant m WHERE m.strMerchantCategory = :merchantCategory")
    public ArrayList<Merchant> findByMerchantCategory(@Param("merchantCategory")Merchant.StrMerchantCategory merchantCategory);

    @Query("SELECT m FROM Merchant m")
    public ArrayList<Merchant> findAllMerchants();
}
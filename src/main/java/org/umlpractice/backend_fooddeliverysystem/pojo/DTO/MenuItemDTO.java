package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;

import org.umlpractice.backend_fooddeliverysystem.pojo.MenuItem;

/**
 * MenuItemDTO 类说明
 * 用于菜品信息的传输对象
 * 包含菜品基本信息和相关字段
 *
 * @author LiJunjie
 * @date 2025/5/31
 */

public class MenuItemDTO {

    private Integer iMenuItemId;
    private String strMenuItemName;
    private String strMenuItemDescription;
    private Double dMenuItemPrice;
    private String strImageUrl;
    private String strMenuItemCategory;
    private Integer iMerchantId;

    // Getter 和 Setter 方法

    public Integer getiMenuItemId() {
        return iMenuItemId;
    }

    public void setiMenuItemId(Integer iMenuItemId) {
        this.iMenuItemId = iMenuItemId;
    }

    public String getStrMenuItemName() {
        return strMenuItemName;
    }

    public void setStrMenuItemName(String strMenuItemName) {
        this.strMenuItemName = strMenuItemName;
    }

    public String getStrMenuItemDescription() {
        return strMenuItemDescription;
    }

    public void setStrMenuItemDescription(String strMenuItemDescription) {
        this.strMenuItemDescription = strMenuItemDescription;
    }

    public Double getdMenuItemPrice() {
        return dMenuItemPrice;
    }

    public void setdMenuItemPrice(Double dMenuItemPrice) {
        this.dMenuItemPrice = dMenuItemPrice;
    }

    public String getStrImageUrl() {
        return strImageUrl;
    }

    public void setStrImageUrl(String strImageUrl) {
        this.strImageUrl = strImageUrl;
    }

    public String getStrMenuItemCategory() {
        return strMenuItemCategory;
    }

    public void setStrMenuItemCategory(String strMenuItemCategory) {
        this.strMenuItemCategory = strMenuItemCategory;
    }

    public Integer getiMerchantId() {
        return iMerchantId;
    }

    public void setiMerchantId(Integer iMerchantId) {
        this.iMerchantId = iMerchantId;
    }

    /**
     * 校验 MenuItemDTO 对象的有效性
     *
     * @return  boolean
     */
    public boolean isValidFORAdd() {
        return strMenuItemName != null && !strMenuItemName.isEmpty()
                && strMenuItemDescription != null && !strMenuItemDescription.isEmpty()
                && dMenuItemPrice != null
                && strImageUrl != null && !strImageUrl.isEmpty()
                && strMenuItemCategory != null && !strMenuItemCategory.isEmpty();
    }

    /**
     * 校验 MenuItemDTO 对象的有效性，用于更新操作
     *
     * @return  boolean
     */
    public boolean isValidForUpdate() {
        return iMenuItemId != null && iMenuItemId > 0
                && strMenuItemName != null && !strMenuItemName.isEmpty()
                && strMenuItemDescription != null && !strMenuItemDescription.isEmpty()
                && dMenuItemPrice != null
                && strImageUrl != null && !strImageUrl.isEmpty()
                && strMenuItemCategory != null && !strMenuItemCategory.isEmpty();
    }







    @Override
    public String toString() {
        return "MenuItemDTO{" +
                "iMenuItemId=" + iMenuItemId +
                ", strMenuItemName='" + strMenuItemName + '\'' +
                ", strMenuItemDescription='" + strMenuItemDescription + '\'' +
                ", dMenuItemPrice=" + dMenuItemPrice +
                ", strImageUrl='" + strImageUrl + '\'' +
                ", strMenuItemCategory='" + strMenuItemCategory + '\'' +
                ", iMerchantId=" + iMerchantId +
                '}';
    }
}
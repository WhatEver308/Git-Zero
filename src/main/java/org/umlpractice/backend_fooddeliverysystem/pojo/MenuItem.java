package org.umlpractice.backend_fooddeliverysystem.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * The type MenuItem.
 * Claims a SQL table: tb_menu_item with its instances in it.
 * iMenuItemId is the primary key.
 *
 * @author LiJunjie
 * @date 2025/5/29 21:05
 */
@Table(name = "tb_menu_item")
@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_id")
    private Integer iMenuItemId;

    @Column(name = "menu_item_name", nullable = false)
    private String strMenuItemName;

    @Column(name = "menu_item_description", nullable = false)
    private String strMenuItemDescription;

    @Column(name = "menu_item_price", nullable = false)
    private Double dMenuItemPrice;

    @Column(name = "image_url", nullable = false)
    private String strImageUrl;

    @Column(name = "dirty_bit", nullable = false)
    private Boolean bDirtyBit = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "menu_item_category", nullable = false)
    private StriMenuItemCategory striMenuItemCategory;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    @JsonBackReference
    private Merchant merchant;

    /**
     * Gets menu item id.
     *
     * @return the menu item id
     */
    public Integer getiMenuItemId() {
        return iMenuItemId;
    }

    /**
     * Sets menu item id.
     *
     * @param iMenuItemId the menu item id
     */
    public void setiMenuItemId(Integer iMenuItemId) {
        this.iMenuItemId = iMenuItemId;
    }

    /**
     * Gets menu item name.
     *
     * @return the menu item name
     */
    public String getStrMenuItemName() {
        return strMenuItemName;
    }

    /**
     * Sets menu item name.
     *
     * @param strMenuItemName the menu item name
     */
    public void setStrMenuItemName(String strMenuItemName) {
        this.strMenuItemName = strMenuItemName;
    }

    /**
     * Gets menu item description.
     *
     * @return the menu item description
     */
    public String getStrMenuItemDescription() {
        return strMenuItemDescription;
    }

    /**
     * Sets menu item description.
     *
     * @param strMenuItemDescription the menu item description
     */
    public void setStrMenuItemDescription(String strMenuItemDescription) {
        this.strMenuItemDescription = strMenuItemDescription;
    }

    /**
     * Gets menu item price.
     *
     * @return the menu item price
     */
    public Double getdMenuItemPrice() {
        return dMenuItemPrice;
    }

    /**
     * Sets menu item price.
     *
     * @param dMenuItemPrice the menu item price
     */
    public void setdMenuItemPrice(Double dMenuItemPrice) {
        this.dMenuItemPrice = dMenuItemPrice;
    }

    /**
     * Gets image url.
     *
     * @return the image url
     */
    public String getStrImageUrl() {
        return strImageUrl;
    }

    /**
     * Sets image url.
     *
     * @param strImageUrl the image url
     */
    public void setStrImageUrl(String strImageUrl) {
        this.strImageUrl = strImageUrl;
    }

    /**
     * Gets menu item category.
     *
     * @return the menu item category
     */
    public StriMenuItemCategory getStriMenuItemCategory() {
        return striMenuItemCategory;
    }

    /**
     * Sets menu item category.
     *
     * @param striMenuItemCategory the menu item category
     */
    public void setStriMenuItemCategory(StriMenuItemCategory striMenuItemCategory) {
        this.striMenuItemCategory = striMenuItemCategory;
    }

    /**
     * Gets merchant.
     *
     * @return the merchant
     */
    public Merchant getMerchant() {
        return merchant;
    }

    /**
     * Sets merchant.
     *
     * @param merchant the merchant
     */
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    /**
     * Gets dirty bit.
     *
     * @return the dirty bit
     */
    public Boolean getbDirtyBit() {
        return bDirtyBit;
    }

    /**
     * Sets dirty bit.
     *
     * @param dirtyBit the dirty bit
     */
    public void setbDirtyBit(Boolean dirtyBit) {
        this.bDirtyBit = dirtyBit;
    }


    @Override
    public String toString() {
        return "MenuItem{" +
                "iMenuItemId=" + iMenuItemId +
                ", strMenuItemName='" + strMenuItemName + '\'' +
                ", strMenuItemDescription='" + strMenuItemDescription + '\'' +
                ", dMenuItemPrice=" + dMenuItemPrice +
                ", strImageUrl='" + strImageUrl + '\'' +
                ", striMenuItemCategory=" + striMenuItemCategory +
                '}';
    }

    public enum  StriMenuItemCategory {
        小炒("小炒"),
        推荐("推荐"),
        海鲜("海鲜"),
        湘菜("湘菜"),
        炖汤("炖汤"),
        点心("点心"),
        甜品("甜品"),
        粤菜("粤菜"),
        粥("粥"),
        其它("其它"),
        饮料("饮料");

        private final String category;

        StriMenuItemCategory(String category) {
            this.category = category;
        }

        public String getCategory() {
            return category;
        }
    }
}


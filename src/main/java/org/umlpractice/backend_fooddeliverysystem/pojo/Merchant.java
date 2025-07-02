package org.umlpractice.backend_fooddeliverysystem.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Merchant.
 * Claims a SQL table: tb_merchant with its instances in it.
 * iMerchantId is the primary key.
 *
 * @author LiJunjie
 * @date 2025/5/29 22:00
 */
@Table(name = "tb_merchant")
@Entity
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private Integer iMerchantId;

    @Column(name = "merchant_name", nullable = false)
    private String strMerchantName;

    @Column(name = "average_price")
    private Double dAveragePrice;

    @Column(name = "delivery_fee")
    private Double dDeliveryFee;

    @Column(name = "min_order")
    private Double dMinOrder;

    @Column(name = "cover_image_url")
    private String strCoverImageUrl;

    @Column(name = "logo_url")
    private String strLogoUrl;

    @Column(name = "rating")
    private Double dRating;

    @Column(name = "delivery_time")
    private Double dDeliveryTime;

    @Column(name = "password")
    private String strPassword;

    @Column(name = "phone")
    private String strPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "merchant_category")
    private Merchant.StrMerchantCategory strMerchantCategory;

    @OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY)    // 商家删除后，菜品仍然保留
    @JsonManagedReference
    private List<MenuItem> cuisine;

    @OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DeliveryOrder> deliveryOrders;

    /**
     * Gets merchant id.
     *
     * @return the merchant id
     */
    public Integer getiMerchantId() {
        return iMerchantId;
    }

    /**
     * Sets merchant id.
     *
     * @param iMerchantId the merchant id
     */
    public void setiMerchantId(Integer iMerchantId) {
        this.iMerchantId = iMerchantId;
    }

    /**
     * Gets merchant name.
     *
     * @return the merchant name
     */
    public String getStrMerchantName() {
        return strMerchantName;
    }

    /**
     * Sets merchant name.
     *
     * @param strMerchantName the merchant name
     */
    public void setStrMerchantName(String strMerchantName) {
        this.strMerchantName = strMerchantName;
    }

    /**
     * Gets average price.
     *
     * @return the average price
     */
    public Double getdAveragePrice() {
        return dAveragePrice;
    }

    /**
     * Sets average price.
     *
     * @param dAveragePrice the average price
     */
    public void setdAveragePrice(Double dAveragePrice) {
        this.dAveragePrice = dAveragePrice;
    }

    /**
     * Gets delivery fee.
     *
     * @return the delivery fee
     */
    public Double getdDeliveryFee() {
        return dDeliveryFee;
    }

    /**
     * Sets delivery fee.
     *
     * @param dDeliveryFee the delivery fee
     */
    public void setdDeliveryFee(Double dDeliveryFee) {
        this.dDeliveryFee = dDeliveryFee;
    }

    /**
     * Gets min order.
     *
     * @return the min order
     */
    public Double getdMinOrder() {
        return dMinOrder;
    }

    /**
     * Sets min order.
     *
     * @param dMinOrder the min order
     */
    public void setdMinOrder(Double dMinOrder) {
        this.dMinOrder = dMinOrder;
    }

    /**
     * Gets cover image url.
     *
     * @return the cover image url
     */
    public String getStrCoverImageUrl() {
        return strCoverImageUrl;
    }

    /**
     * Sets cover image url.
     *
     * @param strCoverImageUrl the cover image url
     */
    public void setStrCoverImageUrl(String strCoverImageUrl) {
        this.strCoverImageUrl = strCoverImageUrl;
    }

    /**
     * Gets logo url.
     *
     * @return the logo url
     */
    public String getStrLogoUrl() {
        return strLogoUrl;
    }

    /**
     * Sets logo url.
     *
     * @param strLogoUrl the logo url
     */
    public void setStrLogoUrl(String strLogoUrl) {
        this.strLogoUrl = strLogoUrl;
    }

    /**
     * Gets rating.
     *
     * @return the rating
     */
    public Double getdRating() {
        return dRating;
    }

    /**
     * Sets rating.
     *
     * @param dRating the rating
     */
    public void setdRating(Double dRating) {
        this.dRating = dRating;
    }

    /**
     * Gets delivery time.
     *
     * @return the delivery time
     */
    public Double getdDeliveryTime() {
        return dDeliveryTime;
    }

    /**
     * Sets delivery time.
     *
     * @param tDeliveryTime the delivery time
     */
    public void setdDeliveryTime(Double tDeliveryTime) {
        this.dDeliveryTime = tDeliveryTime;
    }

    /**
     * Gets cuisine.
     *
     * @return the cuisine
     */
    public List<MenuItem> getCuisine() {
        return cuisine;
    }

    /**
     * Sets cuisine.
     *
     * @param cuisine the cuisine
     */
    public void setCuisine(List<MenuItem> cuisine) {
        this.cuisine = cuisine;
    }

    /**
     * Gets delivery orders.
     *
     * @return the delivery orders
     */
    public List<DeliveryOrder> getDeliveryOrders() {
        return deliveryOrders;
    }

    /**
     * Sets delivery orders.
     *
     * @param deliveryOrders the delivery orders
     */
    public void setDeliveryOrders(List<DeliveryOrder> deliveryOrders) {
        this.deliveryOrders = deliveryOrders;
    }

    public void setStrMerchantCategory(Merchant.StrMerchantCategory strMerchantCategory) {
        this.strMerchantCategory = strMerchantCategory;
    }

    public Merchant.StrMerchantCategory getStrMerchantCategory() {
        return strMerchantCategory;
    }

    /**
     * Adds a delivery order to the merchant.
     *
     * @param deliveryOrder the delivery order to add
     */
    public void addDeliveryOrder(DeliveryOrder deliveryOrder) {
        if (this.deliveryOrders == null) {
            this.deliveryOrders = new ArrayList<>();
        }
        this.deliveryOrders.add(deliveryOrder);
        deliveryOrder.setMerchant(this); // Set the merchant in the DeliveryOrder
    }

    /**
     * Adds a menu item to the merchant's cuisine.
     * This method initializes the cuisine list if it is null and adds the specified menu item to it.
     *
     * @param menuItem the menu item to be added
     */
    public void addMenuItem(MenuItem menuItem) {
        if (this.cuisine == null) {
            this.cuisine = new ArrayList<>();
        }
        this.cuisine.add(menuItem);
    }

    /**
     * Removes a menu item from the merchant's cuisine.
     * This method checks if the cuisine list is not null and removes the specified menu item from it.
     *
     * @param iMerchantId the ID of the menu item to be removed
     */
    public void removeMenuItem(Integer iMerchantId){
        if (this.cuisine == null) {
            throw new IllegalArgumentException("Cuisine list is empty, cannot remove menu item.");
        }
        try {
            this.cuisine.removeIf(menuItem -> menuItem.getiMenuItemId().equals(iMerchantId));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error removing menu item with ID " + iMerchantId + ": " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "iMerchantId=" + iMerchantId +
                ", strMerchantName='" + strMerchantName + '\'' +
                ", dAveragePrice=" + dAveragePrice +
                ", dDeliveryFee=" + dDeliveryFee +
                ", dMinOrder=" + dMinOrder +
                ", strCoverImageUrl='" + strCoverImageUrl + '\'' +
                ", strLogoUrl='" + strLogoUrl + '\'' +
                ", strRating='" + dRating + '\'' +
                ", tDeliveryTime=" + dDeliveryTime +
                ", strPassword='" + strPassword + '\'' +
                ", strPhone='" + strPhone + '\'' +
                ", cuisine=" + cuisine +
                ", deliveryOrders=" + deliveryOrders +
                ", strMerchantCategory=" + strMerchantCategory +
                '}';
    }

    public String getStrPhone() {
        return strPhone;
    }

    public void setStrPhone(String strPhone) {
        this.strPhone = strPhone;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    /**
     * The enum Str merchant category.
     */
    public enum StrMerchantCategory {
        湘菜("湘菜"),
        炖汤("炖汤"),
        点心("点心"),
        甜品("甜品"),
        粤菜("粤菜");

        public final String strCategory;
        StrMerchantCategory(String strCategory) {
            this.strCategory = strCategory;
        }
        public String getStrCategory() {
            return strCategory;
        }
    }
}
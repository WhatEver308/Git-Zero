package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;

/**
 * MerchantDTO 类说明
 * 用于商家信息的传输对象
 * 包含商家基本信息和相关字段
 *
 * @author LiJunjie
 * @date 2025 /5/30
 */
public class MerchantDTO {

    private Integer iMerchantId;
    private String strMerchantName;
    private Double dAveragePrice;
    private Double dDeliveryFee;
    private Double dMinOrder;
    private String strCoverImageUrl;
    private String strLogoUrl;
    private Double dRating;
    private Double dDeliveryTime;
    private String strPassword;
    private String strPhone;

    @Override
    public String toString() {
        return "MerchantDTO{" +
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
                '}';
    }

    public String getStrPhone() {
        return strPhone;
    }

    public void setStrPhone(String strPhone) {
        this.strPhone = strPhone;
    }

    /**
     * 获取商家 ID
     *
     * @return 商家 ID
     */
    public Integer getiMerchantId() {
        return iMerchantId;
    }

    /**
     * 设置商家 ID
     *
     * @param iMerchantId 商家 ID
     */
    public void setiMerchantId(Integer iMerchantId) {
        this.iMerchantId = iMerchantId;
    }

    /**
     * 获取商家名称
     *
     * @return 商家名称 str merchant name
     */
    public String getStrMerchantName() {
        return strMerchantName;
    }

    /**
     * 设置商家名称
     *
     * @param strMerchantName 商家名称
     */
    public void setStrMerchantName(String strMerchantName) {
        this.strMerchantName = strMerchantName;
    }

    /**
     * 获取平均价格
     *
     * @return 平均价格 average price
     */
    public Double getdAveragePrice() {
        return dAveragePrice;
    }

    /**
     * 设置平均价格
     *
     * @param dAveragePrice 平均价格
     */
    public void setdAveragePrice(Double dAveragePrice) {
        this.dAveragePrice = dAveragePrice;
    }

    /**
     * 获取配送费
     *
     * @return 配送费 delivery fee
     */
    public Double getdDeliveryFee() {
        return dDeliveryFee;
    }

    /**
     * 设置配送费
     *
     * @param dDeliveryFee 配送费
     */
    public void setdDeliveryFee(Double dDeliveryFee) {
        this.dDeliveryFee = dDeliveryFee;
    }

    /**
     * 获取最低订单金额
     *
     * @return 最低订单金额 min order
     */
    public Double getdMinOrder() {
        return dMinOrder;
    }

    /**
     * 设置最低订单金额
     *
     * @param dMinOrder 最低订单金额
     */
    public void setdMinOrder(Double dMinOrder) {
        this.dMinOrder = dMinOrder;
    }

    /**
     * 获取封面图片 URL
     *
     * @return 封面图片 URL
     */
    public String getStrCoverImageUrl() {
        return strCoverImageUrl;
    }

    /**
     * 设置封面图片 URL
     *
     * @param strCoverImageUrl 封面图片 URL
     */
    public void setStrCoverImageUrl(String strCoverImageUrl) {
        this.strCoverImageUrl = strCoverImageUrl;
    }

    /**
     * 获取商家 Logo URL
     *
     * @return 商家 Logo URL
     */
    public String getStrLogoUrl() {
        return strLogoUrl;
    }

    /**
     * 设置商家 Logo URL
     *
     * @param strLogoUrl 商家 Logo URL
     */
    public void setStrLogoUrl(String strLogoUrl) {
        this.strLogoUrl = strLogoUrl;
    }

    /**
     * 获取商家评分
     *
     * @return 商家评分 str rating
     */
    public Double getdRating() {
        return dRating;
    }

    /**
     * 设置商家评分
     *
     * @param dRating 商家评分
     */
    public void setdRating(Double dRating) {
        this.dRating = dRating;
    }

    /**
     * 获取配送时间
     *
     * @return 配送时间 delivery time
     */
    public Double getdDeliveryTime() {
        return dDeliveryTime;
    }

    /**
     * 设置配送时间
     *
     * @param dDeliveryTime 配送时间
     */
    public void setdDeliveryTime(Double dDeliveryTime) {
        this.dDeliveryTime = dDeliveryTime;
    }

    /**
     * Gets str password.
     *
     * @return the str password
     */
    public String getStrPassword() {
        return strPassword;
    }

    /**
     * Sets str password.
     *
     * @param strPassword the str password
     */
    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }
}
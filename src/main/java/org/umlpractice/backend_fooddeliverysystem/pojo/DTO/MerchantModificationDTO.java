package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;


/**
 * MerchantModificationDTO 类说明
 *
 * @author 刘陈文君
 * @date 2025/7/1 15:31
 */
public class MerchantModificationDTO extends MerchantDTO{
    private String strMerchantCategory;

    @Override
    public String toString() {
        return "MerchantModificationDTO{" +
                "strMerchantCategory='" + strMerchantCategory + '\'' +
                '}';
    }

    public String getStrMerchantCategory() {
        return strMerchantCategory;
    }

    public void setStrMerchantCategory(String strMerchantCategory) {
        this.strMerchantCategory = strMerchantCategory;
    }
}

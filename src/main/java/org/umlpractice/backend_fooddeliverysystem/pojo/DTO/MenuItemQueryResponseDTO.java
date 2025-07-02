package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;

public class MenuItemQueryResponseDTO {
    private Integer iMenuItemId;
    private String strMenuItemName;
    private Double dMenuItemPrice;
    private String strMenuItemCategory;
    private String strImageUrl;
    private String strMenuItemDescription;

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

    public Double getdMenuItemPrice() {
        return dMenuItemPrice;
    }

    public void setdMenuItemPrice(Double dMenuItemPrice) {
        this.dMenuItemPrice = dMenuItemPrice;
    }

    public String getStrMenuItemCategory() {
        return strMenuItemCategory;
    }

    public void setStrMenuItemCategory(String strMenuItemCategory) {
        this.strMenuItemCategory = strMenuItemCategory;
    }

    public String getStrImageUrl() {
        return strImageUrl;
    }

    public void setStrImageUrl(String strImageUrl) {
        this.strImageUrl = strImageUrl;
    }

    public String getStrMenuItemDescription() {
        return strMenuItemDescription;
    }

    public void setStrMenuItemDescription(String strMenuItemDescription) {
        this.strMenuItemDescription = strMenuItemDescription;
    }
}

package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItemDTO {
    @JsonProperty("iMenuItemId")
    private Integer iMenuItemId;

    private Integer iQuantity;

    public Integer getiMenuItemId() {
        return iMenuItemId;
    }

    public void setiMenuItemId(Integer iMenuItemId) {
        this.iMenuItemId = iMenuItemId;
    }

    public Integer getiQuantity() {
        return iQuantity;
    }

    public void setiQuantity(Integer iQuantity) {
        this.iQuantity = iQuantity;
    }

    public class MenuItemFOROrderDTO {
        private Integer iMenuItemId;
        private String strMenuItemName;
        private String strMenuItemDescription;
        private Double dMenuItemPrice;
        private String strImageUrl;
        private String strMenuItemCategory;

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

        @Override
        public String toString() {
            return "OrderItemDTO{" +
                    "strMenuItemName='" + strMenuItemName + '\'' +
                    ", strMenuItemDescription='" + strMenuItemDescription + '\'' +
                    ", dMenuItemPrice=" + dMenuItemPrice +
                    ", strImageUrl='" + strImageUrl + '\'' +
                    ", strMenuItemCategory='" + strMenuItemCategory + '\'' +
                    '}';
        }

    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "iMenuItemId=" + iMenuItemId +
                ", iQuantity=" + iQuantity +
                '}';
    }
}

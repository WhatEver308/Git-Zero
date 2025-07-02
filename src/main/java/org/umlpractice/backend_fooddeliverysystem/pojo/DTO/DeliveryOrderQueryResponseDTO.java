package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DeliveryOrderQueryResponseDTO {
    private Double dDeliverFee;
    private Double dTotalPrice;
    private Integer iMerchantId;
    private Integer iOrderId;
    private Integer iUserId;
    private String strCreatedAt;
    private String strStatus;

    @JsonProperty("objAddress")
    private AddressDTO addressDTO;

    @JsonProperty("orderItemList")
    private List<OrderItemDTO> orderItemDTOList;

    public void addOrderItemDTO(MenuItemQueryResponseDTO menuItem, Integer iQuantity) {
        if(this.orderItemDTOList == null) {
            this.orderItemDTOList = new java.util.ArrayList<>();
        }
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setMenuItem(menuItem);
        orderItemDTO.setiQuantity(iQuantity);
        this.orderItemDTOList.add(orderItemDTO);
    }


    public static class OrderItemDTO {
        private Integer iQuantity;
        private MenuItemQueryResponseDTO menuItemQueryResponseDTO;

        public Integer getiQuantity() { return iQuantity; }
        public void setiQuantity(Integer iQuantity) { this.iQuantity = iQuantity; }

        public MenuItemQueryResponseDTO getMenuItem() { return menuItemQueryResponseDTO; }
        public void setMenuItem(MenuItemQueryResponseDTO menuItemQueryResponseDTO) { this.menuItemQueryResponseDTO = menuItemQueryResponseDTO; }
    }

    @Override
    public String toString() {
        return "DeliveryOrderQueryResponseDTO{" +
                ", dDeliverFee=" + dDeliverFee +
                ", dTotalPrice=" + dTotalPrice +
                ", iMerchantId=" + iMerchantId +
                ", iOrderId=" + iOrderId +
                ", iUserId=" + iUserId +
                ", strCreatedAt='" + strCreatedAt + '\'' +
                ", strStatus='" + strStatus + '\'' +
                ", addressDTO=" + addressDTO +
                ", orderItemDTOList=" + orderItemDTOList +
                '}';
    }


    public Double getdDeliverFee() {
        return dDeliverFee;
    }

    public void setdDeliverFee(Double dDeliverFee) {
        this.dDeliverFee = dDeliverFee;
    }

    public Double getdTotalPrice() {
        return dTotalPrice;
    }

    public void setdTotalPrice(Double dTotalPrice) {
        this.dTotalPrice = dTotalPrice;
    }

    public Integer getiMerchantId() {
        return iMerchantId;
    }

    public void setiMerchantId(Integer iMerchantId) {
        this.iMerchantId = iMerchantId;
    }

    public Integer getiOrderId() {
        return iOrderId;
    }

    public void setiOrderId(Integer iOrderId) {
        this.iOrderId = iOrderId;
    }

    public Integer getiUserId() {
        return iUserId;
    }

    public void setiUserId(Integer iUserId) {
        this.iUserId = iUserId;
    }

    public String getStrCreatedAt() {
        return strCreatedAt;
    }

    public void setStrCreatedAt(String strCreatedAt) {
        this.strCreatedAt = strCreatedAt;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
    }

    public List<OrderItemDTO> getOrderItemDTOList() {
        return orderItemDTOList;
    }


    public void setOrderItemDTOList(List<OrderItemDTO> orderItemDTOList) {
        this.orderItemDTOList = orderItemDTOList;
    }
}

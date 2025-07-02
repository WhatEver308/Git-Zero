package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;

import java.util.List;

/**
 * 订单传输对象
 * 用于下单请求的数据传输
 */
public class DeliveryOrderDTO {
    private AddressDTO address;           // 收货地址信息
    private Integer iMerchantId;          // 商家ID
    private Integer iUserId;              // 用户ID
    private List<OrderItemDTO> orderItemList; // 订单项列表

    // Getter 和 Setter 方法
    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public Integer getiMerchantId() {
        return iMerchantId;
    }

    public void setiMerchantId(Integer iMerchantId) {
        this.iMerchantId = iMerchantId;
    }

    public Integer getiUserId() {
        return iUserId;
    }

    public void setiUserId(Integer iUserId) {
        this.iUserId = iUserId;
    }

    public List<OrderItemDTO> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemDTO> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "DeliveryOrderDTO{" +
                "address=" + address +
                ", iMerchantId=" + iMerchantId +
                ", iUserId=" + iUserId +
                ", orderItemList=" + orderItemList +
                '}';
    }

    /**
     * 验证下单请求的有效性
     */
    public boolean isValidForCreate() {
        return address != null && address.isValidForOrderCreate() &&
                iMerchantId != null && iMerchantId > 0 &&
                iUserId != null && iUserId > 0 &&
                orderItemList != null && !orderItemList.isEmpty();
    }
}
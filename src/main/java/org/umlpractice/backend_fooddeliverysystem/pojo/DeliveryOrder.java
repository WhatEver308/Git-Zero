package org.umlpractice.backend_fooddeliverysystem.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The type DeliveryOrder.
 * Claims a SQL table: tb_delivery_order with its instances in it.
 * iOrderId is the primary key.
 *
 * @author 刘陈文君
 * @date 2025/5/30 10:00
 */
@Table(name = "tb_delivery_order")
@Entity
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer iOrderId;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address addAddress;

    @Column(name = "delivery_fee", nullable = false)
    private Double dDeliverFee;

    @Column(name = "total_price", nullable = false)
    private Double dTotalPrice;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    @JsonBackReference
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(name = "created_at", nullable = false)
    private String strCreatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StrStatus strStatus;

    @OneToMany(mappedBy = "deliveryOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
    /**
     * Gets order id.
     *
     * @return the order id
     */
    public Integer getiOrderId() {
        return iOrderId;
    }

    /**
     * Sets order id.
     *
     * @param iOrderId the order id
     */
    public void setiOrderId(Integer iOrderId) {
        this.iOrderId = iOrderId;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public Address getAddAddress() {
        return addAddress;
    }

    /**
     * Sets address.
     *
     * @param addAddress the address
     */
    public void setAddAddress(Address addAddress) {
        this.addAddress = addAddress;
    }

    /**
     * Gets delivery fee.
     *
     * @return the delivery fee
     */
    public Double getdDeliverFee() {
        return dDeliverFee;
    }

    /**
     * Sets delivery fee.
     *
     * @param dDeliverFee the delivery fee
     */
    public void setdDeliverFee(Double dDeliverFee) {
        this.dDeliverFee = dDeliverFee;
    }

    /**
     * Gets total price.
     *
     * @return the total price
     */
    public Double getdTotalPrice() {
        return dTotalPrice;
    }

    /**
     * Sets total price.
     *
     * @param dTotalPrice the total price
     */
    public void setdTotalPrice(Double dTotalPrice) {
        this.dTotalPrice = dTotalPrice;
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
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets created at.
     *
     * @return the created at
     */
    public String getStrCreatedAt() {
        return strCreatedAt;
    }

    /**
     * Sets created at.
     *
     * @param strCreatedAt the created at
     */
    public void setStrCreatedAt(String strCreatedAt) {
        this.strCreatedAt = strCreatedAt;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public StrStatus getStrStatus() {
        return strStatus;
    }

    /**
     * Sets status.
     *
     * @param strStatus the status
     */
    public void setStrStatus(StrStatus strStatus) {
        this.strStatus = strStatus;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStrStatus(String status) {
        try {
            this.strStatus = StrStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    /**
     * Gets order items.
     *
     * @return the order items
     */
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    /**
     * Sets order items.
     *
     * @param orderItems the order items
     */
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        if (orderItems == null) {
           this.orderItems = new ArrayList<>();
        }
        this.orderItems.add(orderItem);
        orderItem.setDeliveryOrder(this); // 设置双向关联
    }

    /**
     * To string.
     *
     * @return the string representation of DeliveryOrder
     */
    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "iOrderId=" + iOrderId +
                ", addAddress=" + addAddress +
                ", dDeliverFee=" + dDeliverFee +
                ", dTotalPrice=" + dTotalPrice +
                ", merchant=" + merchant +
                ", strCreatedAt='" + strCreatedAt + '\'' +
                ", strStatus=" + strStatus +
                ", orderItems=" + orderItems +
                '}';
    }


    public enum StrStatus {
        Cancelled("cancelled"),
        Confirmed("confirmed"),
        Delivered("delivered"),
        Delivering("delivering"),
        Pending("pending"),
        Preparing("preparing");

        private final String status;

        StrStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
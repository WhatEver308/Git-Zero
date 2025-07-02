package org.umlpractice.backend_fooddeliverysystem.pojo;

import jakarta.persistence.*;

/**
 * The type OrderItem.
 * Claims a SQL table: tb_order_item with its instances in it.
 * iOrderItemId is the primary key.
 *
 * @author LiJunjie
 * @date 2025/5/30 11:00
 */
@Table(name = "tb_order_item")
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer iOrderItemId;

    @Column(name = "quantity", nullable = false)
    private Integer iQuantity;

    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;
    // 就算menuitem已经被修改了,当下的这个menuitem是以往menuitem的副本
    // 要求menuitem在修改时改用新的menuitem_id,在删除时也只是从商家menu中删除该菜品,不在服务器中删除副本

    @Column(name = "price_at_order", nullable = false)
    private Double dPriceAtOrder;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private DeliveryOrder deliveryOrder;

    /**
     * Gets order item id.
     *
     * @return the order item id
     */
    public Integer getiOrderItemId() {
        return iOrderItemId;
    }

    /**
     * Sets order item id.
     *
     * @param iOrderItemId the order item id
     */
    public void setiOrderItemId(Integer iOrderItemId) {
        this.iOrderItemId = iOrderItemId;
    }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public Integer getiQuantity() {
        return iQuantity;
    }

    /**
     * Sets quantity.
     *
     * @param iQuantity the quantity
     */
    public void setiQuantity(Integer iQuantity) {
        this.iQuantity = iQuantity;
    }

    /**
     * Gets menu item.
     *
     * @return the menu item
     */
    public MenuItem getMenuItem() {
        return menuItem;
    }

    /**
     * Sets menu item.
     *
     * @param menuItem the menu item
     */
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    /**
     * Gets price at order.
     *
     * @return the price at order
     */
    public Double getdPriceAtOrder() {
        return dPriceAtOrder;
    }

    /**
     * Sets price at order.
     *
     * @param dPriceAtOrder the price at order
     */
    public void setdPriceAtOrder(Double dPriceAtOrder) {
        this.dPriceAtOrder = dPriceAtOrder;
    }

    /**
     * Gets delivery order.
     *
     * @return the delivery order
     */
    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    /**
     * Sets delivery order.
     *
     * @param deliveryOrder the delivery order
     */
    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "iOrderItemId=" + iOrderItemId +
                ", iQuantity=" + iQuantity +
                ", menuItem=" + menuItem +
                ", dPriceAtOrder=" + dPriceAtOrder +
                '}';
    }
}
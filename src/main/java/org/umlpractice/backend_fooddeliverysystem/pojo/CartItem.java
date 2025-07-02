package org.umlpractice.backend_fooddeliverysystem.pojo;

import jakarta.persistence.*;

/**
 * The type CartItem.
 * Claims a SQL table: tb_cart_item with its instances in it.
 * iCartItemId is the primary key.
 *
 * @author LiJunjie
 * @date 2025/5/29 22:00
 */


// !!!!!请注意，该类已经废弃!!!!!
@Table(name = "tb_cart_item")
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Integer iCartItemId;

    @Column(name = "quantity", nullable = false)
    private Integer iQuantity;

    @ManyToOne  //这里要使用manytoone，因为一个menuitem可能被多个cartitem使用
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    /**
     * Gets cart item id.
     *
     * @return the cart item id
     */
    public Integer getiCartItemId() {
        return iCartItemId;
    }

    /**
     * Sets cart item id.
     *
     * @param iCartItemId the cart item id
     */
    public void setiCartItemId(Integer iCartItemId) {
        this.iCartItemId = iCartItemId;
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

    @Override
    public String toString() {
        return "CartItem{" +
                "iCartItemId=" + iCartItemId +
                ", iQuantity=" + iQuantity +
                ", menuItem=" + menuItem +
                '}';
    }
}
package org.umlpractice.backend_fooddeliverysystem.pojo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The type User.
 * claims a sql table:tb_user with its instances in it
 * iUserId is first key
 * @author 刘陈文君
 * @date 2025 /5/28 18:01
 */
@Table(name="tb_user")
@Entity

public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer iUserId;

    @Column(name="user_name")
    private String strUserName;

    @Column(name="user_password")
    private String strPassword;

    @Column(name="user_email")
    private String strEmail;

    @Column(name="user_phone")
    private String strPhone;

    @Column(name="user_category")
    private String strUserCategory;
    //用户的地址簿使用userId来进行查询
    //用户的类别为String类型
    @Column(name="user_gender")
    private String strUserGender;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY) //地址簿需要保留
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)   //历史订单需要保留
    @JsonManagedReference
    private List<DeliveryOrder> deliveryOrders;


    /**
     * Gets user id.
     *
     * @return the user id
     */
    public Integer getiUserId() {
        return iUserId;
    }

    /**
     * Sets user id.
     *
     * @param iUserId the user id
     */
    public void setiUserId(Integer iUserId) {
        this.iUserId = iUserId;
    }

    /**
     * Gets str user name.
     *
     * @return the str user name
     */
    public String getStrUserName() {
        return strUserName;
    }

    /**
     * Sets str user name.
     *
     * @param strUserName the str user name
     */
    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
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

    /**
     * Gets str email.
     *
     * @return the str email
     */
    public String getStrEmail() {
        return strEmail;
    }

    /**
     * Sets str email.
     *
     * @param strEmail the str email
     */
    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    /**
     * Gets str phone.
     *
     * @return the str phone
     */
    public String getStrPhone() {
        return strPhone;
    }

    /**
     * Sets str phone.
     *
     * @param strPhone the str phone
     */
    public void setStrPhone(String strPhone) {
        this.strPhone = strPhone;
    }

    /**
     * Gets str user category.
     *
     * @return the str user category
     */
    public String getStrUserCategory() {
        return strUserCategory;
    }

    /**
     * Sets str user category.
     *
     * @param strUserCategory the str user category
     */
    public void setStrUserCategory(String strUserCategory) {
        this.strUserCategory = strUserCategory;
    }

    /**
     * Gets str user gender.
     *
     * @return the str user gender
     */
    public String getStrUserGender() {
        return strUserGender;
    }

    /**
     * Sets str user gender.
     *
     * @param strUserGender the str user gender
     */
    public void setStrUserGender(String strUserGender) {
        this.strUserGender = strUserGender;
    }

    /**
     * Gets addresses.
     *
     * @return the addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * Sets addresses.
     *
     * @param addresses the addresses
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * Sets default address.
     * This method sets the specified address as the default address in the user's address list.
     * If the address is found in the list, its 'bDefault' property is set to true, and all other addresses are set to false.
     *
     * @param addressid the address to be set as default
     */
    public void setDefaultAddress(Integer addressid){
        boolean found = false;
        Address originalDefaultAddress = null;

        if (addresses == null || addresses.isEmpty()) {
            return; // No addresses to set as default
        }

        for (Address address : addresses) {
            if (address.getIAddressId().equals(addressid)) {
                address.setbDefault(true);
                found = true;
            } else {
                address.setbDefault(false);
            }
            if (address.getbDefault()) {
                originalDefaultAddress = address;
            }
        }

        if (!found) {
            if (originalDefaultAddress != null) {
                originalDefaultAddress.setbDefault(true); // Reset the original default address if not found
            }
            throw new IllegalArgumentException("Address with id " + addressid + " not found in user's address list.");
        }
    }

    /**
     * Sets default address.
     * This method sets the specified address as the default address in the user's address list.
     * Attention: This function is only be used when the address is not yet has addressId
     *
     * @param address the address to be set as default
     */
    public void setDefaultAddress(Address address) {
        if (address == null ) {
            throw new IllegalArgumentException("Address cannot be null ");
        }
        if(!addresses.contains(address)) {
            throw new IllegalArgumentException("Address does not belong to this user.");
        }
        for(Address addr : addresses) {
            if (addr.equals(address)) {
                addr.setbDefault(true);
            } else {
                addr.setbDefault(false);
            }
        }
    }

    public void addAddress(Address address) {
        if (this.addresses == null) {
            this.addresses = new ArrayList<>();
        }
        this.addresses.add(address);
    }

    public void removeAddress(Integer addressId) {
        if (this.addresses == null) {
            throw new IllegalArgumentException("Address list is empty, cannot remove address.");
        }
        try {
            this.addresses.removeIf(address -> address.getIAddressId().equals(addressId));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error removing address with ID " + addressId + ": " + e.getMessage());
        }
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

    /**
     * Adds a delivery order to the user's list of delivery orders.
     * This method initializes the deliveryOrders list if it is null and adds the specified delivery order to it.
     * It also sets the user in the DeliveryOrder object to maintain the relationship.
     *
     * @param deliveryOrder the delivery order to be added
     */
    public void addDeliveryOrder(DeliveryOrder deliveryOrder) {
        if (this.deliveryOrders == null) {
            this.deliveryOrders = new ArrayList<>();
        }
        this.deliveryOrders.add(deliveryOrder);
        deliveryOrder.setUser(this); // Set the user in the DeliveryOrder
    }

    @Override
    public String toString() {
        return "User{" +
                "iUserId=" + iUserId +
                ", strUserName='" + strUserName + '\'' +
                ", strPassword='" + strPassword + '\'' +
                ", strEmail='" + strEmail + '\'' +
                ", strPhone='" + strPhone + '\'' +
                ", strUserCategory='" + strUserCategory + '\'' +
                ", strUserGender='" + strUserGender + '\'' +
                '}';
    }
}

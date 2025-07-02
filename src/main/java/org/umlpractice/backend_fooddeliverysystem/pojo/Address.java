package org.umlpractice.backend_fooddeliverysystem.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * The type Address.
 * Claims a SQL table: tb_address with its instances in it.
 * strAddressId is the primary key.
 *
 * @author LiJunjie
 * @date 2025/5/29 21:48
 */

@Table(name = "tb_address")
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer iAddressId;

    @Column(name = "address", nullable = false)
    private String strAddress;

    @Column(name = "city", nullable = false)
    private String strCity;

    @Column(name = "district", nullable = false)
    private String strDistrict;

    @Column(name = "phone", nullable = false)
    private String strPhone;

    @Column(name = "province", nullable = false)
    private String strProvince;

    @Column(name = "recipient_name", nullable = false)
    private String strRecipientName;

    @Column(name = "is_default", nullable = false)
    private Boolean bDefault;

    @Column(name = "dirty_bit", nullable = false)
    private Boolean bDirtyBit = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    /**
     * Gets str address id.
     *
     * @return the str address id
     */
    public Integer getIAddressId() {
        return iAddressId;
    }

    /**
     * Sets str address id.
     *
     * @param strAddressId the str address id
     */
    public void setIAddressId(Integer strAddressId) {
        this.iAddressId = strAddressId;
    }

    /**
     * Gets str address.
     *
     * @return the str address
     */
    public String getStrAddress() {
        return strAddress;
    }

    /**
     * Sets str address.
     *
     * @param strAddress the str address
     */
    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    /**
     * Gets str city.
     *
     * @return the str city
     */
    public String getStrCity() {
        return strCity;
    }

    /**
     * Sets str city.
     *
     * @param strCity the str city
     */
    public void setStrCity(String strCity) {
        this.strCity = strCity;
    }

    /**
     * Gets str district.
     *
     * @return the str district
     */
    public String getStrDistrict() {
        return strDistrict;
    }

    /**
     * Sets str district.
     *
     * @param strDistrict the str district
     */
    public void setStrDistrict(String strDistrict) {
        this.strDistrict = strDistrict;
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
     * Gets str province.
     *
     * @return the str province
     */
    public String getStrProvince() {
        return strProvince;
    }

    /**
     * Sets str province.
     *
     * @param strProvince the str province
     */
    public void setStrProvince(String strProvince) {
        this.strProvince = strProvince;
    }

    /**
     * Gets str recipient name.
     *
     * @return the str recipient name
     */
    public String getStrRecipientName() {
        return strRecipientName;
    }

    /**
     * Sets str recipient name.
     *
     * @param strRecipientName the str recipient name
     */
    public void setStrRecipientName(String strRecipientName) {
        this.strRecipientName = strRecipientName;
    }

    /**
     * Gets b default.
     *
     * @return the b default
     */
    public Boolean getbDefault() {
        return bDefault;
    }

    /**
     * Sets b default.
     *
     * @param bDefault the b default
     */
    public void setbDefault(Boolean bDefault) {
        this.bDefault = bDefault;
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
     * Gets b dirty bit.
     *
     * @return the b dirty bit
     */
    public Boolean getbDirtyBit() {
        return bDirtyBit;
    }

    /**
     * Sets b dirty bit.
     *
     * @param bDirtyBit the b dirty bit
     */
    public void setbDirtyBit(Boolean bDirtyBit) {
        this.bDirtyBit = bDirtyBit;
    }

    @Override
    public String toString() {
        return "Address{" +
                "strAddressId='" + iAddressId + '\'' +
                ", strAddress='" + strAddress + '\'' +
                ", strCity='" + strCity + '\'' +
                ", strDistrict='" + strDistrict + '\'' +
                ", strPhone='" + strPhone + '\'' +
                ", strProvince='" + strProvince + '\'' +
                ", strRecipientName='" + strRecipientName + '\'' +
                ", bDefault=" + bDefault +
                ", bDirtyBit=" + bDirtyBit +
                '}';
    }
}
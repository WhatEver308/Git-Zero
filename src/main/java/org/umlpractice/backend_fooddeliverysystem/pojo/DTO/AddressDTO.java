package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;

/**
 * 地址传输对象
 * 用于新增、修改、查询地址时的数据传递
 */
public class AddressDTO {
    private Integer iAddressId = null;
    private String strAddress;
    private String strCity;
    private String strDistrict;
    private String strPhone;
    private String strProvince;
    private String strRecipientName;
    private Boolean bDefault;

    // getter 和 setter 方法

    public Integer getiAddressId() {
        return iAddressId;
    }

    public void setiAddressId(Integer iAddressId) {
        this.iAddressId = iAddressId;
    }

    public String getStrAddress() {
        return strAddress;
    }

    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    public String getStrCity() {
        return strCity;
    }

    public void setStrCity(String strCity) {
        this.strCity = strCity;
    }

    public String getStrDistrict() {
        return strDistrict;
    }

    public void setStrDistrict(String strDistrict) {
        this.strDistrict = strDistrict;
    }

    public String getStrPhone() {
        return strPhone;
    }

    public void setStrPhone(String strPhone) {
        this.strPhone = strPhone;
    }

    public String getStrProvince() {
        return strProvince;
    }

    public void setStrProvince(String strProvince) {
        this.strProvince = strProvince;
    }

    public String getStrRecipientName() {
        return strRecipientName;
    }

    public void setStrRecipientName(String strRecipientName) {
        this.strRecipientName = strRecipientName;
    }

    public Boolean getbDefault() {
        return bDefault;
    }

    public void setbDefault(Boolean bDefault) {
        this.bDefault = bDefault;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "iAddressId=" + iAddressId +
                ", strAddress='" + strAddress + '\'' +
                ", strCity='" + strCity + '\'' +
                ", strDistrict='" + strDistrict + '\'' +
                ", strPhone='" + strPhone + '\'' +
                ", strProvince='" + strProvince + '\'' +
                ", strRecipientName='" + strRecipientName + '\'' +
                ", bDefault=" + bDefault +
                '}';
    }

    public boolean isValidForAdd() {
        return strAddress != null && !strAddress.isEmpty()
                && strCity != null && !strCity.isEmpty()
                && strDistrict != null && !strDistrict.isEmpty()
                && strPhone != null && !strPhone.isEmpty()
                && strProvince != null && !strProvince.isEmpty()
                && strRecipientName != null && !strRecipientName.isEmpty()
                && bDefault != null;
    }

    public boolean isValidForUpdate() {
        return iAddressId != null && iAddressId > 0
                && strAddress != null && !strAddress.isEmpty()
                && strCity != null && !strCity.isEmpty()
                && strDistrict != null && !strDistrict.isEmpty()
                && strPhone != null && !strPhone.isEmpty()
                && strProvince != null && !strProvince.isEmpty()
                && strRecipientName != null && !strRecipientName.isEmpty()
                && bDefault != null;
    }

    public boolean isValidForOrderCreate(){
        return strAddress != null && !strAddress.isEmpty()
                && strCity != null && !strCity.isEmpty()
                && strDistrict != null && !strDistrict.isEmpty()
                && strPhone != null && !strPhone.isEmpty()
                && strProvince != null && !strProvince.isEmpty()
                && strRecipientName != null && !strRecipientName.isEmpty();
    }
}
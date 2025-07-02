package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;

/**
 * The type User dto.
 * Usage:Data transmission object for http transmission
 * @author 刘陈文君
 * @date 2025 /5/28 18:01
 */
public class UserDTO {

    private Integer iUserId;

    private String strUserName;

    private String strPassword;

    private String strEmail;

    private String strPhone;

    private String strUserCategory;

    private String strUserGender;

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

    public String getStrUserGender() {
        return strUserGender;
    }

    public void setStrUserGender(String strUserGender) {
        this.strUserGender = strUserGender;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
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

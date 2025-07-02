package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;


/**
 * UserModificationDTO 类说明
 *
 * @author 刘陈文君
 * @date 2025/7/1 14:55
 */
public class UserModificationDTO extends UserDTO {
    private String strRawPassword;
    private String strNewPassword;

    @Override
    public String toString() {
        return "UserModificationDTO{" +
                "strRawPassword='" + strRawPassword + '\'' +
                ", strNewPassword='" + strNewPassword + '\'' +
                '}';
    }

    public String getStrRawPassword() {
        return strRawPassword;
    }

    public void setStrRawPassword(String strRawPassword) {
        this.strRawPassword = strRawPassword;
    }

    public String getStrNewPassword() {
        return strNewPassword;
    }

    public void setStrNewPassword(String strNewPassword) {
        this.strNewPassword = strNewPassword;
    }
}

package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;

/**
 * UserLoginDTO 类说明
 * Used for transmitting user login request
 *
 * @author 刘陈文君
 * @date 2025 /5/28 19:46
 */
public class UserLoginDTO {
    private String strUserName;
    private String strPassword;

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

    @Override
    public String toString() {
        return "UserLoginDTO{" +
                "strUserName='" + strUserName + '\'' +
                ", strPassword='" + strPassword + '\'' +
                '}';
    }
}

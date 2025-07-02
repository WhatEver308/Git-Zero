package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;


/**
 * UserLoginResponseDTO 类说明
 *
 * @author 刘陈文君
 * @date 2025 /5/30 14:34
 */
public class UserLoginResponseDTO {
    private UserDTO user_info;
    private String token;

    @Override
    public String toString() {
        return "UserLoginResponseDTO{" +
                "user_info=" + user_info +
                ", token='" + token + '\'' +
                '}';
    }

    /**
     * Gets user info.
     *
     * @return the user info
     */
    public UserDTO getUser_info() {
        return user_info;
    }

    /**
     * Sets user info.
     *
     * @param user_info the user info
     */
    public void setUser_info(UserDTO user_info) {
        this.user_info = user_info;
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets token.
     *
     * @param token the token
     */
    public void setToken(String token) {
        this.token = token;
    }
}

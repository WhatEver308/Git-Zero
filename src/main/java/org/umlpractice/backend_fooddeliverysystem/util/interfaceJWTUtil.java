package org.umlpractice.backend_fooddeliverysystem.util;

/**
 * interfaceJWTUtil 接口说明
 * Used for JWT authentication
 * @author 30367
 * @date 2025/5/28 17:53
 */
public interface interfaceJWTUtil {

    public String generateToken(String userName);

    public boolean verifyToken(String token);

    public String parseUserNameFromToken(String token);

}

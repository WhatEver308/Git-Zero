package org.umlpractice.backend_fooddeliverysystem.pojo;


/**
 * LoginError 类说明
 *
 * @author 刘陈文君
 * @date 2025/5/30 14:52
 */
public class LoginError {
    private Integer code;
    private String error;
    private String errorDescription;

    public static LoginError loginError(String error)
    {
        LoginError loginError = new LoginError();
        loginError.setError(error);
        loginError.setCode(401);
        loginError.setErrorDescription("Invalid Credential");
        return loginError;
    }

    @Override
    public String toString() {
        return "LoginError{" +
                "error='" + error + '\'' +
                ", code=" + code +
                ", errorDescription='" + errorDescription + '\'' +
                '}';
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}

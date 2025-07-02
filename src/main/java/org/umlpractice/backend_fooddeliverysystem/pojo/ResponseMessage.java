package org.umlpractice.backend_fooddeliverysystem.pojo;
import org.springframework.http.HttpStatus;

/**
 * ResponseMessage 类说明
 *
 * @param <T> the type parameter
 * @author 刘陈文君
 * @date 2025 /5/27 14:30
 */
public class ResponseMessage<T> {
    private Integer code;
    private String message;
    private T data;

    /**
     * Gets code.
     *
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * Instantiates a new Response message.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     */
    public ResponseMessage(int code, String message, T data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * Success response message.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the response message
     */
    public static <T> ResponseMessage<T> success(T data)
    {
        ResponseMessage<T> responseMessage = new ResponseMessage<T>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
        return responseMessage;
    }

    public static <T> ResponseMessage<T> failure(T data)
    {
        ResponseMessage<T> responseMessage = new ResponseMessage<T>(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), data);
        return responseMessage;
    }
}

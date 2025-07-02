package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;


/**
 * OrderStatusUpdateBody 类说明
 * @author 刘陈文君
 * @date 2025/6/30 23:10
 */
public class OrderStatusUpdateBody {
    private String status;

    @Override
    public String toString() {
        return "OrderStatusUpdateBody{" +
                "status='" + status + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

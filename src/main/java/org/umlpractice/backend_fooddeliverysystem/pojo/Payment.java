package org.umlpractice.backend_fooddeliverysystem.pojo;


import jakarta.persistence.*;

/**
 * Payment 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/30 20:32
 */
@Table(name="tb_payment")
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    private Integer iPaymentId;

    @Column(name="payment")
    private String strPayment;

    @Column(name="created_time")
    private String strCreatedTime;

    @Column(name="amount")
    private double dAmount;

    @Column(name="related_order_id")
    private Integer iRelatedOrderId;

    @Column(name="payment_status")
    private String strPaymentStatus;

    @Override
    public String toString() {
        return "Payment{" +
                "iPaymentId=" + iPaymentId +
                ", strPayment='" + strPayment + '\'' +
                ", strCreatedTime='" + strCreatedTime + '\'' +
                ", dAmount=" + dAmount +
                ", iRelatedOrderId=" + iRelatedOrderId +
                ", strPaymentStatus='" + strPaymentStatus + '\'' +
                '}';
    }

    public String getStrPaymentStatus() {
        return strPaymentStatus;
    }

    public void setStrPaymentStatus(String strPaymentStatus) {
        this.strPaymentStatus = strPaymentStatus;
    }

    public Integer getiPaymentId() {
        return iPaymentId;
    }

    public void setiPaymentId(Integer iPaymentId) {
        this.iPaymentId = iPaymentId;
    }

    public String getStrPayment() {
        return strPayment;
    }

    public void setStrPayment(String strPayment) {
        this.strPayment = strPayment;
    }

    public String getStrCreatedTime() {
        return strCreatedTime;
    }

    public void setStrCreatedTime(String strCreatedTime) {
        this.strCreatedTime = strCreatedTime;
    }

    public double getdAmount() {
        return dAmount;
    }

    public void setdAmount(double dAmount) {
        this.dAmount = dAmount;
    }

    public Integer getiRelatedOrderId() {
        return iRelatedOrderId;
    }

    public void setiRelatedOrderId(Integer iRelatedOrderId) {
        this.iRelatedOrderId = iRelatedOrderId;
    }
}

package org.umlpractice.backend_fooddeliverysystem.DAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.umlpractice.backend_fooddeliverysystem.pojo.DeliveryOrder;

import java.util.List;

public interface InterfaceDeliveryOrderDAO extends CrudRepository<DeliveryOrder, Integer> {
    /**
     * Find all delivery orders by user ID.
     *
     * @param iUserId 用户ID
     * @return 订单列表
     */
    @Query("SELECT d FROM DeliveryOrder d WHERE d.user.iUserId = :iUserId")
    List<DeliveryOrder> findOrdersByUserId(Integer iUserId);
    /**
     * Find all delivery orders by merchant ID.
     *
     * @param iMerchantId 商家ID
     * @return 订单列表
     */
    @Query("SELECT d FROM DeliveryOrder d WHERE d.merchant.iMerchantId = :iMerchantId")
    List<DeliveryOrder> findOrdersByMerchantId(@Param("iMerchantId")Integer iMerchantId);
    /**
     * Find delivery order by order ID.
     *
     * @param iOrderId 订单 ID
     * @return 订单对象
     */
    @Query("SELECT d FROM DeliveryOrder d WHERE d.iOrderId = :iOrderId")
    DeliveryOrder findOrderById(Integer iOrderId);
}

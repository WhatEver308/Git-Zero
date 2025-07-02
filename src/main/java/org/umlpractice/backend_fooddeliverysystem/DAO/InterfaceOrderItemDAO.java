package org.umlpractice.backend_fooddeliverysystem.DAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.umlpractice.backend_fooddeliverysystem.pojo.OrderItem;

import java.util.List;

@Repository
public interface InterfaceOrderItemDAO extends CrudRepository<OrderItem, Integer> {

    /**
     * Find all order item by orderitem ID.
     *
     * @param iOrderItemId 商家 ID
     * @return 订单菜品项
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.iOrderItemId = :iOrderItemId")
    List<OrderItem> findOrderItemsByOrderItemId(Integer iOrderItemId);
}
package org.umlpractice.backend_fooddeliverysystem.DAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.umlpractice.backend_fooddeliverysystem.pojo.Payment;

import java.util.List;

@Repository
public interface InterfacePaymentDAO extends CrudRepository<Payment,Integer> {
    @Query("SELECT m FROM Payment m WHERE m.strPaymentStatus =:status")
    List<Payment> findByStatus(@Param("status") String status);
}

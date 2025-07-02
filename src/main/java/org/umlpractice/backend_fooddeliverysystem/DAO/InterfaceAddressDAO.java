package org.umlpractice.backend_fooddeliverysystem.DAO;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.umlpractice.backend_fooddeliverysystem.pojo.Address;

import java.util.List;

@Repository
public interface InterfaceAddressDAO extends CrudRepository<Address, Integer> {

    /**
     * Find all addresses by user ID.
     *
     * @param iUserId 用户 ID
     * @return 地址列表
     */
    @Query("SELECT a FROM Address a WHERE a.user.iUserId = :iUserId")
    List<Address> findAddressesByUserId(Integer iUserId);

    /**
     * Find address by ID.
     *
     * @param iAddressId 地址 ID
     * @return 地址对象
     */
    @Query("SELECT a FROM Address a WHERE a.iAddressId = :iAddressId")
    Address findAddressById(Integer iAddressId);
}

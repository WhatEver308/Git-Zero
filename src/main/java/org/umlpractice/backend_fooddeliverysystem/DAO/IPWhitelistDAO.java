package org.umlpractice.backend_fooddeliverysystem.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.umlpractice.backend_fooddeliverysystem.pojo.IPWhitelistItem;

@Repository
public interface IPWhitelistDAO extends CrudRepository<IPWhitelistItem,Integer> {
    @Query("SELECT u FROM IPWhitelistItem u WHERE u.strIP =: ip")
    public IPWhitelistItem findIPWhitelistItemByIp(String ip);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM IPWhitelistItem i WHERE i.strIP = :ip")
    public boolean existsByIp(String ip);
}

package org.umlpractice.backend_fooddeliverysystem.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.umlpractice.backend_fooddeliverysystem.pojo.APIWhitelistItem;

import java.util.List;

@Repository
public interface APIWhitelistDAO extends CrudRepository<APIWhitelistItem,Integer> {
    @Query("SELECT u FROM APIWhitelistItem u WHERE u.strPath LIKE %:path%")
    List<APIWhitelistItem> getPathWhitelistItemByPathPattern(String path);
}

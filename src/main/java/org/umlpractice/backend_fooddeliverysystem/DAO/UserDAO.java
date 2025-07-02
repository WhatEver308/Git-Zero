package org.umlpractice.backend_fooddeliverysystem.DAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;

/**
 * The interface Interface user dao.
 * <T,ID>implies operations on User pojo from Id:Integer
 *
 * @author 刘陈文君
 * @date 2025 /5/28 18:01
 */
@Repository
public interface UserDAO extends CrudRepository<User,Integer> {


    /**
     * Find by username
     *
     * @param strUserName the str user name
     * @return the user
     */
    @Query("SELECT u FROM User u WHERE u.strUserName = :strUserName")
    public User findByUserName(String strUserName);

    @Query("SELECT EXISTS (SELECT 1 FROM User u WHERE u.strUserName = :username)")
    public boolean existsByUsername(String username);

}

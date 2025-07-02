package org.umlpractice.backend_fooddeliverysystem.service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.umlpractice.backend_fooddeliverysystem.DAO.UserDAO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.UserDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * UserServiceImplement 类说明
 * 用于用户相关操作
 *
 * @author 刘陈文君
 * @date 2025 /5/28 18:39
 */
@Service
@Transactional
public class UserServiceImplement implements InterfaceUserService
{
    /**
     * The User dao.
     */
    @Autowired
    UserDAO userDAO;

    @Autowired
    PasswordEncoder passwordEncoder;
    /**
     * add user to MySQL server.
     * @param userDTO the UserDTO object
     * @return User object
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addUser(UserDTO userDTO)
    {

        User pojoUser = new User();
        BeanUtils.copyProperties(userDTO, pojoUser);

        pojoUser.setStrPassword(passwordEncoder.encode(pojoUser.getStrPassword()));
        return userDAO.save(pojoUser);
    }
    /**
     * update user in MySQL server.
     * @param userDTO the UserDTO object
     * @return User object
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(UserDTO userDTO){

        User findResult = userDAO.findByUserName(userDTO.getStrUserName());
        if(findResult != null)
        {
            Integer originalUserId = findResult.getiUserId();
            BeanUtils.copyProperties(userDTO, findResult);
            findResult.setiUserId(originalUserId);
            return userDAO.save(findResult);
        }
        User pojoUser = new User();
        BeanUtils.copyProperties(userDTO, pojoUser);
        pojoUser.setStrPassword(passwordEncoder.encode(pojoUser.getStrPassword()));

        return userDAO.save(pojoUser);
    }
    /**
     * delete user in MySQL server.
     * @param userDTO the UserDTO object
     * @return boolean value indicates success
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(UserDTO userDTO)
    {
        User pojoUser = new User();
        BeanUtils.copyProperties(userDTO, pojoUser);

        Optional<User> result = userDAO.findById(pojoUser.getiUserId());
        userDAO.delete(pojoUser);
        return result.isPresent();
    }

    /**
     * get User object in MySQL server
     * @param userId the Integer
     * @return User object
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserById(Integer userId){
        return userDAO.findById(userId).get();
    }

    /**
     * update user in MySQL server.
     * @param userName the username str
     * @return User object
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserByUserName(String userName)
    {
        return userDAO.findByUserName(userName);
    }

    public User registerUser(UserDTO userDTO)
    {
        if(userDAO.existsByUsername(userDTO.getStrUserName()))
        {
            return null;
        }
        User pojoUser = new User();
        BeanUtils.copyProperties(userDTO, pojoUser);
        pojoUser.setStrPassword(passwordEncoder.encode(pojoUser.getStrPassword()));
        return userDAO.save(pojoUser);
    }

    @Override
    public boolean verifyUser(Integer userId, String username) throws IllegalArgumentException
    {
        Optional<User> user = userDAO.findById(userId);
        if(!user.isPresent())
            throw new IllegalArgumentException("Cannot find user with name " + username);
        if(user.get().getStrUserName().equals(username))
            return true;
        throw new IllegalArgumentException("Username:"+username+" does not match context");
    }
}

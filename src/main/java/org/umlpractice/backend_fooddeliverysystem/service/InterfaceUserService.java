package org.umlpractice.backend_fooddeliverysystem.service;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.UserDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;

/**
 * The interface Interface user service.
 * @author 刘陈文君
 * @date 2025 /5/28 19:46
 */
public interface InterfaceUserService
{
    public User addUser(UserDTO userDTO);
    public User updateUser(UserDTO userDTO);
    public boolean deleteUser(UserDTO userDTO);
    public User getUserById(Integer userId);
    public User getUserByUserName(String userName);
    public boolean verifyUser(Integer userId, String userName) throws IllegalArgumentException;
}

package org.umlpractice.backend_fooddeliverysystem.service;

import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.AddressDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.Address;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;

import java.util.List;
/**
 * 地址相关操作接口
 * 提供新增、删除、修改、查询地址的功能
 * @author LiJunjie
 * @date 2025/5/30
 */
public interface InterfaceAddressService {
    public User getUserById(Integer iUserId);

    public Address addAddress(Integer iUserId, AddressDTO addressDTO);

    public Address updateAddress(Integer iUserId, AddressDTO addressDTO);

    public boolean deleteAddress(Integer iUserId, Integer iAddressId);

    public Address getAddressById(Integer iAddressId);

    public List<AddressDTO> getAllAddressesByUserId(Integer iUserId);

    public boolean setDefaultAddress(Integer iUserId, Integer iAddressId);
}
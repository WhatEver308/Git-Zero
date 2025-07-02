package org.umlpractice.backend_fooddeliverysystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.umlpractice.backend_fooddeliverysystem.DAO.InterfaceAddressDAO;
import org.umlpractice.backend_fooddeliverysystem.DAO.UserDAO;
import org.umlpractice.backend_fooddeliverysystem.pojo.Address;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.AddressDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressServiceImplement implements InterfaceAddressService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private InterfaceAddressDAO addressDAO;

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Integer iUserId) {
        Optional<User> optionalUser = userDAO.findById(iUserId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new IllegalArgumentException("User with ID " + iUserId + " not found.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address addAddress(Integer iUserId, AddressDTO addressDTO) {
        User user = getUserById(iUserId);

        if (addressDTO == null) {
            throw new IllegalArgumentException("AddressDTO cannot be null.");
        }

        if(!addressDTO.isValidForAdd()) {
            throw new IllegalArgumentException("AddressDTO is not valid. Please check the fields.");
        }

        Address newAddress = new Address();

        BeanUtils.copyProperties(addressDTO, newAddress, "iAddressId");

        newAddress.setUser(user);
        user.addAddress(newAddress);

        if(addressDTO.getbDefault()==true){
            user.setDefaultAddress(newAddress);
            // 这个函数会将用户的默认地址设置为新添加的地址
            // 也会将之前的默认地址设置为非默认状态
            // 注意：这个方法只会在地址还没获取到id之前使用
        }

        userDAO.save(user);
        return addressDAO.save(newAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address updateAddress(Integer iUserId, AddressDTO addressDTO) {
        User user = getUserById(iUserId);

        if (addressDTO == null ) {
            throw new IllegalArgumentException("AddressDTO cannot be null.");
        }
        if(!addressDTO.isValidForUpdate()) {
            throw new IllegalArgumentException("AddressDTO is not valid for update. Please check the fields.");
        }

        Address existingAddress = getAddressById(addressDTO.getiAddressId());
        Address updatedAddress = new Address();

        if (!existingAddress.getUser().getiUserId().equals(iUserId)) {
            throw new IllegalArgumentException("Address does not belong to the specified User.");
        }

        if(existingAddress.getbDirtyBit()==true) {
            throw new IllegalArgumentException("Address is dirty and cannot be updated directly. Please delete it first.");
        }

        BeanUtils.copyProperties(addressDTO, updatedAddress, "iAddressId");

        existingAddress.setbDirtyBit(true);

        user.removeAddress(existingAddress.getIAddressId());

        user.addAddress(updatedAddress);

        updatedAddress.setUser(user);

        if(addressDTO.getbDefault()==true){
            user.setDefaultAddress(updatedAddress);
            // 这个函数会将用户的默认地址设置为新添加的��址
            // 也会将之前的默认地址设置为非默认状态
        }

        userDAO.save(user);
        addressDAO.save(existingAddress);

        updatedAddress.setIAddressId(null);
        // 不知道为什么，这里的updatedAddress的iAddressId会和existingAddress的iAddressId相同
        // 所以需要将其设置为null，确保保存时生成新的ID

        return addressDAO.save(updatedAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAddress(Integer iUserId, Integer iAddressId) {
        User user = getUserById(iUserId);
        Address address = getAddressById(iAddressId);

        if (!address.getUser().getiUserId().equals(iUserId)) {
            throw new IllegalArgumentException("Address does not belong to the specified User.");
        }

        if(address.getbDirtyBit() == true) {
            throw new IllegalArgumentException("Address is dirty and cannot be deleted directly. Please update it first.");
        }

        address.setbDirtyBit(true); // 设置脏位，表示需要更新

        user.removeAddress(address.getIAddressId());

        userDAO.save(user);
        addressDAO.save(address);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Address getAddressById(Integer iAddressId) {
        Optional<Address> optionalAddress = addressDAO.findById(iAddressId);
        if (optionalAddress.isPresent()) {
            return optionalAddress.get();
        } else {
            throw new IllegalArgumentException("Address with ID " + iAddressId + " not found.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> getAllAddressesByUserId(Integer iUserId) {
        User user = getUserById(iUserId);

        if (user.getAddresses() == null || user.getAddresses().isEmpty()) {
            throw new IllegalArgumentException("No addresses found for User with ID " + iUserId);
        }

        List<AddressDTO> addressDTOList = new ArrayList<>();
        for (Address address : user.getAddresses()) {
            if(address.getbDirtyBit()==true)
                continue;
            AddressDTO addressDTO = new AddressDTO();
            BeanUtils.copyProperties(address, addressDTO);
            addressDTOList.add(addressDTO);
        }

        return addressDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultAddress(Integer iUserId, Integer iAddressId) {
        User user = getUserById(iUserId);

        try{
        user.setDefaultAddress(iAddressId);
        }
        catch (IllegalArgumentException e) {
            throw e; // 如果地址不存在于该用户之下，setDefaultAddress函数会抛出异常
        }

        userDAO.save(user);
        return true;
    }
}
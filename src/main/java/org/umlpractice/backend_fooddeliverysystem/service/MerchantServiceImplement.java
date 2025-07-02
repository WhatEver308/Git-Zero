package org.umlpractice.backend_fooddeliverysystem.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.umlpractice.backend_fooddeliverysystem.DAO.InterfaceMerchantDAO;
import org.umlpractice.backend_fooddeliverysystem.exceptions.AlreadyExistException;
import org.umlpractice.backend_fooddeliverysystem.exceptions.NotFoundException;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MenuItemQueryResponseDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MerchantDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MerchantQueryResponseDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.MenuItem;
import org.umlpractice.backend_fooddeliverysystem.pojo.Merchant;

import java.util.ArrayList;
import java.util.List;

/**
 * UserServiceImplement 类说明
 * 用于用户相关操作
 *
 * @author 刘陈文君
 * @date 2025 /5/28 18:39
 */
@Service
@Transactional
public class MerchantServiceImplement implements InterfaceMerchantService {

    @Autowired
    private InterfaceMerchantDAO interfaceMerchantDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MenuItemServiceImplement menuItemServiceImplement;

    @Override
    @Transactional(readOnly = true)
    public Merchant getMerchantByMerchantName(String merchantName) throws UsernameNotFoundException
    {
        Merchant ret = interfaceMerchantDAO.findByMerchantName(merchantName);
        if(ret==null)
            throw new UsernameNotFoundException("Merchant not found");
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Merchant registerMerchant(MerchantDTO merchantDTO) throws AlreadyExistException
    {
        if(interfaceMerchantDAO.findByMerchantName(merchantDTO.getStrMerchantName()) != null)
        {
            throw new AlreadyExistException("Merchant already exist");
        }
        Merchant merchant = new Merchant();
        BeanUtils.copyProperties(merchantDTO, merchant);
        merchant.setStrPassword(passwordEncoder.encode(merchantDTO.getStrPassword()));
        merchant.setdAveragePrice(-1.0);
        merchant.setdDeliveryFee(-1.0);
        merchant.setdMinOrder(-1.0);
        merchant.setdRating(-1.0);
        merchant.setdDeliveryTime(-1.0);
        return this.interfaceMerchantDAO.save(merchant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Merchant updateMerchant(MerchantDTO merchantDTO) throws UsernameNotFoundException
    {
        if(interfaceMerchantDAO.existsByMerchantName(merchantDTO.getStrMerchantName()))
        {
            Merchant searchResult = interfaceMerchantDAO.findByMerchantName(merchantDTO.getStrMerchantName());
            BeanUtils.copyProperties(merchantDTO, searchResult);
            return this.interfaceMerchantDAO.save(searchResult);
        }
        else
        {
            throw new UsernameNotFoundException("Merchant not found");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMerchantByMerchantName(String merchantName) throws UsernameNotFoundException
    {
        if(!interfaceMerchantDAO.existsByMerchantName(merchantName))
        {
            throw new UsernameNotFoundException("Merchant not found");
        }
        Merchant searchResult = interfaceMerchantDAO.findByMerchantName(merchantName);
        interfaceMerchantDAO.deleteById(searchResult.getiMerchantId());
    }

    @Override
    public UserDetails loadByMerchantName(String merchantName) throws UsernameNotFoundException
    {
        if(interfaceMerchantDAO.findByMerchantName(merchantName)==null)
            throw new UsernameNotFoundException("Merchant: " + merchantName + " not found");

        Merchant merchant = interfaceMerchantDAO.findByMerchantName(merchantName);
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MERCHANT"));

        return new org.springframework.security.core.userdetails.User(
            merchant.getStrMerchantName(),
            merchant.getStrPassword(), // 从数据库加载的加密密码
            authorities);
    }

    @Override
    @Transactional(readOnly = true)
    public Merchant getMerchantByMerchantId(Integer merchantID) throws NotFoundException
    {
        Merchant ret = interfaceMerchantDAO.findByMerchantId(merchantID);
        if(ret==null)
            throw new NotFoundException("Merchant " +merchantID.toString()+ " not found");
        return ret;
    }

    @Override
    @Transactional(readOnly = true)
    public MerchantQueryResponseDTO getMerchantQueryResponseDTOByMerchantId(Integer merchantId) throws NotFoundException {
        Merchant merchant = getMerchantByMerchantId(merchantId);

        MerchantQueryResponseDTO responseDTO = new MerchantQueryResponseDTO();
        BeanUtils.copyProperties(merchant, responseDTO, "cuisine","strMerchantCategory");

        String Merchantcategory ;
        try {
            Merchantcategory = merchant.getStrMerchantCategory().name();
        } catch (NullPointerException e) {
            throw new NotFoundException("Merchant category is null. Please check the Merchant data.");
        }
        responseDTO.setStrMerchantCategory(Merchantcategory);

        if(merchant.getCuisine() != null) {

            List<MenuItem> cuisineList =  merchant.getCuisine();

            for(MenuItem menuItem : cuisineList) {
            MenuItemQueryResponseDTO menuItemDTO = new MenuItemQueryResponseDTO();
            menuItemDTO = menuItemServiceImplement.getMenuItemDTOById(menuItem.getiMenuItemId());
            responseDTO.addCuisine(menuItemDTO);
            }
        }

        return responseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ArrayList<Merchant> getMerchantByCategory(Merchant.StrMerchantCategory merchantCategory)
    {
        return interfaceMerchantDAO.findByMerchantCategory(merchantCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public ArrayList<Merchant> getAllMerchants()
    {
        return (ArrayList<Merchant>) interfaceMerchantDAO.findAll();
    }

    @Override
    public boolean verifyMerchant(Integer merchantID,String merchantName)throws IllegalArgumentException
    {
        Merchant merchant = this.interfaceMerchantDAO.findByMerchantId(merchantID);
        if(merchant==null)
            throw new IllegalArgumentException("Merchant: " + merchantID.toString()+ " not found");
        if(merchant.getStrMerchantName().equals(merchantName))
            return true;
        throw new IllegalArgumentException("Merchant: " + merchantID.toString()+ " does not match context");
    }
}
package org.umlpractice.backend_fooddeliverysystem.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.umlpractice.backend_fooddeliverysystem.exceptions.AlreadyExistException;
import org.umlpractice.backend_fooddeliverysystem.exceptions.NotFoundException;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MerchantDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MerchantQueryResponseDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.Merchant;


import java.util.ArrayList;

/**
 * The interface Interface merchant service.
 * 商家相关操作接口
 * @author 刘陈文君
 */
public interface InterfaceMerchantService {
    public Merchant getMerchantByMerchantName(String merchantName)throws UsernameNotFoundException;
    public Merchant registerMerchant(MerchantDTO merchantDTO) throws AlreadyExistException;
    public Merchant updateMerchant(MerchantDTO merchantDTO) throws UsernameNotFoundException;
    public void deleteMerchantByMerchantName(String merchantName) throws UsernameNotFoundException;
    public UserDetails loadByMerchantName(String merchantName) throws UsernameNotFoundException;
    public Merchant getMerchantByMerchantId(Integer merchantId) throws NotFoundException;
    public MerchantQueryResponseDTO getMerchantQueryResponseDTOByMerchantId(Integer merchantId) throws NotFoundException;
    public ArrayList<Merchant> getMerchantByCategory(Merchant.StrMerchantCategory merchantCategory);
    public ArrayList<Merchant> getAllMerchants();
    public boolean verifyMerchant(Integer merchantId,String merchantName) throws IllegalArgumentException;
}
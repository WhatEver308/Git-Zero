package org.umlpractice.backend_fooddeliverysystem.controller.api.restaurants;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.umlpractice.backend_fooddeliverysystem.exceptions.NotFoundException;
import org.umlpractice.backend_fooddeliverysystem.pojo.DTO.MerchantQueryResponseDTO;
import org.umlpractice.backend_fooddeliverysystem.pojo.MenuItem;
import org.umlpractice.backend_fooddeliverysystem.pojo.Merchant;
import org.umlpractice.backend_fooddeliverysystem.service.InterfaceMerchantService;
import org.umlpractice.backend_fooddeliverysystem.service.MerchantServiceImplement;
import org.umlpractice.backend_fooddeliverysystem.util.Authenticator;
import org.umlpractice.backend_fooddeliverysystem.util.InterfaceAuthenticator;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RestaurantsController 类说明
 *
 * @author 刘陈文君
 * @date 2025 /6/6 12:50
 */
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantsController {
    /**
     * The Interface merchant service.
     */
    @Autowired
    InterfaceMerchantService interfaceMerchantService;

    /**
     * Gets specified merchant details.
     *
     * @param merchant_id the merchant id
     * @return the specified merchant details
     */

    @Autowired
    private InterfaceAuthenticator authenticator;

    @GetMapping("/{merchant_id}")
    public Object getSpecifiedMerchantDetails(@PathVariable("merchant_id") String merchant_id)
    {
        try
        {
            Integer merchantId = Integer.parseInt(merchant_id);
            MerchantQueryResponseDTO retValue = interfaceMerchantService.getMerchantQueryResponseDTOByMerchantId(merchantId);
            merchantQueryResponseDTO ret = new merchantQueryResponseDTO();
            ret.setStrMerchantCategory(retValue.strMerchantCategory());
            ret.setStrLogo_url(retValue.strLogo_url());
            ret.setStrCoverImage_url(retValue.strCoverImage_url());
            ret.setStrMerchantName(retValue.strMerchantName());
            ret.setiMerchantId(retValue.iMerchantId());
            Merchant result = interfaceMerchantService.getMerchantByMerchantId(merchantId);
            for(MenuItem i:result.getCuisine())
            {
                i.setMerchant(null);
            }
            result.setDeliveryOrders(null);
            return ResponseEntity.ok().body(result);
        }
        catch(IllegalArgumentException e)
        {
            Map<String,Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        catch(NotFoundException e)
        {
            Map<String,Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    class merchantQueryResponseDTO
    {
        private Integer iMerchantId;
        private String strMerchantName;
        private String strLogo_url;
        private String strCoverImage_url;
        private String strMerchantCategory;

        @Override
        public String toString() {
            return "merchantQueryResponseDTO{" +
                    "iMerchantId=" + iMerchantId +
                    ", strMerchantName='" + strMerchantName + '\'' +
                    ", strLogo_url='" + strLogo_url + '\'' +
                    ", strCoverImage_url='" + strCoverImage_url + '\'' +
                    ", strMerchantCategory='" + strMerchantCategory + '\'' +
                    '}';
        }

        public Integer getiMerchantId() {
            return iMerchantId;
        }

        public void setiMerchantId(Integer iMerchantId) {
            this.iMerchantId = iMerchantId;
        }

        public String getStrMerchantName() {
            return strMerchantName;
        }

        public void setStrMerchantName(String strMerchantName) {
            this.strMerchantName = strMerchantName;
        }

        public String getStrLogo_url() {
            return strLogo_url;
        }

        public void setStrLogo_url(String strLogo_url) {
            this.strLogo_url = strLogo_url;
        }

        public String getStrCoverImage_url() {
            return strCoverImage_url;
        }

        public void setStrCoverImage_url(String strCoverImage_url) {
            this.strCoverImage_url = strCoverImage_url;
        }

        public String getStrMerchantCategory() {
            return strMerchantCategory;
        }

        public void setStrMerchantCategory(String strMerchantCategory) {
            this.strMerchantCategory = strMerchantCategory;
        }
    }

    private static int countKeywordMatches(String text, List<String> keywords) {
        return (int) keywords.stream()
                .filter(text::contains)
                .count();
    }

    /**
     * Gets restaurants list.
     *
     * @return the restaurants list
     */
    @GetMapping
    public Object getRestaurantsList(
            @RequestParam("strCategory") String category,
            @RequestParam("strSortBy") String strSortBy,
            @RequestParam("arrstrKeywords") ArrayList<String> arrstrKeywords,
            @RequestParam("iPage") Integer iPage
    )
    {
        try
        {
            if(iPage == null)
                throw new IllegalArgumentException("Required parameter iPage is null");

            ArrayList<Merchant> queryReturn;

            if(category != null&& !category.isEmpty())
            {
                Merchant.StrMerchantCategory icategory = Merchant.StrMerchantCategory.valueOf(category);
                queryReturn = this.interfaceMerchantService.getMerchantByCategory(icategory);
            }
            else
            {
                queryReturn = this.interfaceMerchantService.getAllMerchants();
            }
            switch(strSortBy)//按照排序要求执行排序
            {
                case "name_ascend":
                {
                    Collections.sort(queryReturn, new Comparator<Merchant>()
                    {
                        @Override
                        public int compare(Merchant o1, Merchant o2)
                        {
                            return o1.getStrMerchantName().compareTo(o2.getStrMerchantName());
                        }
                    });
                    break;
                }
                case "name_descend":
                {
                    Collections.sort(queryReturn, new Comparator<Merchant>()
                    {
                        @Override
                        public int compare(Merchant o1, Merchant o2)
                        {
                            return o2.getStrMerchantName().compareTo(o1.getStrMerchantName());
                        }
                    });
                    break;
                }
                case "delivery_time_ascend":
                {
                    Collections.sort(queryReturn, new Comparator<Merchant>()
                    {
                        @Override
                        public int compare(Merchant o1, Merchant o2)
                        {
                            if(o1.getdDeliveryTime()==null||o2.getdDeliveryTime()==null)
                                return -1;
                            return o1.getdDeliveryTime().compareTo(o2.getdDeliveryTime());
                        }
                    });
                    break;
                }
                case "delivery_time_descend":
                {
                    Collections.sort(queryReturn, new Comparator<Merchant>()
                    {
                        @Override
                        public int compare(Merchant o1, Merchant o2)
                        {
                            if(o1.getdDeliveryTime()==null||o2.getdDeliveryTime()==null)
                                return -1;
                            return o2.getdDeliveryTime().compareTo(o1.getdDeliveryTime());
                        }
                    });
                    break;
                }
                case "rating_ascend":
                {
                    Collections.sort(queryReturn, new Comparator<Merchant>()
                    {
                        @Override
                        public int compare(Merchant o1, Merchant o2)
                        {
                            if(o1.getdRating()==null||o2.getdRating()==null)
                                return -1;
                            return o1.getdRating().compareTo(o2.getdRating());
                        }
                    });
                    break;
                }
                case "rating_descend":
                {
                    Collections.sort(queryReturn, new Comparator<Merchant>()
                    {
                        @Override
                        public int compare(Merchant o1, Merchant o2)
                        {
                            if(o1.getdRating()==null||o2.getdRating()==null)
                                return -1;
                            return o2.getdRating().compareTo(o1.getdRating());
                        }
                    });
                    break;
                }
                default:
                    break;
            }
            List<String> keywords = arrstrKeywords;
            List<Merchant>sorted = queryReturn.stream().sorted(Comparator.comparingInt(
                    s -> countKeywordMatches(((Merchant)s).getStrMerchantName(),keywords)
            ).reversed()).collect(Collectors.toList());
            int base = 10*(iPage-1);
            queryReturn = (ArrayList<Merchant>) sorted;
            ArrayList<merchantQueryResponseDTO> ret = new ArrayList<>();
            for(int i=base; i<=base+9; i++)
            {
                if(i>=queryReturn.size())
                    break;
                else
                {
                    Merchant target = queryReturn.get(i);
                    merchantQueryResponseDTO tmp = new merchantQueryResponseDTO();
                    tmp.setiMerchantId(target.getiMerchantId());
                    tmp.setStrLogo_url(target.getStrLogoUrl());
                    tmp.setStrMerchantName(target.getStrMerchantName());
                    tmp.setStrCoverImage_url(target.getStrCoverImageUrl());
                    tmp.setStrMerchantCategory(target.getStrMerchantCategory().toString());
                    ret.add(tmp);
                }
            }
            HashMap<String,Object> response = new HashMap<>();
            response.put("vecRestaurantsArray", ret);
            return ResponseEntity.ok().body(response);
        }
        catch(IllegalArgumentException e)
        {
            Map<String,Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{merchant_id}/menu/{menuItem_id}")
    public Object getMerchantMenuItem(
            @PathVariable("merchant_id") String strMerchantId,
            @PathVariable("menuItem_id") String strMenuItemId,
            Authentication authentication
    )
    {
        try
        {
            Integer merchantId = Integer.parseInt(strMerchantId);
            authenticator.authMerchant(merchantId, authentication);
            Integer menuItemId = Integer.parseInt(strMenuItemId);
            Merchant merchant = this.interfaceMerchantService.getMerchantByMerchantId(merchantId);
            for(MenuItem i:merchant.getCuisine())
            {
                if(i.getiMenuItemId()==menuItemId)
                {
                    i.setMerchant(null);
                    HashMap<String,Object> response = new HashMap<>();
                    response.put("menuItem", i);
                    return ResponseEntity.ok().body(response);
                }
            }
            throw new NotFoundException("Given menu item with id "+menuItemId+" is not found");
        }
        catch(IllegalArgumentException | NotFoundException e)
        {
            Map<String,Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;

import java.util.ArrayList;
import java.util.List;

public class MerchantQueryResponseDTO {
    private List<MenuItemQueryResponseDTO> cuisine;
    private double dAveragePrice;
    private double dDeliveryFee;
    private double dDeliveryTime;
    private double dMinOder;
    private double dstrRating;
    private int iMerchantId;
    private String strCoverImage_url;
    private String strLogo_url;
    private String strMerchantCategory;
    private String strMerchantName;


    public List<MenuItemQueryResponseDTO> cuisine() {
        return cuisine;
    }

    public MerchantQueryResponseDTO setCuisine(List<MenuItemQueryResponseDTO> cuisine) {
        this.cuisine = cuisine;
        return this;
    }

    public double dAveragePrice() {
        return dAveragePrice;
    }

    public MerchantQueryResponseDTO setdAveragePrice(double dAveragePrice) {
        this.dAveragePrice = dAveragePrice;
        return this;
    }

    public double dDeliveryFee() {
        return dDeliveryFee;
    }

    public MerchantQueryResponseDTO setdDeliveryFee(double dDeliveryFee) {
        this.dDeliveryFee = dDeliveryFee;
        return this;
    }

    public double dDeliveryTime() {
        return dDeliveryTime;
    }

    public MerchantQueryResponseDTO setdDeliveryTime(double dDeliveryTime) {
        this.dDeliveryTime = dDeliveryTime;
        return this;
    }

    public double dMinOder() {
        return dMinOder;
    }

    public MerchantQueryResponseDTO setdMinOder(double dMinOder) {
        this.dMinOder = dMinOder;
        return this;
    }

    public double dstrRating() {
        return dstrRating;
    }

    public MerchantQueryResponseDTO setDstrRating(double dstrRating) {
        this.dstrRating = dstrRating;
        return this;
    }

    public int iMerchantId() {
        return iMerchantId;
    }

    public MerchantQueryResponseDTO setiMerchantId(int iMerchantId) {
        this.iMerchantId = iMerchantId;
        return this;
    }

    public String strCoverImage_url() {
        return strCoverImage_url;
    }

    public MerchantQueryResponseDTO setStrCoverImage_url(String strCoverImage_url) {
        this.strCoverImage_url = strCoverImage_url;
        return this;
    }

    public String strLogo_url() {
        return strLogo_url;
    }

    public MerchantQueryResponseDTO setStrLogo_url(String strLogo_url) {
        this.strLogo_url = strLogo_url;
        return this;
    }

    public String strMerchantCategory() {
        return strMerchantCategory;
    }

    public MerchantQueryResponseDTO setStrMerchantCategory(String strMerchantCategory) {
        this.strMerchantCategory = strMerchantCategory;
        return this;
    }

    public String strMerchantName() {
        return strMerchantName;
    }

    public MerchantQueryResponseDTO setStrMerchantName(String strMerchantName) {
        this.strMerchantName = strMerchantName;
        return this;
    }

    public void addCuisine(MenuItemQueryResponseDTO menuItem) {
        if (this.cuisine == null) {
            this.cuisine = new ArrayList<>();
        }
        this.cuisine.add(menuItem);
    }
}

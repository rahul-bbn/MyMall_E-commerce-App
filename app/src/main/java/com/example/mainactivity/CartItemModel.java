package com.example.mainactivity;

import java.util.ArrayList;
import java.util.List;

public class CartItemModel {
    public static final int CART_ITEM=0;
    public static final int TOTAL_AMOUNT=1;

    private int type;

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    ////////////// Cart item
    private String productID;
    private String productImage;
    private String productTitle;
    private Long freeCoupen;
    private String productPrice;
    private String cuttedPrice;
    private Long productQuantity;
    private Long offersApplied;
    private Long coupensApplied;

    private Long maxQuantity,stockQuantity;
    private boolean inStock,qtyError,COD;
    private List<String> qtyIDs;
    private String selectedCoupenId;
    private String discountedPrice;

    public CartItemModel(boolean COD,int type, String productID, String productImage, String productTitle, Long freeCoupen, String productPrice, String cuttedPrice, Long productQuantity, Long offersApplied, Long coupensApplied, boolean inStock,Long maxQuantity,Long stockQuantity) {
        this.type = type;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.freeCoupen = freeCoupen;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.productQuantity = productQuantity;
        this.offersApplied = offersApplied;
        this.coupensApplied = coupensApplied;
        this.maxQuantity = maxQuantity;
        this.stockQuantity = stockQuantity;
        this.inStock=inStock;
        qtyIDs = new ArrayList<>();
        qtyError = false;
        this.COD = COD;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getSelectedCoupenId() {
        return selectedCoupenId;
    }

    public void setSelectedCoupenId(String selectedCoupenId) {
        this.selectedCoupenId = selectedCoupenId;
    }

    public boolean isQtyError() {
        return qtyError;
    }

    public void setQtyError(boolean qtyError) {
        this.qtyError = qtyError;
    }

    public Long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<String> getQtyIDs() {
        return qtyIDs;
    }

    public void setQtyIDs(List<String> qtyIDs) {
        this.qtyIDs = qtyIDs;
    }

    public Long getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Long maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Long getFreeCoupen() {
        return freeCoupen;
    }

    public void setFreeCoupen(Long freeCoupen) {
        this.freeCoupen = freeCoupen;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Long getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(Long offersApplied) {
        this.offersApplied = offersApplied;
    }

    public Long getCoupensApplied() {
        return coupensApplied;
    }

    public void setCoupensApplied(Long coupensApplied) {
        this.coupensApplied = coupensApplied;
    }

    ////////////// Cart item



//    String totalItems ;
//    String totalItemPrice;
//    String deliveryPrice;
//    String totalAmount;
//    String savedAmount;
//
//    public CartItemModel(int type, String totalItems, String totalItemPrice, String deliveryPrice, String totalAmount, String savedAmount) {
//        this.type = type;
//        this.totalItems = totalItems;
//        this.totalItemPrice = totalItemPrice;
//        this.deliveryPrice = deliveryPrice;
//        this.totalAmount = totalAmount;
//        this.savedAmount = savedAmount;
//    }
//
//    public String getTotalItems() {
//        return totalItems;
//    }
//
//    public void setTotalItems(String totalItems) {
//        this.totalItems = totalItems;
//    }
//
//    public String getTotalItemPrice() {
//        return totalItemPrice;
//    }
//
//    public void setTotalItemPrice(String totalItemPrice) {
//        this.totalItemPrice = totalItemPrice;
//    }
//
//    public String getDeliveryPrice() {
//        return deliveryPrice;
//    }
//
//    public void setDeliveryPrice(String deliveryPrice) {
//        this.deliveryPrice = deliveryPrice;
//    }
//
//    public String getTotalAmount() {
//        return totalAmount;
//    }
//
//    public void setTotalAmount(String totalAmount) {
//        this.totalAmount = totalAmount;
//    }
//
//    public String getSavedAmount() {
//        return savedAmount;
//    }
//
//    public void setSavedAmount(String savedAmount) {
//        this.savedAmount = savedAmount;
//    }

    /////////////Cart total

    private int totalItems,totalItemPrice,totalAmount,savedAmount;
    private String deliveryPrice;
    public CartItemModel(int type) {
        this.type = type;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(int totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    /////////////Cart total
}

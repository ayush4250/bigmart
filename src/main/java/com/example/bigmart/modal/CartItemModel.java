package com.example.bigmart.modal;

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

    ///Cart item
    private String productId;
    private String productimage;
    private String producttitle;
    private Long freecoupen;
    private String productprice;
    private String cutprice;
   // private String productQuantity
    private Long offersApplied;
    private Long coupensApplied;
    private boolean instock;
    private Long maxQunatity;
    private Long productQuantity;
    private String selectedCoupenId;
    private List<String> qtyIds;
    ///Cart item

    public CartItemModel(int type,String productId, String productimage, String producttitle,Long freecoupen,String productprice,String cutprice,Long offersApplied,Long coupensApplied,boolean instock,Long maxQunatity,Long productQuantity) {
        this.type = type;
        this.productId=productId;
        this.productimage = productimage;
        this.producttitle = producttitle;
        this.freecoupen=freecoupen;
        this.productprice = productprice;
        this.cutprice=cutprice;
        this.offersApplied=offersApplied;
        this.coupensApplied=coupensApplied;
        this.instock=instock;
        this.productQuantity=productQuantity;
        this.maxQunatity = maxQunatity;
        qtyIds = new ArrayList<>();
    }

    public List<String> getQtyIds() {
        return qtyIds;
    }

    public void setQtyIds(List<String> qtyIds) {
        this.qtyIds = qtyIds;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Long getMaxQunatity() {
        return maxQunatity;
    }

    public void setMaxQunatity(Long maxQunatity) {
        this.maxQunatity = maxQunatity;
    }

    public String getSelectedCoupenId() {
        return selectedCoupenId;
    }

    public void setSelectedCoupenId(String selectedCoupenId) {
        this.selectedCoupenId = selectedCoupenId;
    }

    public boolean isInstock() {
        return instock;
    }

    public void setInstock(boolean instock) {
        this.instock = instock;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getFreecoupen() {
        return freecoupen;
    }

    public void setFreecoupen(Long freecoupen) {
        this.freecoupen = freecoupen;
    }

    public String getCutprice() {
        return cutprice;
    }

    public void setCutprice(String cutprice) {
        this.cutprice = cutprice;
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

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProducttitle() {
        return producttitle;
    }

    public void setProducttitle(String producttitle) {
        this.producttitle = producttitle;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }


    ///cart total
    // private int totalItems; private String totalAmount,deliveryPrice,savedAmount;

    private int totalitems,totalItemPrice,totalAmount,savedAmount;
    private String deliveryPrice;
    public CartItemModel(int type) {
        this.type = type;
    }

    public int getTotalitems() {
        return totalitems;
    }

    public void setTotalitems(int totalitems) {
        this.totalitems = totalitems;
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
}
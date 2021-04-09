package com.example.bigmart.modal;

import java.util.ArrayList;

public class FavouriteModel {

   private String productid;
    private String resturantImage;
    private String resturantName;
    private String averagerating;
    private long totalrating;
    private boolean inStock;
    //private ArrayList<String> tags;

    public FavouriteModel(String productid,String resturantImage, String resturantName, String averagerating, long totalrating,boolean inStock) {
        this.productid=productid;
        this.resturantImage = resturantImage;
        this.resturantName = resturantName;
        this.averagerating = averagerating;
        this.totalrating = totalrating;
        this.inStock = inStock;
    }

//    public ArrayList<String> getTags() {
//        return tags;
//    }
//
//    public void setTags(ArrayList<String> tags) {
//        this.tags = tags;
//    }


    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getResturantImage() {
        return resturantImage;
    }

    public void setResturantImage(String resturantImage) {
        this.resturantImage = resturantImage;
    }

    public String getResturantName() {
        return resturantName;
    }

    public void setResturantName(String resturantName) {
        this.resturantName = resturantName;
    }

    public String getAveragerating() {
        return averagerating;
    }

    public void setAveragerating(String averagerating) {
        this.averagerating = averagerating;
    }

    public long getTotalrating() {
        return totalrating;
    }

    public void setTotalrating(long totalrating) {
        this.totalrating = totalrating;
    }
}

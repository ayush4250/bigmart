package com.example.bigmart.modal;

import java.util.List;

public class HomePageModel {
    public static final int BANNER_SLIDER = 0;
    public static final int HORIZONTAL_PRODUCT_VIEW = 1;
    public static final int GRID_PRODUCT_VIEW = 2;


    private int type;
    private String background;
    //Slider model
    private List<SliderModel> sliderModelList;



    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.sliderModelList=sliderModelList;
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }

    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }





    ////vertical slider
    private String title;
    private List<HorizontalProductModel> horizontalProductModels;
    private List<FavouriteModel> favouriteModelList;

    public HomePageModel(int type, String title,String background, List<HorizontalProductModel> horizontalProductModels,List<FavouriteModel> favouriteModelList) {

        this.type = type;
        this.title = title;
        this.background=background;
        this.horizontalProductModels = horizontalProductModels;
        this.favouriteModelList = favouriteModelList;
    }

    public List<FavouriteModel> getFavouriteModelList() {
        return favouriteModelList;
    }

    public void setFavouriteModelList(List<FavouriteModel> favouriteModelList) {
        this.favouriteModelList = favouriteModelList;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HorizontalProductModel> getHorizontalProductModels() {
        return horizontalProductModels;
    }

    public void setHorizontalProductModels(List<HorizontalProductModel> horizontalProductModels) {
        this.horizontalProductModels = horizontalProductModels;
    }

    ////vertical slider


}











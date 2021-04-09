package com.example.bigmart.modal;

public class SliderModel {
    private String banner;
    private String backgroudncolor;

    public SliderModel(String banner, String backgroudncolor) {
        this.banner = banner;
        this.backgroudncolor = backgroudncolor;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBackgroudncolor() {
        return backgroudncolor;
    }

    public void setBackgroudncolor(String backgroudncolor) {
        this.backgroudncolor = backgroudncolor;
    }
}

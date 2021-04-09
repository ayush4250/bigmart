package com.example.bigmart.modal;

public class AddressesModel {

    private String funnName;
    private String mobileno;
    private String pinCode;
    private String address;
    private Boolean selected;

    public AddressesModel(String funnName, String pinCode, String address,Boolean selected,String mobileno) {
        this.funnName = funnName;
        this.pinCode = pinCode;
        this.address = address;
        this.selected=selected;
        this.mobileno=mobileno;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getFunnName() {
        return funnName;
    }

    public void setFunnName(String funnName) {
        this.funnName = funnName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}


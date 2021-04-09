package com.example.bigmart.modal;


import java.util.Date;

public class RewardModel {
    private String type;
    private String lowerlimit,upperlimit;
    private String disOramt;
    private String coupenbody;
    private Date timestamp;
    private Boolean alreadyUsed;
    private String coupenId;

    public RewardModel(String coupenId,String type, String lowerlimit, String upperlimit, String disOramt, String coupenbody,Date timestamp,Boolean alreadyUsed) {
        this.coupenId = coupenId;
        this.type = type;
        this.lowerlimit = lowerlimit;
        this.upperlimit = upperlimit;
        this.disOramt = disOramt;
        this.timestamp = timestamp;
        this.coupenbody = coupenbody;
        this.alreadyUsed = alreadyUsed;
    }

    public String getCoupenId() {
        return coupenId;
    }

    public void setCoupenId(String coupenId) {
        this.coupenId = coupenId;
    }

    public Boolean getAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(Boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLowerlimit() {
        return lowerlimit;
    }

    public void setLowerlimit(String lowerlimit) {
        this.lowerlimit = lowerlimit;
    }

    public String getUpperlimit() {
        return upperlimit;
    }

    public void setUpperlimit(String upperlimit) {
        this.upperlimit = upperlimit;
    }

    public String getDisOramt() {
        return disOramt;
    }

    public void setDisOramt(String discount) {
        this.disOramt = discount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCoupenbody() {
        return coupenbody;
    }

    public void setCoupenbody(String coupenbody) {
        this.coupenbody = coupenbody;
    }
}





package com.firebase.authentication.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Equipment {
    private String uid = "", equipment_name="", equipment_usage, equipment_price, equipment_details, equipment_location, token, tokenRequest;
    private Date date_added;
    private Request request;
    private boolean done;

    public Equipment() {
    }

    public String getEquipment_location() {
        return equipment_location;
    }

    public void setEquipment_location(String equipment_location) {
        this.equipment_location = equipment_location;
    }

    public String getKey() {
        if(this.key != null) {
            return key;
        }else{
            return "";
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Equipment(String uid, String equipment_name, String equipment_usage, String equipment_price, String equipment_details,String equipment_location, String token, String tokenRequest, Date date_added) {
        this.uid = uid;
        this.equipment_name = equipment_name;
        this.equipment_usage = equipment_usage;
        this.equipment_price = equipment_price;
        this.equipment_details = equipment_details;
        this.equipment_location = equipment_location;
        this.token = token;
        this.tokenRequest = tokenRequest;
        this.date_added = date_added;
    }

    public String getTokenRequest() {
        return tokenRequest;
    }

    public void setTokenRequest(String tokenRequest) {
        this.tokenRequest = tokenRequest;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Equipment(String uid, String equipment_name_value, String equipment_usage_value, String equipment_price_value, String equipment_details_value, String equipment_location_value, String token, Object o, Date currentDate) {
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }

    public String getUid() {
        if(this.uid!=null){
        return uid;
    }else
        {
            return "";
        }
    }


    public String getEquipment_name() {
        return equipment_name;
    }
    
    public String getEquipment_usage() {
        return equipment_usage;
    }
    
    public String getEquipment_price() {
        return equipment_price;
    }
    
    public String getEquipment_details() {
        return equipment_details;
    }

    public Date getDate_added() {
        return date_added;
    }
}

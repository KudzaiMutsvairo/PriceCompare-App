package zw.co.pricecompareapp.models;

import java.util.ArrayList;

public class DataReceived implements java.io.Serializable{
    String error;
    String errorFlag;
    String description;
    ArrayList<Item> data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(String errorFlag) {
        this.errorFlag = errorFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Item> getData() {
        return data;
    }

    public void setData(ArrayList<Item> data) {
        this.data = data;
    }
}

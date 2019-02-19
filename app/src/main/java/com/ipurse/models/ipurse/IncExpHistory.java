package com.ipurse.models.ipurse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncExpHistory {

    @SerializedName("income_name")
    @Expose
    private int id;


    @SerializedName("expense_name")
    @Expose

    private int idExp;


    @SerializedName("operation_name")
    @Expose
    private String operationName;

    @SerializedName("date")
    @Expose
    private Long date;

    @SerializedName("value")
    @Expose
    private double value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


    public int getIdExp() {
        return idExp;
    }

    public void setIdExp(int idExp) {
        this.idExp = idExp;
    }
}

package com.ipurse.models.ipurse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletHistory {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("operation_name")
    @Expose
    private String operationName;

    @SerializedName("date")
    @Expose
    private Long date;

    @SerializedName("value")
    @Expose
    private double value;

    @SerializedName("wallet_name")
    @Expose
    private int walletId;


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

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }
}

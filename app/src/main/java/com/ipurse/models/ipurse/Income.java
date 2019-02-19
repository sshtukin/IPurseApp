package com.ipurse.models.ipurse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Income {

    @SerializedName("id")
    @Expose
    private int id;


    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("value")
    @Expose
    private Double value;


    @SerializedName("interval")
    @Expose
    private Long interval;


    @SerializedName("last_income_date")
    @Expose
    private Long lastIncomeDate;


    @SerializedName("wallet_name")
    @Expose
    private String walletName;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public Long getLastIncomeDate() {
        return lastIncomeDate;
    }

    public void setLastIncomeDate(Long lastIncomeDate) {
        this.lastIncomeDate = lastIncomeDate;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

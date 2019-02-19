package com.ipurse.models.ipurse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Expense {
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


    @SerializedName("last_expense_date")
    @Expose
    private Long lastExpenseDate;


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

    public Long getLastExpenseDate() {
        return lastExpenseDate;
    }

    public void setLastExpenseDate(Long lastExpenseDate) {
        this.lastExpenseDate = lastExpenseDate;
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

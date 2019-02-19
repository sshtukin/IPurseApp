package com.ipurse;

import com.ipurse.models.ipurse.ExchangeRates;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RatesApi {
    @GET("/API/ExRates/Rates/RUB?ParamMode=2")
    Call<ExchangeRates> getRub();

    @GET("/API/ExRates/Rates/USD?ParamMode=2")
    Call<ExchangeRates> getUSD();

    @GET("/API/ExRates/Rates/EUR?ParamMode=2")
    Call<ExchangeRates> getEUR();
}

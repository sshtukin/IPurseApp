package com.ipurse;

import com.ipurse.fragments.CurrencyConvertorFragment;
import com.ipurse.fragments.MapFragment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private Retrofit mRetrofitIPurse;
    private Retrofit mRetrofitBank;
    private Retrofit mRetrofitGoogle;

    private Retrofit buildNew(String url){
        return(new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build());
    }

    public Retrofit getInstance(String url) {
        switch (url) {
            case AuthorizationActivity.IPURSE_URL:
                if (mRetrofitIPurse == null) {
                    mRetrofitIPurse = buildNew(AuthorizationActivity.IPURSE_URL);
                }
                return mRetrofitIPurse;

            case CurrencyConvertorFragment.BANK_URL:
                if (mRetrofitBank == null) {
                    mRetrofitBank = buildNew(CurrencyConvertorFragment.BANK_URL);
                }
                return mRetrofitBank;

             case MapFragment.GOOGLE_URL:
                 if (mRetrofitGoogle == null) {
                     mRetrofitGoogle = buildNew(MapFragment.GOOGLE_URL);
                 }
                 return mRetrofitGoogle;
             default:
                 return null;
        }
    }
}

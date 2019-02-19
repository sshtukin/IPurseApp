package com.ipurse.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ipurse.R;
import com.ipurse.RatesApi;
import com.ipurse.RetrofitClient;
import com.ipurse.models.ipurse.ExchangeRates;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CurrencyConvertorFragment extends Fragment {
    private TextView mTextUSD;
    private TextView mTextEUR;
    private TextView mTextRUB;
    private EditText mEditBYN;
    private EditText mEditUSD;
    private EditText mEditEUR;
    private EditText mEditRUB;
    public static Double USD = 1.0;
    public static Double EUR = 1.0;
    public static Double RUB = 1.0;
    public final static  String BANK_URL = "http://www.nbrb.by";
    public DecimalFormat df = new DecimalFormat("#.####");
    public static final String ON_FAILURE_TAG = "RETROFIT OnFailure";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency_convertor, container, false);
        mTextEUR = view.findViewById(R.id.textViewEUR);
        mTextUSD = view.findViewById(R.id.textViewUSD);
        mTextRUB = view.findViewById(R.id.textViewRUS);
        mEditBYN = view.findViewById(R.id.editTextBYN);
        mEditUSD = view.findViewById(R.id.editTextUSD);
        mEditEUR = view.findViewById(R.id.editTextEUR);
        mEditRUB = view.findViewById(R.id.editTextRUB);
        getRates(mTextEUR, mTextUSD, mTextRUB);
        final Boolean[] mFocus = {false, false, false, false};

        mEditBYN.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (mFocus[0]) {
                    try {
                        Double val = Double.valueOf(s.toString());

                        mEditUSD.setText(df.format(val / USD));
                        mEditEUR.setText(df.format(val / EUR));
                        mEditRUB.setText(df.format(val / RUB));
                    } catch (NumberFormatException e) {
                        mEditUSD.setText("");
                        mEditEUR.setText("");
                        mEditRUB.setText("");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        mEditUSD.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (mFocus[1]) {
                    try {
                        Double val = Double.valueOf(s.toString());
                        val *= USD;
                        mEditBYN.setText(df.format(val));
                        mEditEUR.setText(df.format(val / EUR));
                        mEditRUB.setText(df.format(val / RUB));
                    } catch (NumberFormatException e) {
                        mEditBYN.setText("");
                        mEditEUR.setText("");
                        mEditRUB.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });

        mEditEUR.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (mFocus[2]) {
                    try {
                        Double val = Double.valueOf(s.toString());
                        val *= EUR;
                        mEditBYN.setText(df.format(val));
                        mEditUSD.setText(df.format(val / USD));
                        mEditRUB.setText(df.format(val / RUB));
                    } catch (NumberFormatException e) {
                        mEditBYN.setText("");
                        mEditUSD.setText("");
                        mEditRUB.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });


        mEditRUB.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (mFocus[3]) {
                    try {
                        Double val = Double.valueOf(s.toString());
                        val *= RUB;
                        mEditBYN.setText(df.format(val));
                        mEditEUR.setText(df.format(val / EUR));
                        mEditUSD.setText(df.format(val / USD));
                    } catch (NumberFormatException e) {
                        mEditBYN.setText("");
                        mEditUSD.setText("");
                        mEditEUR.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });


        mEditBYN.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mFocus[0] = b;
            }
        });

        mEditUSD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mFocus[1] = b;
            }
        });

        mEditEUR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mFocus[2] = b;
            }
        });

        mEditRUB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mFocus[3] = b;
            }
        });

        return view;
    }

    public static void getRates(final TextView textEUR, final TextView textUSD, final TextView textRUB){
        Retrofit retrofit = new RetrofitClient().getInstance(BANK_URL);
        RatesApi ratesApi = retrofit.create(RatesApi.class);
        Call<ExchangeRates> call = ratesApi.getEUR();
        call.enqueue(new Callback<ExchangeRates>() {
            @Override
            public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                ExchangeRates exchangeRates = response.body();
                textEUR.setText("EUR: " + exchangeRates.getCurOfficialRate().toString());
                EUR = exchangeRates.getCurOfficialRate();
            }

            @Override
            public void onFailure(Call<ExchangeRates> call, Throwable t) {
                Log.e(ON_FAILURE_TAG, t.toString());
            }
        });

        call = ratesApi.getUSD();
        call.enqueue(new Callback<ExchangeRates>() {
            @Override
            public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                ExchangeRates exchangeRates = response.body();
                textUSD.setText("USD: " + exchangeRates.getCurOfficialRate().toString());
                USD = exchangeRates.getCurOfficialRate();
            }

            @Override
            public void onFailure(Call<ExchangeRates> call, Throwable t) {
                Log.wtf(ON_FAILURE_TAG, t.toString());
            }
        });

        call = ratesApi.getRub();
        call.enqueue(new Callback<ExchangeRates>() {
            @Override
            public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                ExchangeRates exchangeRates = response.body();
                Double rub = Double.valueOf(exchangeRates.getCurOfficialRate()) / 100;
                textRUB.setText("RUB: " + rub.toString().substring(0, 6));
                RUB = exchangeRates.getCurOfficialRate() / 100;
            }

            @Override
            public void onFailure(Call<ExchangeRates> call, Throwable t) {
                Log.e(ON_FAILURE_TAG, t.toString());
            }
        });
    }
}

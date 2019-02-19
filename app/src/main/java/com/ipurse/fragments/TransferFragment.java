package com.ipurse.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ipurse.AuthorizationActivity;
import com.ipurse.IPurseAPI;
import com.ipurse.R;
import com.ipurse.RetrofitClient;
import com.ipurse.models.ipurse.LoginResponse;
import com.ipurse.models.ipurse.Wallet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransferFragment extends Fragment {
    private Spinner mSpinnerFrom;
    private Spinner mSpinnerTo;
    private Button mButton;
    private EditText mEditText;
    ArrayList<String> wallets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        wallets = new ArrayList<>();
        for (Wallet w : WalletsFragment.mWalletList){
            wallets.add(w.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, wallets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerFrom = view.findViewById(R.id.spinner_from);
        mSpinnerTo = view.findViewById(R.id.spinner_to);
        mButton = view.findViewById(R.id.transfer_button);
        mEditText = view.findViewById(R.id.amount);
        mSpinnerFrom.setAdapter(adapter);
        mSpinnerTo.setAdapter(adapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedFrom = mSpinnerFrom.getSelectedItem().toString();
                String selectedTo = mSpinnerTo.getSelectedItem().toString();
                if (!TextUtils.isEmpty(mEditText.getText().toString().replace(" ", ""))) {
                    Double amount = Double.valueOf(String.valueOf(mEditText.getText()));
                    if (!selectedFrom.equals(selectedTo)) {
                        Wallet mWalletFrom = new Wallet();
                        Wallet mWalletTo = new Wallet();

                        for (Wallet w : WalletsFragment.mWalletList) {
                            if (w.getName().equals(selectedFrom))
                                mWalletFrom = w;
                            if (w.getName().equals(selectedTo))
                                mWalletTo = w;
                        }
                        if (mWalletFrom.getSum() >= amount) {

                            String currency = mWalletFrom.getCurrency();
                            Double sum = 0.0;
                            if (currency.equals("BYN")){
                                sum = amount;
                            }
                            if (currency.equals("USD")){
                                sum = amount * CurrencyConvertorFragment.USD;
                            }
                            if (currency.equals("EUR")){
                                sum = amount * CurrencyConvertorFragment.EUR;
                            }
                            if (currency.equals("RUB")){
                                sum = amount * CurrencyConvertorFragment.RUB;
                            }
                            Double new_sum = 0.0;
                            String target_currency = mWalletTo.getCurrency();
                            if (target_currency.equals("BYN")){
                                new_sum = sum;
                            }
                            if (target_currency.equals("USD")){
                                new_sum = sum / CurrencyConvertorFragment.USD;
                            }
                            if (target_currency.equals("EUR")){
                                new_sum = sum / CurrencyConvertorFragment.EUR;
                            }
                            if (target_currency.equals("RUB")){
                                new_sum = sum / CurrencyConvertorFragment.RUB;
                            }
                            new_sum = new_sum * 1000;
                            Long s = Math.round(new_sum);
                            new_sum = s /1000.0;

                            Retrofit retrofit = new RetrofitClient().getInstance(AuthorizationActivity.IPURSE_URL);
                            IPurseAPI iPurseAPI = retrofit.create(IPurseAPI.class);
                            Call<LoginResponse> call = iPurseAPI.changeWallet(
                                    "Token " + AuthorizationActivity.token,
                                    mWalletFrom.getName(),
                                    mWalletFrom.getCurrency(),
                                    String.valueOf(mWalletFrom.getSum() - amount),
                                    1,0,0,
                                    mWalletFrom.getDescription());
                            callEnqueue(call);

                           call = iPurseAPI.changeWallet(
                                    "Token " + AuthorizationActivity.token,
                                    mWalletTo.getName(),
                                    mWalletTo.getCurrency(),
                                    String.valueOf(mWalletTo.getSum() + new_sum),
                                    1, 0, 0,
                                    mWalletTo.getDescription());
                            callEnqueue(call);

                            Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                            mEditText.setText("");
                        } else {
                            Toast.makeText(getContext(), "Source wallet haven't enough money", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Source and destination wallets are equal", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    mEditText.setError("Enter value");
                }
            }

        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wallets.size() < 2){
            mButton.setEnabled(false);
        }
        else {
            mButton.setEnabled(true);
        }
    }

    public void callEnqueue(Call call){
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                    mEditText.setText("");
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
            }
        });
    }
}

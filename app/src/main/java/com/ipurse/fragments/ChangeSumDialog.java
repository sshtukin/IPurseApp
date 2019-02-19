package com.ipurse.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ipurse.AuthorizationActivity;
import com.ipurse.IPurseAPI;
import com.ipurse.R;
import com.ipurse.RetrofitClient;
import com.ipurse.models.ipurse.LoginResponse;
import com.ipurse.models.ipurse.Wallet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeSumDialog extends DialogFragment {
    private Button mRaiseButton;
    private Button mReduceButton;
    private EditText mEditText;
    public Wallet mWallet;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_sum,
                container, false);

        mRaiseButton = view.findViewById(R.id.button_raise);
        mReduceButton = view.findViewById(R.id.button_reduce);
        mEditText = view.findViewById(R.id.editText_value);

        Retrofit retrofit = new RetrofitClient().getInstance(AuthorizationActivity.IPURSE_URL);
        final IPurseAPI IPurse = retrofit.create(IPurseAPI.class);

        mRaiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double val = Double.valueOf(mEditText.getText().toString());
                Call<LoginResponse> call = IPurse.changeWallet("Token " + AuthorizationActivity.token,
                        mWallet.getName(),
                        mWallet.getCurrency(),
                        String.valueOf(val),
                        0,
                        1,
                        0,
                        mWallet.getDescription());

                call.enqueue(new Callback<LoginResponse>() {

                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getTargetFragment().getContext(),  getString(R.string.success), Toast.LENGTH_LONG).show();
                    }
                });
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                dismiss();
            }
        });


        mReduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double val = Double.valueOf(mEditText.getText().toString());
                if (val <= mWallet.getSum()) {
                    Call<LoginResponse> call = IPurse.changeWallet("Token " + AuthorizationActivity.token,
                            mWallet.getName(),
                            mWallet.getCurrency(),
                            String.valueOf(val),
                            0,
                            0,
                            1,
                            mWallet.getDescription());
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            Toast.makeText(getContext(),  getString(R.string.success), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                        }
                    });
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                    dismiss();
                }
                else{
                    Toast.makeText(getActivity(), getString(R.string.not_enough_money), Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    public static ChangeSumDialog newInstance(Wallet wallet){
        ChangeSumDialog fragment = new ChangeSumDialog();
        fragment.mWallet = wallet;
        return fragment;
    }
}

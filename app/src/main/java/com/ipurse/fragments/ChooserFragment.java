package com.ipurse.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ChooserFragment extends DialogFragment {
    private Button mEditButton;
    private Button mHistoryButton;
    private Button mDeleteButton;
    private Button mDefaultButton;
    private Button mRaiseButton;
    private Boolean isArchive = false;
    public Boolean defaultButtonOn  = false;
    public Wallet mWallet;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chooser,
                container, false);

        mEditButton = view.findViewById(R.id.edit_button);
        mHistoryButton = view.findViewById(R.id.history_button);
        mDeleteButton = view.findViewById(R.id.delete_button);
        mDefaultButton = view.findViewById(R.id.set_default);
        mRaiseButton = view.findViewById(R.id.raise);

        if(isArchive){
            mEditButton.setVisibility(View.GONE);
            mDefaultButton.setVisibility(View.GONE);
            mDeleteButton.setVisibility(View.GONE);
            mRaiseButton.setVisibility(View.GONE);
        }

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("result", "edit");
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();
            }
        });

        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", "history");
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", "delete");
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        if (!defaultButtonOn) {
            mDefaultButton.setVisibility(View.GONE);
            mRaiseButton.setVisibility(View.GONE);
        }
        else{
            mDefaultButton.setVisibility(View.VISIBLE);
            mRaiseButton.setVisibility(View.VISIBLE);
        }

        mDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new RetrofitClient().getInstance(AuthorizationActivity.IPURSE_URL);
                IPurseAPI IPurse = retrofit.create(IPurseAPI.class);
                Call<LoginResponse> call = IPurse.setAsDefault("Token " + AuthorizationActivity.token,
                                                                             mWallet.getName());
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Toast.makeText(getTargetFragment().getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.wtf("Retrofit", t.toString());
                        Toast.makeText(getTargetFragment().getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.putExtra("result", "def");
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                });
                dismiss();
            }
        });


        mRaiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("result", "raise");
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });
        return view;
    }

    public static ChooserFragment newInstance(Wallet wallet){
        ChooserFragment fragment = new ChooserFragment();
        fragment.defaultButtonOn = true;
        fragment.mWallet = wallet;
        return fragment;
    }
}

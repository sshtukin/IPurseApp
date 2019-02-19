package com.ipurse.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordFragment extends Fragment {
    private EditText mEditText;
    private Button mButton;
    private EditText mRepPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        mEditText = view.findViewById(R.id.change_pass);
        mButton = view.findViewById(R.id.change_password_button);
        mRepPass = view.findViewById(R.id.repeat_pass);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_pass = mEditText.getText().toString();
                String rep_pass = mRepPass.getText().toString();

                if (new_pass.equals(rep_pass)){
                    if (!new_pass.equals("")) {
                        Retrofit retrofit = new RetrofitClient().getInstance(AuthorizationActivity.IPURSE_URL);
                        IPurseAPI IPurse = retrofit.create(IPurseAPI.class);
                        Call<LoginResponse> call = IPurse.setNewPassword("Token " + AuthorizationActivity.token,
                                AuthorizationActivity.user.getLogin(),
                                new_pass);

                        call.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                Toast.makeText(getContext(), getString(R.string.password_changed), Toast.LENGTH_LONG).show();
                                mEditText.setText("");
                                mRepPass.setText("");
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                Log.e(t.getMessage(), t.getMessage());
                            }
                        });
                    }

                }
                else{
                    Toast.makeText(getContext(), getString(R.string.not_match), Toast.LENGTH_LONG).show();}
            }
        });
        return view;
    }
}

package com.ipurse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ipurse.models.ipurse.LoginResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {
    private EditText mLogin;
    private EditText mPassword;
    private Button mSignInButton;
    public static String login;
    private static final String SIGNUP_ACTIVITY_TAG = "Retrofit SignUpAcy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mLogin = findViewById(R.id.reg_login);
        mPassword = findViewById(R.id.reg_password);
        mSignInButton = findViewById(R.id.reg_signup_button);
        Retrofit retrofit = new RetrofitClient().getInstance(AuthorizationActivity.IPURSE_URL);
        final IPurseAPI iPurseAPI = retrofit.create(IPurseAPI.class);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(mLogin.getText().toString()).matches()) {

                    Call<LoginResponse> call = iPurseAPI.signUpUser(mLogin.getText().toString(), mPassword.getText().toString());
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.code() == 202) {
                                Toast.makeText(getApplicationContext(), getString(R.string.user_was_created), Toast.LENGTH_LONG).show();
                                finish();
                            } else Toast.makeText(getApplicationContext(), getString(R.string.registered_email), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.wtf(SIGNUP_ACTIVITY_TAG, t.toString());
                        }
                    });
                } else {
                    mLogin.setError(getString(R.string.valid_email));
                }
            }
        });
    }
}

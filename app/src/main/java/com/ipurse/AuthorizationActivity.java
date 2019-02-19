package com.ipurse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ipurse.models.ipurse.LoginResponse;
import com.ipurse.models.ipurse.User;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthorizationActivity extends AppCompatActivity {

    private EditText mLogin;
    private EditText mPasswrod;
    private Button mLoginButton;
    private Button mSignInButton;
    private Button mForgetPassword;
    public static User user;
    public DecimalFormat df = new DecimalFormat("#.####");
    public final static  String AUTH_ACTIVITY_TAG = "Retrofit AuthActivity";
    public final static  String IPURSE_URL = "http://10.0.2.2:8000"; //ALLOWED_HOSTS = ['127.0.0.1', '10.0.2.2:8000', '10.0.2.2']
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        mLogin = findViewById(R.id.login);
        mPasswrod = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.login_button);
        mSignInButton = findViewById(R.id.signup_button);
        mForgetPassword = findViewById(R.id.forgot_button);

        if (SignUpActivity.login != null){
            mLogin.setText(SignUpActivity.login);
        }

        Retrofit retrofit = new RetrofitClient().getInstance(IPURSE_URL);
        final IPurseAPI iPurseAPI = retrofit.create(IPurseAPI.class);

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = mLogin.getText().toString();
                if (!login.equals("")){
                    iPurseAPI.resetPassword(login).enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.code() == 200)
                                Toast.makeText(getApplicationContext(), getString(R.string.new_password), Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getApplicationContext(),getString(R.string.user_not_found), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e(AUTH_ACTIVITY_TAG, t.toString());
                            Toast.makeText(getApplicationContext(), getString(R.string.new_password_will_be_send), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else Toast.makeText(getApplicationContext(), getString(R.string.enter_your_email), Toast.LENGTH_LONG).show();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(mLogin.getText().toString()).matches()) {
                    user = new User();
                    user.setLogin(mLogin.getText().toString());
                    user.setPassword(mPasswrod.getText().toString());

                    iPurseAPI.loginUser(user.getLogin(), user.getPassword())
                            .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.code() == 200) {
                                token = response.body().getToken();
                                Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else Toast.makeText(getApplicationContext(), getString(R.string.wrong_user_or_pass), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e(AUTH_ACTIVITY_TAG, t.toString());
                        }
                    });
                }
                else{
                    mLogin.setError(getString(R.string.valid_email));
                }
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(AuthorizationActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}

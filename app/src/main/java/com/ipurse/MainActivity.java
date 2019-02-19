package com.ipurse;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ipurse.fragments.ChangePasswordFragment;
import com.ipurse.fragments.CurrencyConvertorFragment;
import com.ipurse.fragments.ExpenseFragment;
import com.ipurse.fragments.MapFragment;
import com.ipurse.fragments.TransferFragment;
import com.ipurse.fragments.WalletsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mNavigationView;
    private WalletsFragment mWalletsFragment;
    private CurrencyConvertorFragment mConvertorFragment;
    private ExpenseFragment mExpenseFragment;
    private TransferFragment mTransferFragment;
    private ChangePasswordFragment mChangePasswordFragment;
    private MapFragment mMapFragment;
    private static final String MAIN_ACTIVITY_TAG = "Retrofit MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWalletsFragment = new WalletsFragment();
        mExpenseFragment = new ExpenseFragment();
        mConvertorFragment = new CurrencyConvertorFragment();
        mTransferFragment = new TransferFragment();
        mChangePasswordFragment = new ChangePasswordFragment();
        mMapFragment = new MapFragment();

        setFragment(mWalletsFragment);

        mNavigationView = findViewById(R.id.nav_bot);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    mNavigationView.getMenu().setGroupCheckable(0, true, true);
                    switch (menuItem.getItemId()){
                        case R.id.wallets:
                            setFragment(mWalletsFragment);
                            return true;
                        case R.id.transfer:
                            setFragment(mTransferFragment);
                            return true;
                        case R.id.nearby:
                            setFragment(mMapFragment);
                            return true;
                        case R.id.expenses:
                            setFragment(mExpenseFragment);
                            return true;
                        case R.id.currency_converter:
                            setFragment(mConvertorFragment);
                            return true;
                        default:
                            return false;
                    }
                }
            }
        );
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Retrofit retrofit = new RetrofitClient().getInstance(AuthorizationActivity.IPURSE_URL);
                IPurseAPI iPurseAPI = retrofit.create(IPurseAPI.class);
                Call<Void> call = iPurseAPI.logoutUser("Token " + AuthorizationActivity.token);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if  (response.code() == 200)
                            finish();
                        Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(MAIN_ACTIVITY_TAG, t.toString());
                    }
                });
                break;

            case R.id.change_password:
                setFragment(mChangePasswordFragment);
                mNavigationView.getMenu().setGroupCheckable(0, false, true);
                break;
        }
        return true;
    }
}

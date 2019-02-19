package com.ipurse.fragments;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ipurse.AuthorizationActivity;
import com.ipurse.IPurseAPI;
import com.ipurse.R;
import com.ipurse.RetrofitClient;
import com.ipurse.models.ipurse.LoginResponse;
import com.ipurse.models.ipurse.Wallet;
import com.ipurse.models.ipurse.WalletHistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class WalletsFragment extends BaseFragment{
    private static final int CHANGE_SUM_CODE = 11;
    private MyAdapter mAdapter;
    public static List<Wallet> mWalletList;
    public static List<WalletHistory> mWalletHistory = new ArrayList<>();
    public Retrofit retrofit = new RetrofitClient().getInstance(AuthorizationActivity.IPURSE_URL);
    public IPurseAPI iPurseAPI = retrofit.create(IPurseAPI.class);
    public static Wallet saved_Wallet;
    public ArrayList<String> walletsFields = new ArrayList<String>() {{
        add("Wallet name: ");
        add("Sum: ");
        add("Description");
    }};

    public void initItems() {
        if(!mSwipeRefreshLayout.isRefreshing()){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mWalletList = new ArrayList<>();
        Call<List<Wallet>> call = iPurseAPI.getWallets("Token " + AuthorizationActivity.token);
        call.enqueue(new Callback<List<Wallet>>() {
            @Override
            public void onResponse(Call<List<Wallet>> call, Response<List<Wallet>> response) {
                mWalletList = response.body();
                updateRecyclerView();
                if (mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                else{
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Wallet>> call, Throwable t) {
                Log.e(ON_FAILURE_TAG, t.toString());
            }
        });
    }

    public void updateRecyclerView() {
        mAdapter = new MyAdapter();
        mAdapter.setWallets(mWalletList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    void removeItem(int position) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<LoginResponse> call = iPurseAPI.deleteWallet(
                "Token " + AuthorizationActivity.token,
                mWalletList.get(position).getName());
        callEnqueue(call);
        mWalletList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    void addItem() {
        CreateDialogFragment dialog = CreateDialogFragment.newInstance(currencyArray, walletsFields);
        dialog.setTargetFragment(this, WalletsFragment.REQUEST_CODE_CREATE);
        dialog.show(getFragmentManager(), CreateDialogFragment.TAG);
        mAdapter.notifyItemInserted(mWalletList.size());
    }

    @Override
    void editItem(int position) {
        saved_Wallet = mWalletList.get(position);
        CreateDialogFragment dialog = CreateDialogFragment.newInstance(mWalletList.get(position), currencyArray, mWalletList.get(position).getCreateDate());
        dialog.setTargetFragment(this, WalletsFragment.REQUEST_CODE_EDIT);
        dialog.show(getFragmentManager(), CreateDialogFragment.TAG);
        saved_position = position;
        updateRecyclerView();
    }

    void showHistory(int position) {
        final int save_id = mWalletList.get(position).getId();
        mProgressBar.setVisibility(View.VISIBLE);
        Call<List<WalletHistory>> call = iPurseAPI.getWalletsHisoty("Token " + AuthorizationActivity.token);
        call.enqueue(new Callback<List<WalletHistory>>() {
            @Override
            public void onResponse(Call<List<WalletHistory>> call, Response<List<WalletHistory>> response) {
                mWalletHistory = response.body();
                mProgressBar.setVisibility(View.INVISIBLE);
                List<WalletHistory> clearedWalletsHistory = new ArrayList<>();
                for (WalletHistory w : mWalletHistory){
                    if (w.getId() == save_id){
                        clearedWalletsHistory.add(w);
                    }
                }
                HistoryFragment historyFragment = HistoryFragment.newInstance(mWalletHistory);
                historyFragment.show(getFragmentManager(), "HistoryFragment");            }

            @Override
            public void onFailure(Call<List<WalletHistory>> call, Throwable t) {
                Log.e(ON_FAILURE_TAG, t.toString());
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void changeSum(int saved_position) {
        ChangeSumDialog changeSumDialog = ChangeSumDialog.newInstance(mWalletList.get(saved_position));
        changeSumDialog.setTargetFragment(this, CHANGE_SUM_CODE);
        changeSumDialog.show(getFragmentManager(), CreateDialogFragment.TAG);
    }

    @Override
    void callChooser() {
        ChooserFragment chooserFragment = ChooserFragment.newInstance(mWalletList.get(saved_position));
        chooserFragment.setTargetFragment(this, WalletsFragment.REQUEST_CODE_CHOOSER);
        chooserFragment.show(getFragmentManager(), CreateDialogFragment.TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CREATE) {
            mProgressBar.setVisibility(View.VISIBLE);
            Wallet wallet = new Wallet();
            wallet.setName(data.getStringExtra("first"));
            wallet.setCurrency(data.getStringExtra("second"));
            wallet.setSum(Double.valueOf(data.getStringExtra("third")));
            wallet.setDescription(data.getStringExtra("fourth"));
            Long date = new Date().getTime()/1000 + 60*60*3;
            wallet.setCreateDate(date);

            Call<LoginResponse> call = iPurseAPI.createWallet(
                    "Token " + AuthorizationActivity.token,
                    wallet.getName(),
                    wallet.getCurrency(),
                    String.valueOf(wallet.getSum()),
                    wallet.getDescription());
            callEnqueue(call);
            mWalletList.add(wallet);
            updateRecyclerView();
        }

        if (requestCode == REQUEST_CODE_EDIT) {
            mProgressBar.setVisibility(View.VISIBLE);
            Wallet wallet = new Wallet();
            wallet.setName(data.getStringExtra("first"));
            wallet.setCurrency(data.getStringExtra("second"));
            wallet.setSum(Double.valueOf(data.getStringExtra("third")));
            wallet.setDescription(data.getStringExtra("fourth"));
            wallet.setCreateDate(Long.valueOf(data.getStringExtra("time")));
            wallet.setDefault(saved_Wallet.getDefault());
            mWalletList.set(saved_position, wallet);
            mAdapter.notifyItemChanged(saved_position);

            Call<LoginResponse> call = iPurseAPI.changeWallet(
                    "Token " + AuthorizationActivity.token,
                    wallet.getName(),
                    wallet.getCurrency(),
                    String.valueOf(wallet.getSum()),
                    0,
                    0,
                    0,
                    wallet.getDescription());
            callEnqueue(call);
        }

        if (requestCode == REQUEST_CODE_CHOOSER) {
            if(data.getStringExtra("result").equals("edit")){
                editItem(saved_position);
            }
            if(data.getStringExtra("result").equals("history")){
                showHistory(saved_position);
            }
            if(data.getStringExtra("result").equals("delete")){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.deleting));
                builder.setMessage(getString(R.string.are_you_sure));
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(saved_position);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            if(data.getStringExtra("result").equals("raise")){
                changeSum(saved_position);
            }
            if(data.getStringExtra("result").equals("def")){
                initItems();
            }
        }
        if (requestCode == CHANGE_SUM_CODE){
            initItems();
        }
        updateRecyclerView();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        List<Wallet> mWallets = new ArrayList<>();

        private void setWallets(List<Wallet> input_wallets) {
            mWallets = input_wallets;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MyHolder(layoutInflater, parent);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            Wallet wallet = mWallets.get(position);
            holder.mFirstField.setText(wallet.getName());
            holder.mSecondField.setText(getString(R.string.sum) + String.valueOf(wallet.getSum()));
            holder.mThirdField.setText(getString(R.string.currency) + wallet.getCurrency());
            if (wallet.getDescription().isEmpty()) {
                holder.mFourthField.setVisibility(View.GONE);
            } else {
                holder.mFourthField.setVisibility(View.VISIBLE);
                holder.mFourthField.setText(getString(R.string.description) + wallet.getDescription());
            }
            holder.mFifthField.setText(getString(R.string.date_of_crration) + getNiceDateString(wallet.getCreateDate()));
        }

        @Override
        public int getItemCount() {
            return mWallets.size();
        }
    }
}

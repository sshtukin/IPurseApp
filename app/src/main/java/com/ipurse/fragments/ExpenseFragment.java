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
import com.ipurse.models.ipurse.Expense;
import com.ipurse.models.ipurse.IncExpHistory;
import com.ipurse.models.ipurse.LoginResponse;
import com.ipurse.models.ipurse.Wallet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExpenseFragment extends BaseFragment {

    private MyAdapter mAdapter;
    private List<Expense> mExpensesList;
    public DecimalFormat df = new DecimalFormat("#.#####");
    Retrofit retrofit = new RetrofitClient().getInstance(AuthorizationActivity.IPURSE_URL);
    IPurseAPI iPurseAPI = retrofit.create(IPurseAPI.class);
    public ArrayList<String> fields = new ArrayList<String>() {{
        add("Expense name: ");
        add("Sum: ");
        add("Description: ");
    }};


    @Override
    void initItems() {
        if(!mSwipeRefreshLayout.isRefreshing())
            mProgressBar.setVisibility(View.VISIBLE);
        mExpensesList  = new ArrayList<>();

        Call<List<Expense>> call = iPurseAPI.getExpense("Token " + AuthorizationActivity.token);
        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                mExpensesList = response.body();
                updateRecyclerView();
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                else mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                Log.e(ON_FAILURE_TAG, t.toString());
            }
        });
    }

    @Override
    void removeItem(int position) {
        mProgressBar.setVisibility(mRecyclerView.VISIBLE);
        IPurseAPI iPurseAPI = retrofit.create(IPurseAPI.class);
        Call<LoginResponse> call = iPurseAPI.deleteExpense(
                "Token " + AuthorizationActivity.token,
                mExpensesList.get(position).getName());
        callEnqueue(call);
        mExpensesList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    void addItem() {
        ArrayList<String> data = new ArrayList<>();
        for (Wallet temp : WalletsFragment.mWalletList){
            data.add(temp.getName());
        }

        for (Wallet temp : WalletsFragment.mWalletList){
            if (temp.getDefault()){
                Collections.swap(data, 0, data.indexOf(temp.getName()));
            }
        }

        if (data.size() != 0) {
            CreateDialogFragment dialog = CreateDialogFragment.newInstance(data, fields);
            dialog.setTargetFragment(this, ExpenseFragment.REQUEST_CODE_CREATE);
            dialog.show(getFragmentManager(), CreateDialogFragment.TAG);
            mAdapter.notifyItemInserted(mExpensesList.size());
        }
        else {
            Toast.makeText(getActivity(), getString(R.string.create_one_wallet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    void updateRecyclerView() {
        mAdapter = new MyAdapter();
        mAdapter.setExpenses(mExpensesList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    void editItem(int position) {
        ArrayList<String> data = new ArrayList<>();
        for (Wallet temp : WalletsFragment.mWalletList){
            data.add(temp.getName());
        }
        for (Wallet temp : WalletsFragment.mWalletList){
            if (temp.getDefault()){
                Collections.swap(data, 0, data.indexOf(temp.getName()));
            }
        }
        CreateDialogFragment dialog = CreateDialogFragment.newInstance(mExpensesList.get(position), data, mExpensesList.get(position).getLastExpenseDate());
        dialog.setTargetFragment(this, WalletsFragment.REQUEST_CODE_EDIT);
        dialog.show(getFragmentManager(), CreateDialogFragment.TAG);
        saved_position = position;
        updateRecyclerView();
    }

    @Override
    void callChooser() {
        ChooserFragment chooserFragment = new ChooserFragment();
        chooserFragment.setTargetFragment(this, ExpenseFragment.REQUEST_CODE_CHOOSER);
        chooserFragment.show(getFragmentManager(), CreateDialogFragment.TAG);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder>{
        List<Expense> mExpenses = new ArrayList<>();

        private void setExpenses(List<Expense> input_expense) {
            mExpenses = input_expense;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MyHolder(layoutInflater, parent);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            Expense expense = mExpenses.get(position);
            holder.mFirstField.setText(expense.getName());
            holder.mSecondField.setText(getString(R.string.sum)+ String.valueOf(expense.getValue()));
            holder.mThirdField.setText(getString(R.string.interval_oper)  + df.format(expense.getInterval() / (60.0 * 60 * 24)) + " days");
            holder.mFourthField.setText(getString(R.string.wallet_name) + expense.getWalletName());
            holder.mFifthField.setText(getString(R.string.last_date) +  WalletsFragment.getNiceDateString(expense.getLastExpenseDate()));
        }
        @Override
        public int getItemCount() {
            return mExpenses.size();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_CREATE) {
            mProgressBar.setVisibility(View.VISIBLE);
            Expense expense = new Expense();
            expense.setName(data.getStringExtra("first"));
            expense.setValue(Double.valueOf(data.getStringExtra("third")));
            Double d = Double.valueOf(data.getStringExtra("fourth"));
            expense.setInterval(Math.round(d*60*60*24));
            expense.setWalletName(data.getStringExtra("second"));
            Long date = new Date().getTime()/1000 + 60*60*3;
            expense.setLastExpenseDate(date);

            Call<LoginResponse> call = iPurseAPI.createExpense(
                    "Token " + AuthorizationActivity.token,
                    expense.getName(),
                    String.valueOf(expense.getValue()),
                    String.valueOf(expense.getInterval()),
                    expense.getWalletName());
            callEnqueue(call);
            mExpensesList.add(expense);
            updateRecyclerView();
        }


        if (requestCode == REQUEST_CODE_EDIT) {
            mProgressBar.setVisibility(View.VISIBLE);
            Expense expense = new Expense();
            expense.setName(data.getStringExtra("first"));
            expense.setValue(Double.valueOf(data.getStringExtra("third")));
            Double d = Double.valueOf(data.getStringExtra("fourth"));
            expense.setInterval(Math.round(d*60*60*24));
            expense.setWalletName(data.getStringExtra("second"));
            expense.setLastExpenseDate(Long.valueOf(data.getStringExtra("time")));
            mExpensesList.set(saved_position, expense);
            mAdapter.notifyItemChanged(saved_position);

            Call<LoginResponse> call = iPurseAPI.changeExpense(
                    "Token " + AuthorizationActivity.token,
                    expense.getName(),
                    String.valueOf(expense.getValue()),
                    String.valueOf(expense.getInterval()),
                    expense.getWalletName());
            callEnqueue(call);
        }

        if (requestCode == REQUEST_CODE_CHOOSER) {
            if(data.getStringExtra("result").equals("edit")){
                editItem(saved_position);
            }
            if(data.getStringExtra("result").equals("delete")){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.deleting);
                builder.setMessage(R.string.are_you_sure);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(saved_position);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}

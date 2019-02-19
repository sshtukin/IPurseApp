package com.ipurse.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ipurse.R;
import com.ipurse.models.ipurse.WalletHistory;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment  extends DialogFragment {
    public RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    public List<WalletHistory> walletHistories;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,
                container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view_history);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyAdapter();
        mAdapter.setWalletHistories(walletHistories);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return view;
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        public TextView mFirstField1;
        public TextView mSecondField1;
        public TextView mThirdField1;

        public MyHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.item_history, parent, false));
            mFirstField1 = itemView.findViewById(R.id.textView111);
            mSecondField1 = itemView.findViewById(R.id.textView222);
            mThirdField1 = itemView.findViewById(R.id.textView333);
        }
    }

    private class MyAdapter extends  RecyclerView.Adapter<MyHolder>{
        List<WalletHistory> mWalletHistories = new ArrayList<>();

        public void setWalletHistories(List<WalletHistory> input_expense) {
            mWalletHistories = input_expense;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MyHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            WalletHistory walletHistory = mWalletHistories.get(position);
            holder.mFirstField1.setText(walletHistory.getOperationName());
            if (!String.valueOf(walletHistory.getValue()).equals("0"))
                holder.mSecondField1.setText(String.valueOf(walletHistory.getValue()));
            else
                holder.mSecondField1.setText(" ");
            holder.mThirdField1.setText(WalletsFragment.getNiceDateString(walletHistory.getDate()));
        }
        @Override
        public int getItemCount() {
            return mWalletHistories.size();
        }
    }

    public static HistoryFragment newInstance(List<WalletHistory> walletHistories){
        HistoryFragment fragment = new HistoryFragment();
        fragment.walletHistories = walletHistories;
        return fragment;
    }
}

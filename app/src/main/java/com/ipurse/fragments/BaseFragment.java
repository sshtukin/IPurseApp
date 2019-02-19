package com.ipurse.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ipurse.R;
import com.ipurse.models.ipurse.LoginResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final int REQUEST_CODE_CREATE = 0;
    public static final int REQUEST_CODE_EDIT = 1;
    public static final int REQUEST_CODE_CHOOSER = 2;
    public RecyclerView mRecyclerView;
    public ProgressBar mProgressBar;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public int saved_position;
    public FloatingActionButton mFloatingActionButton;
    public static final String ON_FAILURE_TAG = "RETROFIT OnFailure";
    public ArrayList<String> currencyArray = new ArrayList<String>() {{
        add("USD");
        add("EUR");
        add("RUB");
        add("BYN");
    }};

    public static String getNiceDateString(Long d) {
        d -= 60*60*3;
        Date date = new Date(d*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm");
        return dateFormat.format(date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        mProgressBar = view.findViewById(R.id.progressBar);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));


        mFloatingActionButton = view.findViewById(R.id.floatingActionButton);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        initItems();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    abstract void initItems();

    abstract void removeItem(int position);

    abstract void addItem();

    abstract void updateRecyclerView();

    abstract void  editItem(int position);

    abstract void callChooser();


    @Override
    public void onRefresh() {
        initItems();
    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mFirstField;
        public TextView mSecondField;
        public TextView mThirdField;
        public TextView mFourthField;
        public TextView mFifthField;


        public MyHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.item_wallet, parent, false));
            mFirstField = itemView.findViewById(R.id.first_field);
            mSecondField = itemView.findViewById(R.id.second_field);
            mThirdField = itemView.findViewById(R.id.third_field);
            mFourthField = itemView.findViewById(R.id.fourth_field);
            mFifthField = itemView.findViewById(R.id.fifth_field);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            saved_position = getAdapterPosition();
            callChooser();
        }
    }

    public void callEnqueue(Call call){
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if ((response.code() == 200) || (response.code() == 202)) {
                    Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_message), Toast.LENGTH_LONG).show();
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(ON_FAILURE_TAG, t.toString());
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}

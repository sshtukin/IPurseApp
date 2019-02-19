package com.ipurse.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ipurse.R;
import com.ipurse.models.ipurse.Expense;
import com.ipurse.models.ipurse.Income;
import com.ipurse.models.ipurse.Wallet;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CreateDialogFragment extends DialogFragment {
    public static final String TAG = "CreateDialogFragment";
    private static final String FIRST = "FIRST";
    private static final String SECOND = "SECOND";
    private static final String THIRD = "THIRD";
    private static final String FOURTH = "FOURTH";
    public static final int CREATE_WALLET = 1;
    public static final int EDIT_WALLET = 2;
    public static final int CREATE_INCOME = 3;
    public static final int EDIT_INCOME = 4;
    public int operationType = 0;
    private EditText mFirstField;
    private EditText mThirdField;
    private EditText mFourthField;
    private Button mSaveButton;
    private Button mCancelButton;
    private Spinner spinner;
    public ArrayList<String> data;
    public ArrayList<String> fields;
    public Long time;

    public Boolean validator(){
        Boolean result = true;

        if ((operationType == CREATE_WALLET) || (operationType == EDIT_WALLET)){
            if (TextUtils.isEmpty(mFirstField.getText().toString().replace(" ", ""))){
                mFirstField.setError(getString(R.string.missing_name));
                result = false;
            }
            if (TextUtils.isEmpty(mThirdField.getText().toString().replace(" ", ""))){
                mThirdField.setError(getString(R.string.missong_sum));
                result = false;
            }
            else{
                try {
                   Double val = Double.parseDouble(mThirdField.getText().toString());
                   if (val < 0){
                       mThirdField.setError(getString(R.string.not_negative));
                       result =  false;
                   }
                } catch (NumberFormatException e) {
                    mThirdField.setError(getString(R.string.not_number));
                    result = false;
                }
            }
        }

        if ((operationType == CREATE_INCOME) || (operationType == EDIT_INCOME)){
            if (TextUtils.isEmpty(mFirstField.getText().toString().replace(" ", ""))){
                mFirstField.setError("Missing name");
                result = false;
            }

            if (TextUtils.isEmpty(mThirdField.getText().toString().replace(" ", ""))){
                mThirdField.setError(getString(R.string.missing_name));
                result = false;
            }
            else{
                try {
                    Double val = Double.parseDouble(mThirdField.getText().toString());
                    if (val < 0){
                        mThirdField.setError(getString(R.string.not_negative));
                        result =  false;
                    }
                } catch (NumberFormatException e) {
                    mThirdField.setError(getString(R.string.not_number));
                    result = false;
                }
            }

            if (TextUtils.isEmpty(mFourthField.getText().toString().replace(" ", ""))){
                mFourthField.setError(getString(R.string.mis_interval));
                result = false;
            }
            else{
                try {
                    Double.parseDouble(mFourthField.getText().toString());
                    if (Double.valueOf(mFourthField.getText().toString()) < 0.00069){
                        mFourthField.setError(getString(R.string.too_small_interv));
                        result = false;
                    }
                } catch (NumberFormatException e) {
                    mFourthField.setError(getString(R.string.not_number));
                    result = false;
                }
            }
        }
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create,
                container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        mFirstField = view.findViewById(R.id.dialog_create_first_field);
        mThirdField = view.findViewById(R.id.dialog_create_third_field);
        mFourthField = view.findViewById(R.id.dialog_create_fourth_field);
        mSaveButton = view.findViewById(R.id.dialog_create_save_button);
        mCancelButton = view.findViewById(R.id.dialog_create_cancel_button);

        if (fields != null) {
            mFirstField.setHint(fields.get(0));
            mThirdField.setHint(fields.get(1));
            mFourthField.setHint(fields.get(2));
        }
        else{
            mFirstField.setVisibility(View.GONE);
        }

        if (getArguments() != null){
            mFirstField.setText(getArguments().getString(FIRST));
            spinner.setSelection(data.indexOf((getArguments().getString(SECOND))));
            mThirdField.setText(getArguments().getString(THIRD));
            mFourthField.setText(getArguments().getString(FOURTH));
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (validator()) {
                        Intent intent = new Intent();
                        if (getTargetFragment() == null) {
                            dismiss();
                        }
                        String selected = spinner.getSelectedItem().toString();
                        intent.putExtra("first", mFirstField.getText().toString());
                        intent.putExtra("second", selected);
                        intent.putExtra("third", mThirdField.getText().toString());
                        intent.putExtra("fourth", mFourthField.getText().toString());
                        if (time != null) {
                            intent.putExtra("time", time.toString());
                        }
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        dismiss();
                    }
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public static CreateDialogFragment newInstance(ArrayList<String> arrayList, ArrayList<String> fields){
        CreateDialogFragment fragment = new CreateDialogFragment();
        fragment.data = arrayList;
        fragment.fields = fields;
        if(fields.get(0).equals("Wallet name")){
            fragment.operationType = CREATE_WALLET;
        }
        else{
            fragment.operationType = CREATE_INCOME;
        }
        return fragment;
    }

    public static CreateDialogFragment newInstance(Wallet wallet, ArrayList<String> arrayList, Long timestamp){
        CreateDialogFragment fragment = new CreateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FIRST, wallet.getName());
        bundle.putString(SECOND, wallet.getCurrency());
        bundle.putString(THIRD, String.valueOf(wallet.getSum()));
        bundle.putString(FOURTH, wallet.getDescription());
        fragment.setArguments(bundle);
        fragment.data = arrayList;
        fragment.time = timestamp;
        fragment.operationType = EDIT_WALLET;
        return fragment;
    }

    public static CreateDialogFragment newInstance(Expense expense, ArrayList<String> arrayList,  Long timestamp){
        CreateDialogFragment fragment = new CreateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FIRST, expense.getName());
        bundle.putString(SECOND, expense.getWalletName());
        bundle.putString(THIRD, String.valueOf(expense.getValue()));
        DecimalFormat df = new DecimalFormat("#.#####");
        bundle.putString(FOURTH, df.format(expense.getInterval() / (60.0 * 60 * 24)));
        fragment.setArguments(bundle);
        fragment.data = arrayList;
        fragment.time = timestamp;
        fragment.operationType = EDIT_INCOME;
        return fragment;
    }
}

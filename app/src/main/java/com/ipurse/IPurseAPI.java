package com.ipurse;

import com.ipurse.models.ipurse.Expense;
import com.ipurse.models.ipurse.IncExpHistory;
import com.ipurse.models.ipurse.Income;
import com.ipurse.models.ipurse.LoginResponse;
import com.ipurse.models.ipurse.Wallet;
import com.ipurse.models.ipurse.WalletHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IPurseAPI {

    @FormUrlEncoded
    @POST("/login/")
    Call<LoginResponse> loginUser(@Field("username") String username,
                                  @Field("password") String password);

    @POST("/logout/")
    Call<Void> logoutUser(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/create_user/")
    Call<LoginResponse> signUpUser(@Field("username") String username,
                                   @Field("password") String password);

    @POST("/get_wallets/")
    Call<List<Wallet>> getWallets(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/create_wallet/")
    Call<LoginResponse> createWallet(@Header("Authorization") String token,
                                     @Field("name") String name,
                                     @Field("currency") String currency,
                                     @Field("value") String value,
                                     @Field("description") String description);

    @FormUrlEncoded
    @POST("/delete_wallet/")
    Call<LoginResponse> deleteWallet(@Header("Authorization") String token,
                                     @Field("name") String name);

    @FormUrlEncoded
    @POST("/change_wallet/")
    Call<LoginResponse> changeWallet(@Header("Authorization") String token,
                                     @Field("name") String name,
                                     @Field("currency") String currency,
                                     @Field("value") String value,
                                     @Field("is_transfer") int isTransfer,
                                     @Field("is_raise") int isRaise,
                                     @Field("is_reduce") int isReduce,
                                     @Field("description") String description);

    @FormUrlEncoded
    @POST("/create_expense/")
    Call<LoginResponse> createExpense(@Header("Authorization") String token,
                                      @Field("name") String name,
                                      @Field("value") String value,
                                      @Field("interval") String interval,
                                      @Field("wallet_name") String wallet_name);

    @FormUrlEncoded
    @POST("/delete_expense/")
    Call<LoginResponse> deleteExpense(@Header("Authorization") String token,
                                      @Field("name") String name);

    @POST("/get_expenses/")
    Call<List<Expense>> getExpense(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/change_expense/")
    Call<LoginResponse> changeExpense(@Header("Authorization") String token,
                                      @Field("name") String name,
                                      @Field("value") String value,
                                      @Field("interval") String interval,
                                      @Field("wallet_name") String wallet_name);

    @POST("/get_wallets_history/")
    Call<List<WalletHistory>> getWalletsHisoty(@Header("Authorization") String token);


    @POST("/get_incomes_history/")
    Call<List<IncExpHistory>> getIncomeHisoty(@Header("Authorization") String token);


    @POST("/get_expenses_history/")
    Call<List<IncExpHistory>> getExpenseHisoty(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/reset_password/")
    Call<LoginResponse> resetPassword(@Field("username") String username);

    @FormUrlEncoded
    @POST("/set_new_password/")
    Call<LoginResponse> setNewPassword(@Header("Authorization") String token,
                                  @Field("username") String username,
                                  @Field("password") String password);
    @FormUrlEncoded
    @POST("/set_default_wallet/")
    Call<LoginResponse>  setAsDefault(@Header("Authorization") String token,
                                       @Field("name") String wallet);

}
package com.promact.akansh.shoppingappdemo.PinModule;

import android.app.ProgressDialog;
import android.content.Context;

import com.promact.akansh.shoppingappdemo.APIClient;
import com.promact.akansh.shoppingappdemo.APIInterface;
import com.promact.akansh.shoppingappdemo.Model.Users;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Akansh on 13-10-2017.
 */

public class PinPresenter implements PinContract.RegisterPresenter {
    PinContract.RegisterView view;

    public PinPresenter(PinContract.RegisterView view) {
        this.view = view;
    }

    @Override
    public void RegisterUser(Context context, Users users) {
        Random random = new Random();
        int id = random.nextInt(1081) + 2000;

        APIInterface apiInterface = APIClient.getClient()
                .create(APIInterface.class);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Adding the product");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<Users> usersAdd = apiInterface.addUsers(id, users);
        if (usersAdd != null) {
            usersAdd.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    view.showMessage("User has been added successfully");
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    view.showMessage("User addition failed..");
                    progressDialog.dismiss();
                }
            });
        }
    }
}

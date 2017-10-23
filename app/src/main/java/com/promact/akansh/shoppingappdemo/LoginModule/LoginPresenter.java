package com.promact.akansh.shoppingappdemo.LoginModule;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.promact.akansh.shoppingappdemo.APIClient;
import com.promact.akansh.shoppingappdemo.APIInterface;
import com.promact.akansh.shoppingappdemo.LoginCheckClass;
import com.promact.akansh.shoppingappdemo.Model.Product;
import com.promact.akansh.shoppingappdemo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Akansh on 11-10-2017.
 */

public class LoginPresenter implements LoginContract.LoginPresenter {
    private static final String TAG = "LoginPresenter";
    private String  mblNo;
    private ProgressDialog progressDialog;

    @Override
    public void checkCredentials(String username, String password, Context context) {
        new LoginCheckClass(username, password, context).execute();
    }

    private String searchForRecords(final APIInterface apiInterface, final String username, final String password, final Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Call<ResponseBody> call = apiInterface.getAllUsers();

        try {
            Response<ResponseBody> response = call.execute();

            if (response.body().contentLength() > 4) {
                mblNo = "";
                JSONObject jsonObj = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObj.names();

                for (int i=0; i<jsonArray.length(); i++) {
                    Log.d(TAG, "jsonArr: " + jsonArray.length());
                    JSONObject jsonObject = jsonObj
                            .getJSONObject(jsonArray.get(i).toString());
                    if (username.equals(jsonObject.getString("username")) && password.equals(jsonObject.getString("password"))) {
                        mblNo = jsonObject.getString("mobileno");
                    } else if (!(username.equals(jsonObject.getString("username")) && password.equals(jsonObject.getString("password")))) {
                        mblNo += "";
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mblNo;
    }
}

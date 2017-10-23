package com.promact.akansh.shoppingappdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.promact.akansh.shoppingappdemo.Model.Users;
import com.promact.akansh.shoppingappdemo.PinModule.PinActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Akansh on 17-10-2017.
 */

public class LoginCheckClass extends AsyncTask<Void, Void, String> {
    private static final String TAG = "checkLoginStatus";
    String username;
    String password;
    Context context;
    private String  mblNo = "";
    ProgressDialog progress;

    public LoginCheckClass(String username, String password, Context context) {
        this.username = username;
        this.password = password;
        this.context = context;
    }

    @Override
    protected void onPostExecute(String result) {
        if (mblNo.equals("") || mblNo.length() == 0) {
            AlertDialog.Builder builder = new AlertDialog
                    .Builder(context);

            builder.setTitle("Search Completed")
                    .setMessage("Entered credentials don't match or internet " +
                            "is down.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            Intent intent = new Intent(context,
                    PinActivity.class);
            intent.putExtra("mobileNo", result);
            intent.putExtra("authType", "login");

            context.startActivity(intent);
        }
        progress.dismiss();

        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(context, "Loading", "PleaseWait", true);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        APIInterface apiInterface = APIClient.getClient()
                .create(APIInterface.class);
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

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

public class RegisterCheckClass extends AsyncTask<Void, Void, String> {
    private static final String TAG = "checkLoginStatus";
    Users users;
    Context context;
    String msg;
    ProgressDialog progress;

    public RegisterCheckClass(Users users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    protected void onPostExecute(String result) {
        if (msg.equals("Data Exists")) {
            AlertDialog.Builder builder = new AlertDialog
                    .Builder(context);

            builder.setTitle("Search Completed")
                    .setMessage("Entered mobile is already registered")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            Intent intent = new Intent(context, PinActivity.class);
            intent.putExtra("username", users.getUsername());
            intent.putExtra("password", users.getPassword());
            intent.putExtra("mobileNo", users.getMobileno());
            intent.putExtra("authType", "register");

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
                JSONObject jsonObj = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObj.names();

                for (int i=0; i<jsonArray.length(); i++) {
                    Log.d(TAG, "jsonArr: " + jsonArray.length());
                    JSONObject jsonObject = jsonObj
                            .getJSONObject(jsonArray.get(i).toString());
                    if (jsonObject.getString("mobileno").equals(users.getMobileno()) ||
                            jsonObject.getString("username").equals(users.getUsername())) {
                        msg = "Data Exists";
                    } else if (!(users.getMobileno().equals(jsonObject.getString("mobileno")))) {
                        msg = "Data not exists";
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return msg;
    }
}

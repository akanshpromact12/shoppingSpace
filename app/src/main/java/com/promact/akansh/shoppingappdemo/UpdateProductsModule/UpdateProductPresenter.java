package com.promact.akansh.shoppingappdemo.UpdateProductsModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.promact.akansh.shoppingappdemo.APIClient;
import com.promact.akansh.shoppingappdemo.APIInterface;
import com.promact.akansh.shoppingappdemo.HomeModule.HomeActivity;
import com.promact.akansh.shoppingappdemo.Model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProductPresenter implements UpdateProductsContract.UpdateProdPresenter {
    private static final String TAG = "UpdtPresenter";
    private final UpdateProductsContract.UpdateProdView view;
    private String records;

    UpdateProductPresenter(UpdateProductsContract.UpdateProdView view) {
        this.view = view;
    }

    @Override
    public void updateProducts(Product product, Product oldProduct) {
        APIInterface apiInterface = APIClient.getClient()
                .create(APIInterface.class);

        int id = Integer.parseInt(searchForRecords(apiInterface,
                oldProduct.getProductName(), oldProduct.getProductDesc(),
                oldProduct.getSellingPrice()));
        Log.d(TAG, "id: "+id);
        Call<Product> updtCall = apiInterface.addProducts(id, product);
        updtCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                view.showMessage("data has successfully been updated.");
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                view.showMessage("data updation unsuccessful.");
            }
        });
    }

    private String searchForRecords(final APIInterface apiInterface, final String productName, final String productDesc, final double sellingPrice) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Call<ResponseBody> call = apiInterface.getAllProducts();
        try {
            Response<ResponseBody> response = call.execute();
            if (response.body().contentLength() > 4) {
                JSONObject jsonObj = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObj.names();

                for (int i=0; i<jsonArray.length(); i++) {
                    Log.d(TAG, "jsonArr: " + jsonArray.length());
                    JSONObject jsonObject = jsonObj
                            .getJSONObject(jsonArray.get(i).toString());
                    if (productName.equals(jsonObject.getString("productName")) && productDesc.equals(jsonObject.getString("productDesc")) && sellingPrice == Double.parseDouble(jsonObject.getString("sellingPrice"))) {
                        records = jsonArray.get(i).toString();
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return records;
    }

    @Override
    public void goBackToHomePage(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
}

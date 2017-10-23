package com.promact.akansh.shoppingappdemo.HomeModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.promact.akansh.shoppingappdemo.APIClient;
import com.promact.akansh.shoppingappdemo.APIInterface;
import com.promact.akansh.shoppingappdemo.AddProductsModule.AddProductsActivity;
import com.promact.akansh.shoppingappdemo.Model.Product;
import com.promact.akansh.shoppingappdemo.UpdateProductsModule.UpdateProductActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter implements HomeContract.HomePresenter {
    private static final String TAG = "HomePresenter";
    private HomeContract.HomeView view;
    private ArrayList<Product> productList = new ArrayList<>();
    private int id;

    HomePresenter(HomeContract.HomeView view) {
        this.view = view;
    }

    @Override
    public void showAllProducts(final Context context) {
        APIInterface apiInterface = APIClient.getClient()
                .create(APIInterface.class);
        Call<ResponseBody> call =  apiInterface.getAllProducts();

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Loading all products");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body().contentLength() > 4) {
                        JSONObject jsonObj = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObj.names();

                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonObj
                                    .getJSONObject(jsonArray.get(i).toString());
                            String prodName = jsonObject.getString("productName");
                            String prodDesc = jsonObject.getString("productDesc");
                            double cost = Double.parseDouble(jsonObject.getString("sellingPrice"));

                            Product product = new Product(prodName, prodDesc, cost);
                            productList.add(product);
                            view.showProducts(productList);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "failed to gather product(s). Please turn " +
                        "on your internet, if it's off.",
                        Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void addProducts(Context context) {
        Intent intent = new Intent(context, AddProductsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void updtProducts(Context context, Product products) {
        Intent intent = new Intent(context, UpdateProductActivity.class);
        intent.putExtra("prodName", products.getProductName());
        intent.putExtra("prodRating", products.getProductDesc());
        intent.putExtra("prodPrice", products.getSellingPrice());

        context.startActivity(intent);

    }

    @Override
    public void delProducts(Context context, Product product) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        String prodName = product.getProductName();
        String prodRating = product.getProductDesc();
        Double prodPrice = product.getSellingPrice();

        int id = searchForRecords(apiInterface, prodName, prodRating, prodPrice);

        Call<ResponseBody> delete = apiInterface.deleteProduct(id);
        delete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                view.showMessage("Data deleted successfully");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.showMessage("Data deletion failed!!");
            }
        });
    }

    private int searchForRecords(APIInterface apiInterface, final String productName, final String productDesc, final Double sellingPrice){
        StrictMode.ThreadPolicy policy = new StrictMode
                .ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Call<ResponseBody> delCall = apiInterface.getAllProducts();
        try {
            Response<ResponseBody> response = delCall.execute();
            if (response.body().contentLength() > 4) {
                JSONObject jsonObj = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObj.names();

                for (int i=0; i<jsonArray.length(); i++) {
                    Log.d(TAG, "jsonArr: " + jsonArray.length());
                    JSONObject jsonObject = jsonObj
                            .getJSONObject(jsonArray.get(i).toString());
                    if (productName.equals(jsonObject.getString("productName")) && productDesc.equals(jsonObject.getString("productDesc")) && sellingPrice == Double.parseDouble(jsonObject.getString("sellingPrice"))) {
                        id = Integer.parseInt(jsonArray
                                .get(i).toString());
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return id;
    }
}
package com.promact.akansh.shoppingappdemo.DeleteProduct;

import android.os.StrictMode;
import android.util.Log;

import com.promact.akansh.shoppingappdemo.APIClient;
import com.promact.akansh.shoppingappdemo.APIInterface;
import com.promact.akansh.shoppingappdemo.Model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.promact.akansh.shoppingappdemo.R.id.productName;

/**
 * Created by Akansh on 09-10-2017.
 */

public class DeleteProductPresenter implements DeleteProductContract.deletePresenter {
    private static final String TAG = "deletePresenter";
    private DeleteProductContract.deleteView view;
    private int id;

    public DeleteProductPresenter(DeleteProductContract.deleteView view) {
        this.view = view;
    }

    @Override
    public void deleteProduct(Product product) {
        APIInterface apiInterface = APIClient.getClient()
                .create(APIInterface.class);
        String prodName = product.getProductName();
        String prodRating = product.getProductDesc();
        Double prodPrice = product.getSellingPrice();

        int id = searchForRecords(apiInterface, prodName,
                prodRating, prodPrice);
        Log.d(TAG, "id to delete: " + id);
    }

    private int searchForRecords(APIInterface apiInterface, final String productName, final String productDesc, final Double sellingPrice) {
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

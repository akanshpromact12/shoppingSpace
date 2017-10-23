package com.promact.akansh.shoppingappdemo;

import com.promact.akansh.shoppingappdemo.Model.Product;
import com.promact.akansh.shoppingappdemo.Model.Users;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIInterface {
    @PUT("products/{number}.json")
    Call<Product> addProducts(@Path("number") int number, @Body Product products);

    @GET("products.json")
    Call<ResponseBody> getAllProducts();

    @GET("users.json")
    Call<ResponseBody> getAllUsers();

    @DELETE("products/{id}.json")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

    @PUT("users/{id}.json")
    Call<Users> addUsers(@Path("id") int id, @Body Users users);
}
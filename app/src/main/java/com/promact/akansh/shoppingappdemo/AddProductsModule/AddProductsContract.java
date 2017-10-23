package com.promact.akansh.shoppingappdemo.AddProductsModule;

import android.content.Context;

import com.promact.akansh.shoppingappdemo.Model.Product;

public class AddProductsContract {
    public interface addProductsView {
        void showMessage(String msg);
    }

    public interface addProductActions {
        void addProducts(Context context, Product product);
        void goBackToHomePage(Context context);
    }
}

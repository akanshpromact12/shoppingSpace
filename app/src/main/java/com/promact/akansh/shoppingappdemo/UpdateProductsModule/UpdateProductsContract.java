package com.promact.akansh.shoppingappdemo.UpdateProductsModule;

import android.content.Context;

import com.promact.akansh.shoppingappdemo.Model.Product;

import java.util.ArrayList;

/**
 * Created by Akansh on 07-10-2017.
 */

class UpdateProductsContract {
    interface UpdateProdView {
        void showMessage(String msg);
    }

    interface UpdateProdPresenter {
        void updateProducts(Product product, Product oldProduct);
        void goBackToHomePage(Context context);
    }
}

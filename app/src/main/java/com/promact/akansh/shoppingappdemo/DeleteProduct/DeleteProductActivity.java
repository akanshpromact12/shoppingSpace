package com.promact.akansh.shoppingappdemo.DeleteProduct;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Akansh on 09-10-2017.
 */

public class DeleteProductActivity extends AppCompatActivity {
    String name;
    String rating;
    Double price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        name = getIntent().getStringExtra("prodName");
        rating = getIntent().getStringExtra("prodRating");
        price = getIntent().getDoubleExtra("prodPrice", 0.0);
    }
}

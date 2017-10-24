package com.promact.akansh.shoppingappdemo.UpdateProductsModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.promact.akansh.shoppingappdemo.HomeModule.HomeActivity;
import com.promact.akansh.shoppingappdemo.LoginModule.LoginActivity;
import com.promact.akansh.shoppingappdemo.Model.Product;
import com.promact.akansh.shoppingappdemo.R;

import java.util.Locale;

public class UpdateProductActivity extends AppCompatActivity implements UpdateProductsContract.UpdateProdView {
    String name;
    String rating;
    Double price;
    final String TAG = "updateAct";
    EditText prodName;
    RatingBar prodRating;
    EditText prodPrice;
    Button update;
    ImageView back;
    TextView heading;
    ImageView updtProduct, logout;
    UpdateProductPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        presenter = new UpdateProductPresenter(this);
        name = getIntent().getStringExtra("prodName");
        rating = getIntent().getStringExtra("prodRating");
        price = getIntent().getDoubleExtra("prodPrice", 0.0);
        prodName = (EditText) findViewById(R.id.txtBoxUpdtName);
        prodRating = (RatingBar) findViewById(R.id.ratingBarUpdtRating);
        prodPrice = (EditText) findViewById(R.id.txtBoxUpdtCost);
        update = (Button) findViewById(R.id.btnUpdtProd);
        back = (ImageView) findViewById(R.id.backButton);
        heading = (TextView) findViewById(R.id.heading);
        updtProduct = (ImageView) findViewById(R.id.addProductButton);
        logout = (ImageView) findViewById(R.id.logout);

        prodName.setText(name);
        prodRating.setRating(Float.parseFloat(rating));
        prodPrice.setText(price.toString());
        heading.setText(getString(R.string.update));
        updtProduct.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.goBackToHomePage(UpdateProductActivity.this);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "name: " + prodName.getText().toString() + " rating: " +
                        prodRating.getRating() + " price: " + prodPrice
                        .getText().toString());

                final Product products = new Product(prodName.getText().toString(),
                        "" + prodRating.getRating(),
                        Double.parseDouble(prodPrice.getText().toString()));
                final Product productOld = new Product(name, rating, price);

                final ProgressDialog dialog = ProgressDialog.show(UpdateProductActivity.this,
                        "Please Wait...", "updation of records in progress", true);
                dialog.setCancelable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.updateProducts(products, productOld);

                        dialog.dismiss();
                    }
                }).start();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("userPrefs",
                        Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent intent = new Intent(UpdateProductActivity.this,
                        LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(UpdateProductActivity.this, msg, Toast.LENGTH_SHORT)
                .show();

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

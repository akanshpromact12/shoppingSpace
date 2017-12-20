package com.promact.akansh.shoppingappdemo.AddProductsModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class AddProductsActivity extends AppCompatActivity implements AddProductsContract.addProductsView {
    public AddProductsPresenter productsPresenter;
    /*public RecyclerView productsView;*/
    public EditText prod_name;
    public EditText prod_price;
    public RatingBar prod_rating;
    public Button addProducts;
    public ImageView back;
    TextView heading;
    ImageView addProduct, logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        prod_name = (EditText) findViewById(R.id.txtBoxProdName);
        back = (ImageView) findViewById(R.id.backButton);
        prod_price = (EditText) findViewById(R.id.txtBoxProdCost);
        prod_rating = (RatingBar) findViewById(R.id.ratingBarProdRating);
        addProducts = (Button) findViewById(R.id.btnAddProd);
        productsPresenter = new AddProductsPresenter(this);
        heading = (TextView) findViewById(R.id.heading);
        addProduct = (ImageView) findViewById(R.id.addProductButton);
        logout = (ImageView) findViewById(R.id.logout);
        heading.setText(getString(R.string.add));
        addProduct.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            productsPresenter.goBackToHomePage(AddProductsActivity.this);
            }
        });

        SharedPreferences prefs = getSharedPreferences("userPrefs",
                MODE_PRIVATE);
        final String unm = prefs.getString("username", "");

        addProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = prod_name.getText().toString().trim();
                String price = prod_price.getText().toString().trim();

                if (price.equals("")) {
                    price = "0";
                }

                if (name.length() == 0) {
                    prod_name.setError("Please enter a valid product name");
                }

                if (Integer.parseInt(price) == 0) {
                    prod_price.setError("Price cannot be zero(0)");
                }

                if (!name.isEmpty() && Integer.parseInt(price) > 0) {
                    String pName = prod_name.getText().toString();
                    float pRating = prod_rating.getRating();
                    Double pCost = Double.parseDouble(prod_price.getText().toString());

                    Product product = new Product(pName, "" + pRating, pCost, unm);
                    productsPresenter.addProducts(AddProductsActivity.this, product);

                    prod_name.requestFocus();
                    prod_name.setText("");
                    prod_rating.setRating(0);
                    prod_price.setText("");
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("userPrefs",
                        Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent intent = new Intent(AddProductsActivity.this,
                        LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(AddProductsActivity.this, msg, Toast.LENGTH_SHORT).show();

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

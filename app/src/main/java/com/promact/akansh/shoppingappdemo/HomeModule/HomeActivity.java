package com.promact.akansh.shoppingappdemo.HomeModule;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.promact.akansh.shoppingappdemo.LoginModule.LoginActivity;
import com.promact.akansh.shoppingappdemo.Model.Product;
import com.promact.akansh.shoppingappdemo.adapters.ProductAdapter;
import com.promact.akansh.shoppingappdemo.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import layout.Login;

public class HomeActivity extends AppCompatActivity implements HomeContract.HomeView, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "HomeActivity";
    public RecyclerView productsView;
    private ArrayList<Product> mainProductList;
    private ProductAdapter productAdapter;
    private HomePresenter presenter;
    ImageView back;
    TextView heading;
    ImageView addProduct, logout;
    /*
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationCode;
    private boolean mVerificationInProgress;*/

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(HomeActivity.this, "In HomeActivity",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        /*FirebaseApp.initializeApp(HomeActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Log.d(TAG, "Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationCode = verificationId;
                mResendToken = token;
                Log.d(TAG, "verif Code: " + mVerificationCode +
                        " resendToken: " + mResendToken);
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+917990630414",
                60, TimeUnit.SECONDS, this, mCallbacks);*/

        productsView = (RecyclerView) findViewById(R.id.recycler_view);
        mainProductList = new ArrayList<>();
        presenter = new HomePresenter(this);
        productAdapter = new ProductAdapter(this, mainProductList, presenter);
        back = (ImageView) findViewById(R.id.backButton);
        heading = (TextView) findViewById(R.id.heading);
        addProduct = (ImageView) findViewById(R.id.addProductButton);
        logout = (ImageView) findViewById(R.id.logout);

        back.setVisibility(View.GONE);
        heading.setText(getString(R.string.app_name));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        productsView.setLayoutManager(layoutManager);
        productsView.addItemDecoration(new GridSpacingItemDecoration(2,
                dpToPx(10), true));
        productsView.setItemAnimator(new DefaultItemAnimator());
        productsView.setAdapter(productAdapter);

        presenter.showAllProducts(HomeActivity.this);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addProducts(HomeActivity.this);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,
                        LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    public void showProducts(ArrayList<Product> productList) {
        mainProductList.clear();
        mainProductList.addAll(productList);
        Log.d(TAG, "productList size: " + mainProductList.size());
        productAdapter.notifyDataSetChanged();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finishAffinity();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    //Delete showMessage() Method:
    @Override
    public void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(HomeActivity.this, "failed", Toast.LENGTH_SHORT).show();
    }
}

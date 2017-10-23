package com.promact.akansh.shoppingappdemo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.promact.akansh.shoppingappdemo.LoginModule.LoginActivity;

import layout.Login;

public class FrontPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FrontPageActivity.this,
                        LoginActivity.class);
                startActivity(intent);

                finish();
            }
        }, 4000);
    }
}

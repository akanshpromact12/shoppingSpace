package com.promact.akansh.shoppingappdemo.LoginModule;

import android.content.Context;
import com.promact.akansh.shoppingappdemo.LoginCheckClass;

public class LoginPresenter implements LoginContract.LoginPresenter {
    @Override
    public void checkCredentials(String username, String password, Context context) {
        new LoginCheckClass(username, password, context).execute();
    }
}

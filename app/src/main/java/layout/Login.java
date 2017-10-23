package layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.promact.akansh.shoppingappdemo.HomeModule.HomeActivity;
import com.promact.akansh.shoppingappdemo.LoginModule.LoginContract;
import com.promact.akansh.shoppingappdemo.LoginModule.LoginPresenter;
import com.promact.akansh.shoppingappdemo.Model.Users;
import com.promact.akansh.shoppingappdemo.PinModule.PinActivity;
import com.promact.akansh.shoppingappdemo.R;

import java.util.concurrent.TimeUnit;

public class Login extends Fragment {
    private static final String TAG = "LoginFragment";
    RelativeLayout layout;
    Drawable drawable;
    Bitmap bitmap;
    EditText username, password;
    LoginContract.LoginPresenter presenter;
    Button login;


    public Login() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        presenter = new LoginPresenter();
        drawable = getResources().getDrawable(R.drawable.market, null);
        bitmap = ((BitmapDrawable) drawable).getBitmap();
        username = (EditText) rootView.findViewById(R.id.usernameTextBox);
        password = (EditText) rootView.findViewById(R.id.pwdTextBox);
        login = (Button) rootView.findViewById(R.id.btnLogIn);
        layout = (RelativeLayout) rootView.findViewById(R.id.relLayout);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (username.length() == 0) {
                    username.setError("Please input the proper username");
                }
            }
        });

        if (username.length() == 0 && password.length() == 0) {
            login.setBackgroundColor(getResources()
                    .getColor(R.color.colorPrimaryDark, null));
            login.setClickable(false);
        } else {
            login.setBackgroundColor(getResources()
                    .getColor(R.color.colorAccent, null));
            login.setClickable(true);
        }

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (username.length() == 0 && password.length() == 0) {
                    login.setBackgroundColor(getResources()
                            .getColor(R.color.colorPrimaryDark, null));
                    login.setClickable(false);
                } else {
                    login.setBackgroundColor(getResources()
                            .getColor(R.color.colorAccent, null));
                    login.setClickable(true);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (username.length() == 0 && password.length() == 0) {
                    login.setBackgroundColor(getResources()
                            .getColor(R.color.colorPrimaryDark, null));
                    login.setClickable(false);
                } else {
                    login.setBackgroundColor(getResources()
                            .getColor(R.color.colorAccent, null));
                    login.setClickable(true);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkCredentials(username.getText().toString(),
                        password.getText().toString(),
                        rootView.getContext());
            }
        });

        return rootView;
    }
}
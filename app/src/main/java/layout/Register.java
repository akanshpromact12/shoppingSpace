package layout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.promact.akansh.shoppingappdemo.LoginModule.LoginContract;
import com.promact.akansh.shoppingappdemo.RegisterCheckClass;
import com.promact.akansh.shoppingappdemo.Model.Users;
import com.promact.akansh.shoppingappdemo.PinModule.PinActivity;
import com.promact.akansh.shoppingappdemo.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Fragment {
    private static final String TAG = "Register";
    EditText email, pwd, confirmPwd, mobile;
    Button register;

    public Register() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        email = (EditText) rootView.findViewById(R.id.reg_uname);
        pwd = (EditText) rootView.findViewById(R.id.reg_pwd);
        confirmPwd = (EditText) rootView.findViewById(R.id.reg_conf_pwd);
        mobile = (EditText) rootView.findViewById(R.id.reg_mob);
        register = (Button) rootView.findViewById(R.id.reg_submit);

        if ((checkIfEmailOrNot(email.getText().toString()) && pwd.length() != 0 &&
                confirmPwd.length() != 0 && mobile.length() != 0) &&
                (pwd.getText().toString().equals(confirmPwd.getText()
                        .toString())) && (mobile.length() == 10)) {
            register.setBackgroundColor(getResources()
                    .getColor(R.color.colorAccent, null));
            register.setClickable(true);
        } else {
            register.setBackgroundColor(getResources()
                    .getColor(R.color.colorPrimary, null));
            register.setClickable(false);
        }
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((checkIfEmailOrNot(email.getText().toString()) && pwd.length() != 0 &&
                        confirmPwd.length() != 0 && mobile.length() != 0) &&
                        (pwd.getText().toString().equals(confirmPwd.getText()
                                .toString())) && (mobile.length() == 10)) {
                    register.setBackgroundColor(getResources()
                            .getColor(R.color.colorAccent, null));
                    register.setClickable(true);
                } else {
                    register.setBackgroundColor(getResources()
                            .getColor(R.color.colorPrimary, null));
                    register.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((checkIfEmailOrNot(email.getText().toString()) && pwd.length() != 0 &&
                        confirmPwd.length() != 0 && mobile.length() != 0) &&
                        (pwd.getText().toString().equals(confirmPwd.getText()
                                .toString())) && (mobile.length() == 10)) {
                    register.setBackgroundColor(getResources()
                            .getColor(R.color.colorAccent, null));
                    register.setClickable(true);
                } else {
                    register.setBackgroundColor(getResources()
                            .getColor(R.color.colorPrimary, null));
                    register.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((checkIfEmailOrNot(email.getText().toString()) && pwd.length() != 0 &&
                        confirmPwd.length() != 0 && mobile.length() != 0) &&
                        (pwd.getText().toString().equals(confirmPwd.getText()
                                .toString())) && (mobile.length() == 10)) {
                    register.setBackgroundColor(getResources()
                            .getColor(R.color.colorAccent, null));
                    register.setClickable(true);
                } else {
                    register.setBackgroundColor(getResources()
                            .getColor(R.color.colorPrimary, null));
                    register.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!pwd.getText().toString().equals(confirmPwd.getText()
                        .toString())) {
                    confirmPwd.setError("Passwords don't match.");
                }
            }
        });
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((checkIfEmailOrNot(email.getText().toString()) && pwd.length() != 0 &&
                        confirmPwd.length() != 0 && mobile.length() != 0) &&
                        (pwd.getText().toString().equals(confirmPwd.getText()
                                .toString())) && (mobile.length() == 10)) {
                    register.setBackgroundColor(getResources()
                            .getColor(R.color.colorAccent, null));
                    register.setClickable(true);
                } else {
                    register.setBackgroundColor(getResources()
                            .getColor(R.color.colorPrimary, null));
                    register.setClickable(false);
                }

                if (mobile.length() == 10) {
                    InputMethodManager manager = (InputMethodManager)
                            getActivity().getSystemService(Context
                                    .INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(mobile.getWindowToken(), 0);
                }

                if (!(mobile.getText().toString().startsWith("9") ||
                        mobile.getText().toString().startsWith("7"))) {
                    mobile.setError("Please enter a valid mobile number");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = email.getText().toString();
                String password = pwd.getText().toString();
                String mobileNo = mobile.getText().toString();

                Users users = new Users(username, password, mobileNo);
                new RegisterCheckClass(users,
                        rootView.getContext()).execute();
            }
        });

        return rootView;
    }

    private Boolean checkIfEmailOrNot(String email) {
        String expr = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}

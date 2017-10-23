package com.promact.akansh.shoppingappdemo.PinModule;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigbangbutton.editcodeview.EditCodeListener;
import com.bigbangbutton.editcodeview.EditCodeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.promact.akansh.shoppingappdemo.HomeModule.HomeActivity;
import com.promact.akansh.shoppingappdemo.LetterSpacingEditText;
import com.promact.akansh.shoppingappdemo.Model.Users;
import com.promact.akansh.shoppingappdemo.R;
import com.promact.akansh.shoppingappdemo.SMSClasses.SmsListener;
import com.promact.akansh.shoppingappdemo.SMSClasses.SmsReceiver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PinActivity extends AppCompatActivity implements PinContract.RegisterView {
    private EditText pin;
    private String username, password, mobile;
    private Button submit;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private static final int REQUEST_SMS = 0;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationCode;
    private static final String TAG = "PinActivity";
    private PinContract.RegisterPresenter presenter;
    private String authType;
    private SmsReceiver smsReceiver;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_entry);

        FirebaseApp.initializeApp(PinActivity.this);
        mAuth = FirebaseAuth.getInstance();
        presenter = new PinPresenter(this);
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        mobile = getIntent().getStringExtra("mobileNo");
        authType = getIntent().getStringExtra("authType");
        smsReceiver = new SmsReceiver();
        Users users = new Users(username, password, mobile);

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String code) {
                Log.d(TAG, "code is: " + code);
                Toast.makeText(PinActivity.this, "Code is: " + code,
                        Toast.LENGTH_SHORT).show();
            }
        });
        if (authType.equals("register")) {
            presenter.RegisterUser(PinActivity.this, users);
        }
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

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

        Toast.makeText(PinActivity.this, "mobile: " + mobile,
                Toast.LENGTH_SHORT).show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobile,
                120, TimeUnit.SECONDS, this, mCallbacks);

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text",messageText);
                Toast.makeText(PinActivity.this,
                        "Message-OTP: "+messageText,
                        Toast.LENGTH_LONG).show();
            }
        });


        pin = (EditText) findViewById(R.id.RegPin);

        PinContract.RegisterPresenter presenter = new PinPresenter(this);
        submit = (Button) findViewById(R.id.btnSubmitPin);

        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pin.length() == 6) {
                    //pin.setFocusable(false);

                    InputMethodManager manager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(pin.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(PinActivity.this);

                progressDialog.setMessage("Please wait...");
                progressDialog.setTitle("Checking validity of code");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                //mVerificationCode
                verifyPhoneWithCode(mVerificationCode, pin.getText().toString());
            }
        });
    }

    private void verifyPhoneWithCode(String id, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider
                .getCredential(id, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(PinActivity.this,
                                    "user: " + user.getUid(),
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PinActivity.this,
                                    HomeActivity.class);
                            startActivity(intent);

                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(PinActivity.this,
                                        "Invalid code.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog
                                    .Builder(PinActivity.this);

                            builder.setTitle("Code Invalid")
                                    .setMessage("The entered code is either" +
                                            " incorrect or has expired.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            Toast.makeText(PinActivity.this,
                                    "Signin fialed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendSms(final FirebaseUser user) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasSMSPermission = checkSelfPermission(Manifest.permission.SEND_SMS);
            if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                    showMessageOKCancel("You need to allow access to Send SMS",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[] {Manifest.permission.SEND_SMS},
                                                REQUEST_SMS);

                                        Toast.makeText(PinActivity.this,
                                                "user: " + user.getUid(),
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PinActivity.this,
                                                HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[] {Manifest.permission.SEND_SMS},
                        REQUEST_SMS);
                return;
            }
            sendMySMS();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(PinActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void sendMySMS() {

        String message = getString(R.string.messageConfirmation);

        //Check if the phoneNumber is empty
        if (mobile.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
        } else {

            SmsManager sms = SmsManager.getDefault();
            // if message length is too long messages are divided
            List<String> messages = sms.divideMessage(message);
            for (String msg : messages) {
                PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
                PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
                sms.sendTextMessage("+91"+mobile, null, msg, sentIntent, null);
            }
        }
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(PinActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(smsReceiver, new IntentFilter("otp"));

        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(smsReceiver);

        super.onPause();
    }
}

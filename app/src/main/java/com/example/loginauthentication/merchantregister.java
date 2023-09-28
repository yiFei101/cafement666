package com.example.loginauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class merchantregister extends AppCompatActivity {


    TextInputLayout Fname, Lname, Email, Password;
    Button Signup;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    String fname, lname, email, password;
    String role = "Merchant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchantregister);

        Fname = findViewById(R.id.Firstname1);
        Lname = findViewById(R.id.Lastname1);
        Email = findViewById(R.id.Email22);
        Password = findViewById(R.id.Pwd1);

        Signup = findViewById(R.id.merchantsignup);

        databaseReference = FirebaseDatabase.getInstance().getReference("Merchant");
        FAuth = FirebaseAuth.getInstance();
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = Fname.getEditText().getText().toString().trim();
                lname = Lname.getEditText().getText().toString().trim();
                email = Email.getEditText().getText().toString().trim();
                password = Password.getEditText().getText().toString().trim();

                if (isValid()) {
                    // Create a ProgressDialog for the registration process
                    final ProgressDialog mDialog = new ProgressDialog(merchantregister.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registration in progress please wait......");
                    mDialog.show();

                    // Show the Lottie animation during registration
                    final AlertDialog.Builder builder = new AlertDialog.Builder(merchantregister.this);
                    builder.setCancelable(false);
                    builder.setView(R.layout.activity_loadinganimation);

                    // Create the AlertDialog and show it
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    // Find the LottieAnimationView in the custom dialog
                    LottieAnimationView lottieAnimationView = alertDialog.findViewById(R.id.lottieAnimationView);

                    // Start the animation
                    lottieAnimationView.playAnimation();

                    // Rest of your registration logic
                    FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registration successful
                                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userid);
                                final HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Role", role);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        HashMap<String, String> hashMap1 = new HashMap<>();
                                        hashMap1.put("First Name", fname);
                                        hashMap1.put("Last Name", lname);
                                        hashMap1.put("Email", email);
                                        hashMap1.put("Password", password);

                                        FirebaseDatabase.getInstance().getReference("Merchant")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(Task<Void> task) {
                                                        // Dismiss the Lottie animation and the ProgressDialog
                                                        alertDialog.dismiss();
                                                        mDialog.dismiss();

                                                        // Send email verification
                                                        FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Registration and email verification successful
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(merchantregister.this);
                                                                    builder.setMessage("You Have Registered! Make Sure to Verify your Email");
                                                                    builder.setCancelable(false);
                                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            dialog.dismiss();
                                                                            // Handle what to do after clicking OK
                                                                        }
                                                                    });
                                                                    AlertDialog Alert = builder.create();
                                                                    Alert.show();
                                                                } else {
                                                                    ReusableCodeForAll.ShowAlert(merchantregister.this, "Error", task.getException().getMessage());
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });
                            } else {
                                // Registration failed, dismiss the Lottie animation and the ProgressDialog
                                alertDialog.dismiss();
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(merchantregister.this, "Error", task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {
        Email.setErrorEnabled(false);
        Email.setError("");
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Lname.setErrorEnabled(false);
        Lname.setError("");

        Password.setErrorEnabled(false);
        Password.setError("");

        boolean isValid = false, isValidLname = false, isValidname = false, isValidemail = false, isValidpassword = false;

        if (TextUtils.isEmpty(fname)) {
            Fname.setErrorEnabled(true);
            Fname.setError("Enter First Name");
        } else {
            isValidname = true;
        }

        if (TextUtils.isEmpty(lname)) {
            Lname.setErrorEnabled(true);
            Lname.setError("Enter Last Name");
        } else {
            isValidLname = true;
        }

        if (TextUtils.isEmpty(email)) {
            Email.setErrorEnabled(true);
            Email.setError("Email is Required");
        } else {
            if (email.matches(emailpattern)) {
                isValidemail = true;
            } else {
                Email.setErrorEnabled(true);
                Email.setError("Enter a Valid Email");
            }
        }

        if (TextUtils.isEmpty(password)) {
            Password.setErrorEnabled(true);
            Password.setError("Enter Password");
        } else {
            if (password.length() < 8) {
                Password.setErrorEnabled(true);
                Password.setError("Password is weak");
            } else {
                isValidpassword = true;
            }
        }



        isValid = (isValidemail && isValidpassword && isValidname  && isValidLname) ? true : false;

        return isValid;
    }

}

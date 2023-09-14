package com.example.loginauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class StudentRegister extends AppCompatActivity {

    TextInputLayout Fname, Lname, Studid, Email, Password;
    Button Signup;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    String fname, lname, email, studid, password;
    String role = "Student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        Fname = (TextInputLayout) findViewById(R.id.Firstname);
        Lname = (TextInputLayout) findViewById(R.id.Lastname);
        Studid = (TextInputLayout) findViewById(R.id.StudentID);
        Email = (TextInputLayout) findViewById(R.id.Email);
        Password = (TextInputLayout) findViewById(R.id.Pwd);

        Signup = (Button) findViewById(R.id.StudentSignup);

        databaseReference = FirebaseDatabase.getInstance().getReference("Student");
        FAuth = FirebaseAuth.getInstance();
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = Fname.getEditText().getText().toString().trim();
                lname = Lname.getEditText().getText().toString().trim();
                studid = Studid.getEditText().getText().toString().trim();
                email = Email.getEditText().getText().toString().trim();
                password = Password.getEditText().getText().toString().trim();

                if (isValid()) {
                    final ProgressDialog mDialog = new ProgressDialog(StudentRegister.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registration in progress please wait......");
                    mDialog.show();

                    FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registration successful
                                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userid);
                                final HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Role", role);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        HashMap<String, String> hashMap1 = new HashMap<>();
                                        hashMap1.put("Student Id", studid);
                                        hashMap1.put("First Name", fname);
                                        hashMap1.put("Last Name", lname);
                                        hashMap1.put("Email", email);
                                        hashMap1.put("Password", password);

                                        FirebaseDatabase.getInstance().getReference("Student")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mDialog.dismiss();
                                                        FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Registration and email verification successful
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentRegister.this);
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
                                                                    mDialog.dismiss();
                                                                    ReusableCodeForAll.ShowAlert(StudentRegister.this, "Error", task.getException().getMessage());
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });
                            } else {
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(StudentRegister.this, "Error", task.getException().getMessage());
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
        Studid.setErrorEnabled(false);
        Studid.setError("");
        Password.setErrorEnabled(false);
        Password.setError("");

        boolean isValid = false, isValidLname = false, isValidname = false, isValidemail = false, isValidpassword = false, isValidstudentid = false;

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

        if (TextUtils.isEmpty(studid)) {
            Studid.setErrorEnabled(true);
            Studid.setError("Enter StudentID");
        } else {
            if (studid.length() < 8) {
                Studid.setErrorEnabled(true);
                Studid.setError("StudentID is weak");
            } else {
                isValidstudentid = true;
            }
        }

        isValid = (isValidemail && isValidpassword && isValidname && isValidstudentid && isValidLname) ? true : false;

        return isValid;
    }
}

package com.example.loginauthentication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StudentPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_panel);

        // Retrieve the email address from the intent
        String userEmail = getIntent().getStringExtra("userEmail");

        // Find the TextView
        TextView greetingTextView = findViewById(R.id.greetingTextView);

        // Set the user's email in the TextView
        if (userEmail != null) {
            greetingTextView.setText("Hello, " + userEmail);
        }
    }
}

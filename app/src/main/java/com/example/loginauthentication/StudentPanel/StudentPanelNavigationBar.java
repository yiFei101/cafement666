package com.example.loginauthentication.StudentPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.loginauthentication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class StudentPanelNavigationBar extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_panel_navigation_bar);
        BottomNavigationView navigationView = findViewById(R.id.student_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.Studenthome:
                fragment=new StudentHomeFragment();
                break;
            case R.id.StudentMenu:
                fragment=new StudentMenuFragment();
                break;
            case R.id.StudentOrders:
                fragment=new StudentOrdersFragment();
                break;
            case R.id.StudentFaq:
                fragment=new StudentFaqFragment();
                break;
        }
        return loadcheffragment(fragment);
    }

    private boolean loadcheffragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }
}
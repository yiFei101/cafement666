package com.example.loginauthentication.CustomerFoodPanel;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginauthentication.Customer;
import com.example.loginauthentication.R;

import com.example.loginauthentication.MerchantPanel.Merchant;
import com.example.loginauthentication.MerchantPanel.UpdateDishModel;
import com.example.loginauthentication.Customer;
import  com.example.loginauthentication.CustomerFoodPanel_BottomNavigation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class OrderDish extends AppCompatActivity {

    String RandomId, ChefID;
    ImageView imageView;
    EditText additem;
    TextView Foodname, ChefName, ChefLoaction, FoodQuantity, FoodPrice, FoodDescription;
    DatabaseReference databaseReference, dataaa, chefdata, reference, data, dataref;
    String State, City, Sub, dishname;
    int dishprice;
    String custID;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dish);

        Foodname = findViewById(R.id.food_name);
        ChefName = findViewById(R.id.chef_name);
        ChefLoaction = findViewById(R.id.chef_location);
        FoodQuantity = findViewById(R.id.food_quantity);
        FoodPrice = findViewById(R.id.food_price);
        FoodDescription = findViewById(R.id.food_description);
        imageView = findViewById(R.id.image);
        additem = findViewById(R.id.editTextNumber);
        Button incrementButton = findViewById(R.id.incrementButton);
        Button decrementButton = findViewById(R.id.decrementButton);

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Student").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer cust = dataSnapshot.getValue(Customer.class);

                RandomId = getIntent().getStringExtra("FoodMenu");
                ChefID = getIntent().getStringExtra("MerchantId");

                databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails").child(State).child(City).child(Sub).child(ChefID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                        Foodname.setText(updateDishModel.getDishes());
                        String qua = "<b>" + "Quantity: " + "</b>" + updateDishModel.getQuantity();
                        FoodQuantity.setText(Html.fromHtml(qua));
                        String ss = "<b>" + "Description: " + "</b>" + updateDishModel.getDescription();
                        FoodDescription.setText(Html.fromHtml(ss));
                        String pri = "<b>" + "Price: ₹ " + "</b>" + updateDishModel.getPrice();
                        FoodPrice.setText(Html.fromHtml(pri));

                        // Load image from URL
                        try {
                            URL imageURL = new URL(updateDishModel.getImageURL());
                            Bitmap bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                            imageView.setImageBitmap(bmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        chefdata = FirebaseDatabase.getInstance().getReference("Chef").child(ChefID);
                        chefdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Merchant merchant = dataSnapshot.getValue(Merchant.class);

                                String name = "<b>" + "Chef Name: " + "</b>" + merchant.getFname() + " " + merchant.getLname();
                                ChefName.setText(Html.fromHtml(name));
                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Cart cart = dataSnapshot.getValue(Cart.class);
                                        if (dataSnapshot.exists()) {
                                            additem.setText(cart.getDishQuantity());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                incrementButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentValue = Integer.parseInt(additem.getText().toString());
                        additem.setText(String.valueOf(currentValue + 1));
                    }
                });

                decrementButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentValue = Integer.parseInt(additem.getText().toString());
                        if (currentValue > 0) {
                            additem.setText(String.valueOf(currentValue - 1));
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

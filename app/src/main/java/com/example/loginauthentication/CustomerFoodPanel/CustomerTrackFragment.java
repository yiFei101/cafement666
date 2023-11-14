package com.example.loginauthentication.CustomerFoodPanel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.CustomerFoodPanel_BottomNavigation;
import com.example.loginauthentication.MerchantPanel.ChefFinalOrders;
import com.example.loginauthentication.MerchantPanel.ChefFinalOrders1;
import com.example.loginauthentication.R;
import com.example.loginauthentication.SendNotification.APIService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerTrackFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<CustomerFinalOrders> customerFinalOrdersList;
    private CustomerTrackAdapter adapter;
    private DatabaseReference databaseReference;
    private TextView grandtotal, Address, Status;
    private Button Order, preparedButton;
    private LinearLayout total;
    private ProgressDialog progressDialog;
    String RandomUID, userid;
    String deliveryId = "oCpc4SwLVFbKO0fPdtp4R6bmDmI3";
    private APIService apiService;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Track");
        View view = inflater.inflate(R.layout.fragment_customertrack, container, false);

        recyclerView = view.findViewById(R.id.recyclefinalorders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        grandtotal = view.findViewById(R.id.Rs);
        Address = view.findViewById(R.id.addresstrack);
        Status = view.findViewById(R.id.status);
        total = view.findViewById(R.id.btnn);
        customerFinalOrdersList = new ArrayList<>();
        Order = view.findViewById(R.id.order);

        progressDialog = new ProgressDialog(getContext());
        setUpPreparedButton();

        CustomerTrackOrder();

        return view;
    }

    private void setUpPreparedButton() {
        Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                        .getReference("MerchantFinalOrders")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(RandomUID)
                        .child("Dishes");

                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            final ChefFinalOrders chefFinalOrders = dataSnapshot1.getValue(ChefFinalOrders.class);
                            HashMap<String, String> hashMap = new HashMap<>();
                            String dishid = chefFinalOrders.getDishId();
                            userid = chefFinalOrders.getUserId();
                            hashMap.put("MerchantId", chefFinalOrders.getMerchantId());
                            hashMap.put("DishId", chefFinalOrders.getDishId());
                            hashMap.put("DishName", chefFinalOrders.getDishName());
                            hashMap.put("DishPrice", chefFinalOrders.getDishPrice());
                            hashMap.put("DishQuantity", chefFinalOrders.getDishQuantity());
                            hashMap.put("RandomUID", RandomUID);
                            hashMap.put("TotalPrice", chefFinalOrders.getTotalPrice());
                            hashMap.put("UserId", chefFinalOrders.getUserId());

                            // Update data in "DeliveryShipOrders" node
                            FirebaseDatabase.getInstance().getReference("FinishOrder")
                                    .child(deliveryId)
                                    .child(RandomUID)
                                    .child("Dishes")
                                    .child(dishid)
                                    .setValue(hashMap);
                        }
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("MerchantFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                String merchantid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                hashMap1.put("Address", chefFinalOrders1.getAddress());
                                hashMap1.put("Merchant", merchantid);
                                hashMap1.put("GrandTotalPrice", chefFinalOrders1.getGrandTotalPrice());
                                hashMap1.put("RandomUID", RandomUID);
                                hashMap1.put("Status", "Order is Ready to Pick Up");
                                hashMap1.put("UserId", userid);
                                FirebaseDatabase.getInstance().getReference("FinishOrder").child(deliveryId).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // Add the code for the order success
                                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(deliveryId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                progressDialog.dismiss();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setMessage("Order has been Done");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Add the code for navigation or any other action after successful order
                                                        dialog.dismiss();
                                                        Intent b = new Intent(getContext(), CustomerFoodPanel_BottomNavigation.class);
                                                        startActivity(b);
                                                        getActivity().finish();
                                                    }
                                                });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }


    private void CustomerTrackOrder() {
        databaseReference = FirebaseDatabase.getInstance().getReference("StudentFinalOrders")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerFinalOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("StudentFinalOrders")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(snapshot.getKey())
                            .child("Dishes");

                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                CustomerFinalOrders customerFinalOrders = snapshot1.getValue(CustomerFinalOrders.class);
                                customerFinalOrdersList.add(customerFinalOrders);
                            }

                            if (customerFinalOrdersList.size() == 0) {
                                Address.setVisibility(View.INVISIBLE);
                                total.setVisibility(View.INVISIBLE);
                                Order.setVisibility(View.INVISIBLE);
                            } else {
                                Address.setVisibility(View.VISIBLE);
                                total.setVisibility(View.VISIBLE);
                                Order.setVisibility(View.VISIBLE);
                            }

                            adapter = new CustomerTrackAdapter(getContext(), customerFinalOrdersList);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StudentFinalOrders")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(snapshot.getKey())
                            .child("OtherInformation");

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            CustomerFinalOrders1 customerFinalOrders1 = dataSnapshot.getValue(CustomerFinalOrders1.class);
                            try {
                                grandtotal.setText("₱ " + customerFinalOrders1.getGrandTotalPrice());
                                Address.setText(customerFinalOrders1.getAddress());
                                Status.setText(customerFinalOrders1.getStatus());
                            } catch (Exception e) {
                                Log.d("CustomerTrackFragment", "onDataChange: " + e);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}

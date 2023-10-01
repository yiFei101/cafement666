package com.example.loginauthentication.CustomerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Import Button
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.loginauthentication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class CustomerCartAdapter extends RecyclerView.Adapter<CustomerCartAdapter.ViewHolder> {

    private Context mcontext;
    private List<Cart> cartModellist;
    static int total = 0;

    public CustomerCartAdapter(Context context, List<Cart> cartModellist) {
        this.cartModellist = cartModellist;
        this.mcontext = context;
        total = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cart_placeorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Cart cart = cartModellist.get(position);
        holder.dishname.setText(cart.getDishName());
        holder.PriceRs.setText("Price: ₹ " + cart.getPrice());
        holder.Qty.setText("× " + cart.getDishQuantity());
        holder.Totalrs.setText("Total: ₹ " + cart.getTotalprice());
        total += Integer.parseInt(cart.getTotalprice());
        holder.qtyButton.setText("Qty: " + cart.getDishQuantity()); // Set the quantity as text on the button
        final int dishprice = Integer.parseInt(cart.getPrice());

        holder.qtyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(cart.getDishQuantity());
                int totalprice = num * dishprice;
                if (num != 0) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("DishID", cart.getDishID());
                    hashMap.put("DishName", cart.getDishName());
                    hashMap.put("DishQuantity", String.valueOf(num));
                    hashMap.put("Price", String.valueOf(dishprice));
                    hashMap.put("Totalprice", String.valueOf(totalprice));
                    hashMap.put("MerchantId",cart.getMerchantId());

                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getDishID()).setValue(hashMap);
                } else {
                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getDishID()).removeValue();
                }
            }
        });
        CustomerCartFragment.grandt.setText("Grand Total: ₹ " + total);
        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal").setValue(String.valueOf(total));

    }

    @Override
    public int getItemCount() {
        return cartModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, PriceRs, Qty, Totalrs;
        Button qtyButton; //

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.Dishname);
            PriceRs = itemView.findViewById(R.id.pricers);
            Qty = itemView.findViewById(R.id.qty);
            Totalrs = itemView.findViewById(R.id.totalrs);
            qtyButton = itemView.findViewById(R.id.regularButton); // Replace with your button ID
        }
    }
}

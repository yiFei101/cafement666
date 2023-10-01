package com.example.loginauthentication.CustomerFoodPanel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import com.example.loginauthentication.MerchantPanel.UpdateDishModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.MerchantPanel.UpdateDishModel;
import com.example.loginauthentication.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder> {

    private Context mcontext;
    private List<UpdateDishModel> updateDishModellist;
    DatabaseReference databaseReference;

    public CustomerHomeAdapter(Context context, List<UpdateDishModel> updateDishModellist) {
        this.updateDishModellist = updateDishModellist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_menudish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UpdateDishModel updateDishModel = updateDishModellist.get(position);

        holder.Dishname.setText(updateDishModel.getDishes());
        String MerchantId = updateDishModel.getMerchantId();
        holder.price.setText("Price: ₱ " + updateDishModel.getPrice());

        // Load the image from Firebase Storage
        String ImageURL = updateDishModel.getImageURL(); // This should be the path or reference to the image in Firebase Storage
        Log.d("ImageURL", ImageURL);


        if (ImageURL != null && !ImageURL.isEmpty()) {
            // Create a FirebaseStorage instance
            FirebaseStorage storage = FirebaseStorage.getInstance();

            // Create a storage reference
            StorageReference storageRef = storage.getReference().child(ImageURL);

            // Download the image as a byte array
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Convert the bytes into a Bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    // Set the Bitmap to the ImageView
                    holder.imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors that occurred while fetching the image
                    holder.imageView.setImageResource(R.drawable.placeholder_image); // Set a placeholder image on failure
                }
            });
        } else {
            // Handle the case where imageURL is null or empty
            holder.imageView.setImageResource(R.drawable.placeholder_image); // Set a placeholder image
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, OrderDish.class);
                intent.putExtra("FoodDetails", updateDishModel.getRandomUID());
                intent.putExtra("MerchantId", MerchantId); // Use the merchantId variable here
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView Dishname, price;
        TextView additem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.menu_image);
            Dishname = itemView.findViewById(R.id.dishname);
            price = itemView.findViewById(R.id.dishprice);
            additem = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}
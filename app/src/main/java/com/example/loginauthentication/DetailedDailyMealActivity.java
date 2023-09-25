package com.example.loginauthentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.loginauthentication.R;
import com.example.loginauthentication.adapters.DetailedDailyAdapter;
import com.example.loginauthentication.models.DetailedDailyModel;

import java.util.ArrayList;
import java.util.List;

public class DetailedDailyMealActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<DetailedDailyModel> detailedDailyModelList;
    DetailedDailyAdapter dailyAdapter;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_daily_meal);

        String type = getIntent().getStringExtra("type");

        recyclerView = findViewById(R.id.detailed_rec);
        imageView = findViewById(R.id.detailed_image);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailedDailyModelList = new ArrayList<>();
        dailyAdapter = new DetailedDailyAdapter(detailedDailyModelList);
        recyclerView.setAdapter(dailyAdapter);

        if (type != null && type.equalsIgnoreCase("carinderia")){

            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.carinderia,"carinderia","description","50"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.carinderia,"carinderia","description","50"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.carinderia,"carinderia","description","50"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.carinderia,"carinderia","description","50"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.carinderia,"carinderia","description","50"));
            dailyAdapter.notifyDataSetChanged();
        }

        if (type != null && type.equalsIgnoreCase("burger")){

            imageView.setImageResource(R.drawable.burger);
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.burger, "burger","description","50"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.burger,"burger","description","50"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.burger,"burger","description","50"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.burger,"burger","description","50"));
            detailedDailyModelList.add(new DetailedDailyModel(R.drawable.burger,"burger","description","50"));
            dailyAdapter.notifyDataSetChanged();
        }
    }
}
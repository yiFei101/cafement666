package com.example.loginauthentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.adapters.DailyMealAdapter;
import com.example.loginauthentication.models.DailyMealModel;

import java.util.ArrayList;
import java.util.List;

public class DailyMealFragment extends Fragment {

    RecyclerView recyclerView;
    List<DailyMealModel> dailyMealModels;
    DailyMealAdapter dailyMealAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.daily_meal_fragment,container,false);

        recyclerView = root.findViewById(R.id.daily_meal_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dailyMealModels = new ArrayList<>();

        dailyMealModels.add(new DailyMealModel(R.drawable.carinderia,"carinderia","Description","carinderia"));
        dailyMealModels.add(new DailyMealModel(R.drawable.burger,"burger","Description","burger"));
        dailyMealModels.add(new DailyMealModel(R.drawable.lumpia,"lumpia","Description","lumpia"));
        dailyMealModels.add(new DailyMealModel(R.drawable.icecream,"icecream","Description","icecream"));
        dailyMealModels.add(new DailyMealModel(R.drawable.sariwa,"sariwa","Description","sariwa"));
        dailyMealModels.add(new DailyMealModel(R.drawable.chocolate,"Chocolate","Description","chocolate"));

        dailyMealAdapter = new DailyMealAdapter(getContext(),dailyMealModels);
        recyclerView.setAdapter(dailyMealAdapter);
        dailyMealAdapter.notifyDataSetChanged();

        return root;
    }
}

package com.example.fooddemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.fooddemoapp.Activities.DishesActivity;
import com.example.fooddemoapp.Models.Food;
import com.example.fooddemoapp.Models.RestCategory;
import com.example.fooddemoapp.Models.Restaurant;
import com.example.fooddemoapp.RecyclerViewAdapters.RestCategoryAdapter;
import com.example.fooddemoapp.RecyclerViewAdapters.RestaurantAdapter;

import com.example.fooddemoapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends BaseActivity implements RestCategoryAdapter.OnRestCategoryAdapterClickListener {
    // Recycler View
    private RecyclerView recyclerView;
    private RestCategoryAdapter adapter;
    private Parcelable recyclerViewState;

    private List<RestCategory> restCategoryList;

    private DatabaseReference dbRefCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // Codigo para extender el layout base hacia otras activities
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main,null,false);
        mDrawer.addView(contentView,0);


        dbRefCategories = FirebaseDatabase.getInstance().getReference("categories");

        restCategoryList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCategories);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    } // Fin de la Funcion onCreate



    @Override
    protected void onStart() {
        super.onStart();
        // Funcion temporal para crear las categorias
        //crearCategorias();
        dbRefCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                restCategoryList.clear();

                for (DataSnapshot categorySnapshot: dataSnapshot.getChildren()){
                    RestCategory category = categorySnapshot.getValue(RestCategory.class);

                    restCategoryList.add(category);
                }

                // pequeño script para ordenarlos
                Collections.sort(restCategoryList, new Comparator<RestCategory>() {
                    @Override
                    public int compare(RestCategory o1, RestCategory o2) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        int cmp = o1.getmCategoryName().compareToIgnoreCase(o2.getmCategoryName());
                        return  cmp;
                    }
                });

                adapter = new RestCategoryAdapter(MainActivity.this,restCategoryList, MainActivity.this);

                recyclerView.setAdapter(adapter);

                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    } // Fin de la funcion onStart

    private void crearCategorias(){

        String id = dbRefCategories.push().getKey();
        RestCategory category = new RestCategory(id,
                "Pescados y Mariscos",
                "Solo Cevicherias",
                "#4D3F330E",
                "https://firebasestorage.googleapis.com/v0/b/food-demo-app.appspot.com/o/img%2Frest_category%2Fcategory_background.jpg?alt=media&token=fe24612f-1089-4df0-8575-f00fc1e1972f");

        dbRefCategories.child(id).setValue(category);

    }


    @Override
    public void onRecyclerViewClick(int pos) {
        RestCategory category = restCategoryList.get(pos);
        //finish();
        Toast.makeText(this, category.getmCategoryName() + " clicked!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);

        intent.putExtra(Constants.CATEGORY_NAME, category.getmCategoryName());

        startActivity(intent);
    }
}

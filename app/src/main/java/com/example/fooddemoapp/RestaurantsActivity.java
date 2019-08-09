package com.example.fooddemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddemoapp.Activities.DishesActivity;
import com.example.fooddemoapp.Models.Restaurant;
import com.example.fooddemoapp.RecyclerViewAdapters.RestaurantAdapter;
import com.example.fooddemoapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsActivity extends BaseActivity implements RestaurantAdapter.OnRestaurantAdapterClickListener {
    // TextView
    private TextView textViewWarning;
    private TextView textViewActivityTitle;

    // Recycler View
    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private Parcelable recyclerViewState;

    //
    private String mRestCategory;

    private List<Restaurant> restaurantList;

    private DatabaseReference databaseRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // Codigo para extender el layout base hacia otras activities
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_restaurants,null,false);
        mDrawer.addView(contentView,0);

        databaseRestaurants = FirebaseDatabase.getInstance().getReference("restaurants");

        restaurantList = new ArrayList<>();

        // Recuperando Data del Intent
        Intent intent = getIntent();

        mRestCategory = intent.getStringExtra(Constants.CATEGORY_NAME);

        // Seteando los views
        textViewWarning = (TextView) findViewById(R.id.textViewWarning);
        textViewActivityTitle = (TextView) findViewById(R.id.textViewActivityTitle);

        // Set up del RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Log.d("MainActivity","onCreate Finished");

    } // Fin de la Funcion onCreate



    @Override
    protected void onStart() {
        super.onStart();

        Query query = databaseRestaurants.orderByChild("mRestCategory").equalTo(mRestCategory);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Revisamos si hay contenido en el SnapShot
                if (dataSnapshot.exists()){
                    recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                    restaurantList.clear(); // Ver la forma de no crear listas en cada momento

                    for (DataSnapshot restSnapshot : dataSnapshot.getChildren()){
                        Restaurant rest = restSnapshot.getValue(Restaurant.class);

                        restaurantList.add(rest);
                    }


                    adapter = new RestaurantAdapter(RestaurantsActivity.this,restaurantList,RestaurantsActivity.this);
                    recyclerView.setAdapter(adapter);

                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                } else {
                    textViewActivityTitle.setVisibility(View.GONE);
                    textViewWarning.setVisibility(View.VISIBLE);
                }
            } // Fin de If

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    } // Fin de la funcion onStart

    @Override
    public void onRecyclerViewClick(int pos) {
        Restaurant rest = restaurantList.get(pos);
        //finish();
        Toast.makeText(this, rest.getmRestTitle() + " clicked!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RestaurantsActivity.this, DishesActivity.class);

        intent.putExtra(Constants.REST_ID, rest.getmRestId());
        intent.putExtra(Constants.REST_TITLE,rest.getmRestTitle());

        startActivity(intent);
    } // End of onRecyclerViewClick
}


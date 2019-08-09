package com.example.fooddemoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddemoapp.BaseActivity;
import com.example.fooddemoapp.Models.Food;
import com.example.fooddemoapp.RecyclerViewAdapters.FoodAdapter;
import com.example.fooddemoapp.MainActivity;
import com.example.fooddemoapp.R;
import com.example.fooddemoapp.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class DishesActivity extends BaseActivity implements FoodAdapter.OnFoodClickListener, View.OnClickListener {

    private TextView textViewRestTitle;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;

    private FoodAdapter adapter;
    private List<Food> mFoodList = new ArrayList<>();
    private ArrayList<Integer> mFoodQuantity = new ArrayList<>();

    private DatabaseReference dbReferenceFoods;

    FloatingActionButton fab;

    // Info para pasar al Intent
    private String restId;
    private String restTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dishes);

        // Codigo para extender el layout base hacia otras activities
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_dishes,null,false);
        mDrawer.addView(contentView,0);


        // Conectando con los Views
        textViewRestTitle = (TextView) findViewById(R.id.textViewRestTitle);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),1);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Es necesario desactivar las anmaciones por defecto del recycler view
        // por que si yo quiero implementar un comportamiento especial
        // ambas animaciones se realizaran en conjunto dando cosas extrañas
        // todo esto solo funciona cuando se hace la llamada a notifyItemchanged()
        recyclerView.setItemAnimator(null);

        // Recuperando data del Intent
        Intent intent = getIntent();

        restId = intent.getStringExtra(Constants.REST_ID);
        restTitle = intent.getStringExtra(Constants.REST_TITLE);

        // Usando la data recuperada para setear el activity
        textViewRestTitle.setText(restTitle);

        dbReferenceFoods = FirebaseDatabase.getInstance().getReference("restaurants/" + restId +"/Foods");

        // Linking the buttons
        findViewById(R.id.fab).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbReferenceFoods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Para guardar el estado y no se vea que se esta reiniciando a cada momento
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                mFoodList.clear();
                mFoodQuantity.clear();

                // Leyendo las comidas y agregandolas a la lista
                for (DataSnapshot foodsSnapshot: dataSnapshot.getChildren()){
                    Food food = foodsSnapshot.getValue(Food.class);

                    mFoodList.add(food);
                    mFoodQuantity.add(0);
                }

                // Mejorar esta comparacion para setear el orden de la lista
                // Es posible agregar un nuevo campo en la lista de las comidas para que sirva como un Sorter
                // y poder configurar la posicion de los platos
                Collections.sort(mFoodList, new Comparator<Food>() {
                    @Override
                    public int compare(Food o1, Food o2) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        int cmp = o1.getFoodName().compareToIgnoreCase(o2.getFoodName());
                        return  cmp;
                    }
                });

                // El tercer argumento del Constructor del adapter hacer referencia a la implementacion de OnFoodClickListener
                // le pasaremos como cuarto argumento una lista de la cantidad de comida que se pide de cada plato
                adapter = new FoodAdapter(DishesActivity.this, mFoodList, DishesActivity.this, mFoodQuantity);

                recyclerView.setAdapter(adapter);

                // Recuperamos el estado anterior y lo seteamos para que no se vean cambios bruscos
                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            } // End of onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DishesActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    } // Fin de la funcion onStart()

    // En esta funcion debemos implementar el comportamiento del click sobre el RecyclerView
    @Override
    public void OnFoodClick(int pos) {
        Food food = mFoodList.get(pos);
        if (food.getFoodAvailability()){
            if (mFoodQuantity.get(pos) <= 0){
                Toast.makeText(this, food.getFoodName() + " was clicked", Toast.LENGTH_SHORT).show();

                View view = recyclerView.findViewHolderForAdapterPosition(pos).itemView;

                LinearLayout qtyLayout = view.findViewById(R.id.quantityLayout);

                // Agregando las animaciones
                Animation anim = AnimationUtils.loadAnimation(DishesActivity.this, R.anim.fadein);
                qtyLayout.startAnimation(anim);

                // Mostrando los botones
                qtyLayout.setVisibility(View.VISIBLE);

                //view.startAnimation(anim);

                // Adding Food Quantity
                mFoodQuantity.set( pos, 1 );

                // Actualizando el Recycler View
                //recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.getAdapter().notifyItemChanged(pos);

            } // EndOfIf
        }else{
            Toast.makeText(this,"Lo sentimos, esta comida no esta disponible por el momento",Toast.LENGTH_LONG).show();
        }

    } // OnFoodClick

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:{
                Toast.makeText(DishesActivity.this, "Estoy comprando", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DishesActivity.this, BudgetActivity.class);

                // Declaramos dos arreglos temporales para almacenar los pedidos que hayan sido seleccionados
                List<Food> foodBudgetList = new ArrayList<>();
                ArrayList<Integer> foodBudgetQuantity = new ArrayList<>();

                for (int i = 0; i < mFoodQuantity.size(); i++){
                    if (mFoodQuantity.get(i) != 0){
                        foodBudgetList.add(mFoodList.get(i));
                        foodBudgetQuantity.add(mFoodQuantity.get(i));
                    }
                }

                intent.putExtra(Constants.REST_ID, restId);
                intent.putExtra(Constants.REST_TITLE, restTitle);

                intent.putExtra("RequiredFood", (Serializable)foodBudgetList);
                intent.putIntegerArrayListExtra("RequiredQuantity", foodBudgetQuantity);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
} // Fin de la clase DishesActivity

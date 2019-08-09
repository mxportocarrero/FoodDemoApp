package com.example.fooddemoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddemoapp.BaseActivity;
import com.example.fooddemoapp.BudgetOverviewActivity;
import com.example.fooddemoapp.ListViewAdapters.FoodListAdapter;
import com.example.fooddemoapp.Models.AppConfig;
import com.example.fooddemoapp.Models.Budget;
import com.example.fooddemoapp.Models.CustomUser;
import com.example.fooddemoapp.Models.Food;
import com.example.fooddemoapp.Models.FoodForBudget;
import com.example.fooddemoapp.Models.RestForBudget;
import com.example.fooddemoapp.R;
import com.example.fooddemoapp.utils.Constants;
import com.example.fooddemoapp.utils.CustomMapView;
import com.example.fooddemoapp.utils.NumberFormater;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {
    // Enlaces a los handlers de objetos
    private TextView textViewBudgetTitle;
    private TextView textViewReqFoodTotal;
    private EditText editTextDeliveryAddress;
    private EditText editTextDeliveryPhone;
    private EditText editTextAdditionalInfo;
    private ListView listViewRequiredFood;

    // Lista de Comidas escogidas
    private List<Food> mFoodList = new ArrayList<>();
    private ArrayList<Integer> mFoodQuantity = new ArrayList<>();
    private double mBudgetTotalPrice;

    // MapView
    private CustomMapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private LongPressLocationSource longPressLocationSource;

    //Conexion a Firebase
    private DatabaseReference databaseBudgets;

    // Info del Intent
    private String mRestId;
    private String mRestTitle;

    // Utils
    private NumberFormater formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_budget);
        // Codigo para extender el layout base hacia otras activities
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_budget,null,false);
        mDrawer.addView(contentView,0);

        // Setting the reference
        databaseBudgets = FirebaseDatabase.getInstance().getReference("budgets");

        textViewBudgetTitle = (TextView) findViewById(R.id.textViewBudget);
        textViewReqFoodTotal = (TextView) findViewById(R.id.textViewReqFoodTotal);
        editTextDeliveryAddress = (EditText) findViewById(R.id.editTextDeliveryAddress);
        editTextDeliveryPhone = (EditText) findViewById(R.id.editTextDeliveryNumber);
        editTextAdditionalInfo = (EditText) findViewById(R.id.editTextAdditionalInfo);
        listViewRequiredFood = (ListView) findViewById(R.id.listViewRequiredFood);

        // Codigo para el GPS
        longPressLocationSource = new LongPressLocationSource();

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView = (CustomMapView) findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

        // Info que recibimos del Dishes Activity
        Intent intent = getIntent();
        mFoodList = (List<Food>) intent.getSerializableExtra("RequiredFood");
        mFoodQuantity = intent.getIntegerArrayListExtra("RequiredQuantity");
        mRestId = intent.getStringExtra(Constants.REST_ID);
        mRestTitle = intent.getStringExtra(Constants.REST_TITLE);


        FoodListAdapter adapter = new FoodListAdapter(this, mFoodList, mFoodQuantity);

        listViewRequiredFood.setAdapter(adapter);
        setListViewHeightBasedonChildren(listViewRequiredFood);

        // Calculating Total Price
        mBudgetTotalPrice = 0.0;
        for (int i = 0; i < mFoodQuantity.size(); i++){
            mBudgetTotalPrice += (double) mFoodQuantity.get(i) * mFoodList.get(i).getFoodPrice();
        }

        formatter = new NumberFormater();
        textViewReqFoodTotal.setText(formatter.format(mBudgetTotalPrice));

        // Linkeando los botones a los eventos listeners
        findViewById(R.id.buttonConfirmBudget).setOnClickListener(this);

    } // Fin de onCreate

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);

    } // onSaveInstanceState

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    } // End of onResume

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    } // End of onStart

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    } // End of onStop

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Colocando un marcador en el mapa
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-16.386852, -71.530839)).title("Marker"));

        // Este Condicional es necesario para mostrar MyLocation! Pero ya hicimos la validacion en el MainActivity asi que no sera necesario
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        // Obtenemos la posicion de Latitud y Longitud
        googleMap.setLocationSource(longPressLocationSource);
        googleMap.setOnMapLongClickListener(longPressLocationSource);

        // Activando el Auto Location
        googleMap.setMyLocationEnabled(true);
    } // End of onMapReady

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    // Funciones para los clicks
    // -------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonConfirmBudget:{
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Extraemos la info del usuario a partir de la DB (Duplicidad de datos)
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());

                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final CustomUser customUser = dataSnapshot.getValue(CustomUser.class);

                        DatabaseReference dbConfigRef = FirebaseDatabase.getInstance().getReference("config");

                        dbConfigRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                AppConfig appConfig = dataSnapshot.getValue(AppConfig.class);

                                //String budgetId = databaseBudgets.push().getKey(); // Obtenemos un Id unico
                                String budgetId =  formatter.budgetIdFormat(appConfig.getmAppBudgetAmount());

                                RestForBudget rest = new RestForBudget(mRestId,mRestTitle);

                                // Creando el objeto recibo para enviar a la DB
                                Budget budget = new Budget(budgetId,
                                        rest,
                                        customUser, // Info del usuario
                                        editTextDeliveryAddress.getText().toString(),
                                        editTextDeliveryPhone.getText().toString(),
                                        editTextAdditionalInfo.getText().toString().length() > 0 ? editTextAdditionalInfo.getText().toString() : "Ninguna",
                                        ServerValue.TIMESTAMP,
                                        "new",
                                        mBudgetTotalPrice,
                                        prepareFoodListForBudget());

                                databaseBudgets.child(budgetId).setValue(budget)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(BudgetActivity.this, "Su Pedido ha sido registrado exitosamente",Toast.LENGTH_LONG).show();

                                            // Cambiamos al Indent donde visualizamos los pedidos
                                            finish();

                                            Intent intent = new Intent(BudgetActivity.this, BudgetOverviewActivity.class);
                                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(BudgetActivity.this, "Su Pedido ha fallado vuelva a intentar",Toast.LENGTH_LONG).show();
                                        }
                                    });

                                Toast.makeText(BudgetActivity.this, "Confirmando Pedido", Toast.LENGTH_SHORT).show();

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



                break;
            }
            default:
                break;
        }
    } // Fin de las funciones onClick


    // Codigo para recuperar la posicion despues de presionar largo tiempo en el mapa
    //-------------------------------------------------------------------------------
    /**
     * A {@link LocationSource} which reports a new location whenever a user long presses the map
     * at
     * the point at which a user long pressed the map.
     */
    private class LongPressLocationSource implements LocationSource, GoogleMap.OnMapLongClickListener{
        private OnLocationChangedListener mListener;

        @Override
        public void onMapLongClick(LatLng coord) {
            if (mListener != null){
                Location location = new Location("LongPressLocationProvider");
                location.setLatitude(coord.latitude);
                location.setLongitude(coord.longitude);
                location.setAccuracy(100);

                // Otra opcion para guardar este dato es medianto
                //GeoPoint pero este es un feature de solo Firebase cloud

                mListener.onLocationChanged(location);

                Toast.makeText(BudgetActivity.this, location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        } // End of onMapLongClick

        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
        }

        @Override
        public void deactivate() {
            mListener = null;
        }
    }

    // Configuramos el listview dependiendo de la cantidad de items para q no haya problemas con el scrollview
    private static void setListViewHeightBasedonChildren(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null){
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i,null,listView);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    // Funcion para empaquetar nuestras listas para poder agregarlos a la DB
    private List<FoodForBudget> prepareFoodListForBudget(){
        List<FoodForBudget> foodForBudgetList = new ArrayList<>();

        // Empaquetando la comida seleccionada
        for(int i = 0; i < mFoodQuantity.size(); i++){
            FoodForBudget foodForBudget = new FoodForBudget( mFoodList.get(i), mFoodQuantity.get(i) );
            foodForBudgetList.add(foodForBudget);
        }

        return foodForBudgetList;
    }
} // End of Budget Activity

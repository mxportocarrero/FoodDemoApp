package com.example.fooddemoapp.ListViewAdapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fooddemoapp.Models.Food;
import com.example.fooddemoapp.R;
import com.example.fooddemoapp.utils.NumberFormater;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FoodListAdapter extends ArrayAdapter<Food> {
    private ArrayList<Integer> mFoodQuantity;

    public FoodListAdapter(@NonNull Context context, @NonNull List<Food> objects, @NonNull ArrayList<Integer> quantity) {
        super(context, 0, objects);
        mFoodQuantity = quantity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Food food = getItem(position);
        //Check if an existing view is being reused otherwise inflate
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_required_food,parent,false);
        }

        // Lookup view for data population
        TextView textViewReqFoodName = (TextView) convertView.findViewById(R.id.textViewReqFoodName);
        TextView textViewReqFoodQuantity = (TextView) convertView.findViewById(R.id.textViewReqFoodQuantity);
        TextView textViewReqFoodUnitPrice = (TextView) convertView.findViewById(R.id.textViewReqFoodUnitPrice);
        TextView textViewReqFoodSubTotal = (TextView) convertView.findViewById(R.id.textViewReqFoodSubTotal);

        // Nombre del Producto
        textViewReqFoodName.setText(food.getFoodName());

        // Precio Unitario
        NumberFormater formatter = new NumberFormater();
        textViewReqFoodUnitPrice.setText( formatter.format( food.getFoodPrice() ) );

        // Cantidad
        textViewReqFoodQuantity.setText( formatter.format(mFoodQuantity.get(position)) + "  x  ");

        // Subtotal
        double subTotal =  (double)mFoodQuantity.get(position) * food.getFoodPrice();
        textViewReqFoodSubTotal.setText( formatter.format(subTotal) );


        return convertView;
    }
}

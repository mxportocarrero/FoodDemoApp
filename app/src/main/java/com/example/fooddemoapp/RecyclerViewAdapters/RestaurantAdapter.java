package com.example.fooddemoapp.RecyclerViewAdapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fooddemoapp.R;
import com.example.fooddemoapp.Models.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context mCtx;
    private List<Restaurant> restaurantList;
    private OnRestaurantAdapterClickListener mOnRestaurantAdapterClickListener;

    public RestaurantAdapter(Context mCtx, List<Restaurant> restaurantList, OnRestaurantAdapterClickListener onRestaurantAdapterClickListener) {
        this.mCtx = mCtx;
        this.restaurantList = restaurantList;
        this.mOnRestaurantAdapterClickListener = onRestaurantAdapterClickListener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recycler_restaurants_layout,null);

        RestaurantViewHolder holder = new RestaurantViewHolder(view,mOnRestaurantAdapterClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int i) {
        Restaurant rest = restaurantList.get(i);

        // Actualizando los ImageViews
        Glide.with(mCtx)
                .load(rest.getmRestImgUrl())
                .into(restaurantViewHolder.imageViewRestLogo);

        restaurantViewHolder.textViewRestTitle.setText(rest.getmRestTitle());
        restaurantViewHolder.textViewRestDesc.setText(rest.getmRestDesc());

        //restaurantViewHolder.imageViewRestLogo.setImageDrawable( mCtx.getResources().getDrawable( R.drawable.resturant_icon, null ) );
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewRestLogo;
        TextView textViewRestTitle, textViewRestDesc;
        OnRestaurantAdapterClickListener onRestaurantAdapterClickListener;

        public RestaurantViewHolder(@NonNull View view, OnRestaurantAdapterClickListener onRestaurantAdapterClickListener){
            super(view);

            imageViewRestLogo = view.findViewById(R.id.imageViewRestLogo);
            textViewRestTitle = view.findViewById(R.id.textViewRestTitle);
            textViewRestDesc = view.findViewById(R.id.textViewRestDesc);
            this.onRestaurantAdapterClickListener = onRestaurantAdapterClickListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onRestaurantAdapterClickListener.onRecyclerViewClick(getAdapterPosition());
        }
    } // fin de Class RestaurantViewHolder

    // Agregando un Listener para los clicks
    public interface OnRestaurantAdapterClickListener{
        void onRecyclerViewClick(int pos);
    }


} // Fin Class RestaurantAdapter

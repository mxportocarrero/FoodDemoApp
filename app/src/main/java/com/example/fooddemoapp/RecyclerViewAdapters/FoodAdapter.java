package com.example.fooddemoapp.RecyclerViewAdapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fooddemoapp.Models.Food;
import com.example.fooddemoapp.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context mCtx;
    private List<Food> foodList;
    private ArrayList<Integer> mFoodQuantity = new ArrayList<>();

    private OnFoodClickListener mOnFoodClickListener;

    public FoodAdapter(Context mCtx, List<Food> foodList, OnFoodClickListener onFoodClickListener, ArrayList<Integer> FoodQuantity) {
        this.mCtx = mCtx;
        this.foodList = foodList;
        this.mFoodQuantity = FoodQuantity;
        this.mOnFoodClickListener = onFoodClickListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recycler_dishes_layout,null);

        FoodViewHolder foodViewHolder = new FoodViewHolder(view, mOnFoodClickListener);

        return foodViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder holder, final int pos) {
        Food food = foodList.get(pos);

        // Actualizando los ImageViews
        Glide.with(mCtx)
                .load(food.getFoodImgUrl())
                .into(holder.imageViewFoodImg);

        //holder.imageViewFoodImg.setImageDrawable(mCtx.getResources().getDrawable( R.drawable.resturant_icon, null ));

        // Revisando la disponibilidad de la comida
        if (food.getFoodAvailability())
            holder.imageViewFoodAvailability.setImageDrawable( mCtx.getResources().getDrawable(R.drawable.availability_circle, null) );
        else
            holder.imageViewFoodAvailability.setImageDrawable( mCtx.getResources().getDrawable(R.drawable.unavailability_circle, null) );


        // Actualizando los TextViews
        holder.textViewFoodName.setText(food.getFoodName());
        holder.textViewFoodDesc.setText(food.getFoodDesc());

        // Dando formato al precio
        NumberFormat formatter = new DecimalFormat("#0.00");
        holder.textViewFoodPrice.setText( "S/. " + formatter.format( food.getFoodPrice() ) );

        // Procesando la data que se mostrara en Cantidad
        int cant = mFoodQuantity.get(pos);
        if (cant != 0){
            //holder.quantityLayout.setVisibility(View.VISIBLE);
            holder.textViewFoodQuantity.setText( "x" + cant );
        }

        // Agregando una pequeña animación
        holder.imgButtonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(mCtx, R.anim.rotate);
                holder.imgButtonIncrease.startAnimation(animation);

                // Aumentamos una unidad al producto
                mFoodQuantity.set( pos, mFoodQuantity.get(pos) + 1  );

                // notificamos al adapter que ha ocurrido una actualizacion
                notifyItemChanged(pos);
            }
        });

        holder.imgButtonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(mCtx, R.anim.anti_rotate);
                holder.imgButtonDecrease.startAnimation(animation);

                // Quitamos una unidad del producto
                mFoodQuantity.set( pos, mFoodQuantity.get(pos) - 1  );

                // Si llega a cero lanzamos animacion y ocultamos los botones
                if (mFoodQuantity.get(pos) == 0){
                    Animation anim = AnimationUtils.loadAnimation(mCtx, R.anim.fadeout);

                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            holder.quantityLayout.setVisibility(View.GONE);
                            // Seteamos a 0 para que al terminar la animacion no se produscan bugs
                            // a los click listeners
                            mFoodQuantity.set( pos, 0);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    holder.quantityLayout.startAnimation(anim);

                }

                // notificamos al adapter que ha ocurrido una actualizacion
                notifyItemChanged(pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


    // ViewHolder Class
    class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewFoodImg, imageViewFoodAvailability;
        TextView textViewFoodName, textViewFoodDesc, textViewFoodPrice, textViewFoodQuantity;
        ImageButton imgButtonIncrease, imgButtonDecrease;
        LinearLayout quantityLayout;
        OnFoodClickListener onFoodClickListener;


        public FoodViewHolder(@NonNull View itemView, OnFoodClickListener onFoodClickListener) {
            super(itemView);

            // Image Views
            imageViewFoodImg = itemView.findViewById(R.id.imageViewFoodImg);
            imageViewFoodAvailability = itemView.findViewById(R.id.imageViewFoodAvailability);

            // TextViews
            textViewFoodName = itemView.findViewById(R.id.textViewFoodName);
            textViewFoodDesc = itemView.findViewById(R.id.textViewFoodDesc);
            textViewFoodPrice = itemView.findViewById(R.id.textViewFoodPrice);
            textViewFoodQuantity = itemView.findViewById(R.id.textViewFoodQuantity);

            // Botones
            imgButtonIncrease = itemView.findViewById(R.id.imageButtonIncrease);
            imgButtonDecrease = itemView.findViewById(R.id.imageButtonDecrease);

            // Layout
            quantityLayout = itemView.findViewById(R.id.quantityLayout);

            this.onFoodClickListener = onFoodClickListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            // retornamos la posicion del adapter cuando de clickea
            onFoodClickListener.OnFoodClick( getAdapterPosition() );

        }
    } // End of Class FoodViewHolder

    public interface OnFoodClickListener{
        void OnFoodClick(int pos);
    }





} // Class End Food Adapter

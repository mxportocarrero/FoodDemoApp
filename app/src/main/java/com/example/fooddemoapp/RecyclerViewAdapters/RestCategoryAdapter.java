package com.example.fooddemoapp.RecyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fooddemoapp.Models.RestCategory;
import com.example.fooddemoapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RestCategoryAdapter extends RecyclerView.Adapter<RestCategoryAdapter.RestCategoryViewHolder>{

    private Context mCtx;
    private List<RestCategory> mCategoryList;

    private OnRestCategoryAdapterClickListener mOnRestCategoryAdapterClickListener;

    public RestCategoryAdapter(Context mCtx, List<RestCategory> mCategoryList, OnRestCategoryAdapterClickListener onRestCategoryAdapterClickListener) {
        this.mCtx = mCtx;
        this.mCategoryList = mCategoryList;
        this.mOnRestCategoryAdapterClickListener = onRestCategoryAdapterClickListener;
    }

    @NonNull
    @Override
    public RestCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recycler_category_layout,null);

        RestCategoryViewHolder categoryViewHolder = new RestCategoryViewHolder(view,mOnRestCategoryAdapterClickListener);

        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestCategoryViewHolder holder, int position) {
        RestCategory category = mCategoryList.get(position);

        // Actualizando los ImageViews
        Glide.with(mCtx)
                .load(category.getmCategoryImgUrl())
                .into(holder.imageViewCategoryImg);

        //int color = Integer.decode(category.getmCategoryForegroundColor());
        //int color = 0xFFFF0000;0x4D3F330E
        Drawable drawable = new ColorDrawable(Color.parseColor(category.getmCategoryForegroundColor()));

        holder.imageViewCategoryImg.setForeground(drawable);

        // Seteando el titulo y la descripcion de categorias
        holder.textViewCategoryTitle.setText(category.getmCategoryName());
        holder.textViewCategoryDesc.setText(category.getmCategoryDesc());
    }

    @Override
    public int getItemCount() { return mCategoryList.size(); }

    // Clase View Holder
    // -----------------
    class RestCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewCategoryImg;
        TextView textViewCategoryTitle, textViewCategoryDesc;

        OnRestCategoryAdapterClickListener onRestCategoryAdapterClickListener;

        public RestCategoryViewHolder(@NonNull View itemView, OnRestCategoryAdapterClickListener onRestCategoryAdapterClickListener) {
            super(itemView);

            // Image Views
            imageViewCategoryImg = itemView.findViewById(R.id.imageViewCategoryImg);

            // TextViews
            textViewCategoryTitle = itemView.findViewById(R.id.textViewCategoryTitle);
            textViewCategoryDesc = itemView.findViewById(R.id.textViewCategoryDesc);

            //ClickListener
            this.onRestCategoryAdapterClickListener = onRestCategoryAdapterClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRestCategoryAdapterClickListener.onRecyclerViewClick(getAdapterPosition());
        }
    } // Fin de la clase RestCategoryViewHolder

    // Agregando un Listener para los clicks
    public interface OnRestCategoryAdapterClickListener{
        void onRecyclerViewClick(int pos);
    }

}

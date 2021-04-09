package com.example.bigmart.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bigmart.CategoryActivity;
import com.example.bigmart.R;
import com.example.bigmart.modal.CategoryModel;


import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {


    private List<CategoryModel> categoryModelList;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        String icon=categoryModelList.get(position).getCategoryIconLink();
        String name=categoryModelList.get(position).getCategoryName();
        holder.setCategory(name,position);
        holder.setCategoryicon(icon);

       /* if(lastPosition < position) {

            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.);
            holder.itemView.setAnimation(animation);
            lastPosition=position;
            }*/

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryicon;
        private TextView categoryname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryicon = itemView.findViewById(R.id.category_icon);
            categoryname = itemView.findViewById(R.id.category_name);
        }

        private void setCategoryicon(String iconURL) {
            if (!iconURL.equals("null")){
                Glide.with(itemView.getContext()).load(iconURL).apply(new RequestOptions().placeholder(R.drawable.ic_add_circle)).into(categoryicon);
            }
            //todo: set category icons
        }
        private void setCategory(final String name,final int position){
            categoryname.setText(name);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position !=0) {
                            Intent categoryIntent = new Intent(itemView.getContext(), CategoryActivity.class);
                            categoryIntent.putExtra("CategoryName", name);
                            itemView.getContext().startActivity(categoryIntent);

                        }
                }
            });


        }
    }
}


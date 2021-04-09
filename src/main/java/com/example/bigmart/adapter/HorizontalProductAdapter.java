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
import com.example.bigmart.ProductDetailsActivity;
import com.example.bigmart.R;
import com.example.bigmart.modal.HorizontalProductModel;


import java.util.List;

public class HorizontalProductAdapter extends RecyclerView.Adapter<HorizontalProductAdapter.ViewHolder> {

    private List<HorizontalProductModel> horizontalProductModels;

    public HorizontalProductAdapter(List<HorizontalProductModel> horizontalProductModels) {
        this.horizontalProductModels = horizontalProductModels;

    }

    @NonNull
    @Override
    public HorizontalProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductAdapter.ViewHolder holder, int position) {
        String resource = horizontalProductModels.get(position).getProductImage();
        String title = horizontalProductModels.get(position).getProductTitle();
        String dece = horizontalProductModels.get(position).getProductDescription();
        String price = horizontalProductModels.get(position).getProductPrice();
        String product_id=horizontalProductModels.get(position).getProductId();

        holder.setData(product_id,resource,title,dece,price);
    }

    @Override
    public int getItemCount() {
        if(horizontalProductModels.size()> 8){
            return 8;
        }else{
            return horizontalProductModels.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productimage;
        private TextView ptitle,pdescription,pprice;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productimage=itemView.findViewById(R.id.h_s_image);
            ptitle=itemView.findViewById(R.id.h_s_title);
            pdescription=itemView.findViewById(R.id.h_h_desc);
            pprice=itemView.findViewById(R.id.h_s_price);


        }

        private void setData(final String product_ids,String resource,String title,String desc,String price) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_delete_black)).into(productimage);
            ptitle.setText(title);
            pdescription.setText(desc);
            pprice.setText("Rs." + price + "/-");


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productdetails = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productdetails.putExtra("PRODUCT_ID",product_ids);
                    itemView.getContext().startActivity(productdetails);
                }
            });

        }

    }
}


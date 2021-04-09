package com.example.bigmart.adapter;



import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bigmart.ProductDetailsActivity;
import com.example.bigmart.R;
import com.example.bigmart.modal.HorizontalProductModel;


import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<HorizontalProductModel> horizontalProductModelList;

    public GridProductLayoutAdapter(List<HorizontalProductModel> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if(convertView==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_view,null);
            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#ffffff"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productdetailsintent=new Intent(parent.getContext(), ProductDetailsActivity.class);
                    productdetailsintent.putExtra("Product_ID",horizontalProductModelList.get(position).getProductId());
                    parent.getContext().startActivity(productdetailsintent);
                }
            });


            ImageView productImage=view.findViewById(R.id.h_s_image);
            TextView producttitle= view.findViewById(R.id.h_s_title);
            TextView description=view.findViewById(R.id.h_h_desc);
            TextView price=view.findViewById(R.id.h_s_price);

            Glide.with(parent.getContext()).load(horizontalProductModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.ic_delete_black)).into(productImage);
            producttitle.setText(horizontalProductModelList.get(position).getProductTitle());
            description.setText(horizontalProductModelList.get(position).getProductDescription());
            price.setText("Rs."+horizontalProductModelList.get(position).getProductPrice());

        }else{
            view=convertView;
        }
        return view;
    }
}


package com.example.bigmart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigmart.R;
import com.example.bigmart.modal.ProductSpecificationModal;

import java.util.List;

public class ProductSpecificationAdpter extends RecyclerView.Adapter<ProductSpecificationAdpter.ViewHolder> {

    private List<ProductSpecificationModal> productSpecificationModals;

    public ProductSpecificationAdpter(List<ProductSpecificationModal> productSpecificationModals) {
        this.productSpecificationModals = productSpecificationModals;
    }

    @Override
    public int getItemViewType(int position){
        switch (productSpecificationModals.get(position).getType()){
            case 0:
                return ProductSpecificationModal.SPEFICATION_TITLE;
            case 1:
                return ProductSpecificationModal.SPEFICATION_BODY;
                default:
                    return -1;
        }
    }

    @NonNull
    @Override
    public ProductSpecificationAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case ProductSpecificationModal.SPEFICATION_TITLE:
                TextView title = new TextView(parent.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16,parent.getContext()),
                        setDp(16,parent.getContext()),setDp(16,parent.getContext()),setDp(8,parent.getContext()));
                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);


            case ProductSpecificationModal.SPEFICATION_BODY:


        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdpter.ViewHolder holder, int position) {
        switch (productSpecificationModals.get(position).getType()){
            case ProductSpecificationModal.SPEFICATION_TITLE:
                holder.setTitle(productSpecificationModals.get(position).getTitle());
                break;
                case ProductSpecificationModal.SPEFICATION_BODY:
                    break;
                    default:
                        return;

        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView featureName,featureValue,title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        private void setTitle(String titleText){
            title = (TextView) itemView;
            title.setText(titleText);
        }
        private void setFeatures(String featureTitle,String featuredetail){
            featureName = itemView.findViewById(R.id.featureName);
            featureValue = itemView.findViewById(R.id.featureValue);
            featureName.setText(featureTitle);
            featureValue.setText(featuredetail);
        }
    }

    private int setDp(int dp, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
}

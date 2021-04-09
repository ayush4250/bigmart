package com.example.bigmart.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bigmart.DbQueries;
import com.example.bigmart.R;
import com.example.bigmart.modal.RewardModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardAdapter extends RecyclerView.Adapter<MyRewardAdapter.Viewholder> {

    private List<RewardModel> rewardModelList;
    private Boolean useminiLayout =false;
    private RecyclerView coupenrecycleview;
    private LinearLayout selectedcoupen;
    private String productOriginalPrice;
    private TextView selectedcoupenTitle;
    private TextView selectedcoupenExpiryDate;
    private TextView selectedcoupenBody;
    private TextView discountedprice;
    private int cartItemPosition;


    public MyRewardAdapter(List<RewardModel> rewardModelList,Boolean useminiLayout) {
        this.rewardModelList = rewardModelList;
        this.useminiLayout=useminiLayout;
    }

    public MyRewardAdapter(List<RewardModel> rewardModelList, Boolean useminiLayout, RecyclerView coupenrecycleview, LinearLayout selectedcoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody,TextView discountedprice) {
        this.rewardModelList = rewardModelList;
        this.useminiLayout=useminiLayout;
        this.coupenrecycleview=coupenrecycleview;
        this.selectedcoupen=selectedcoupen;
        this.productOriginalPrice=productOriginalPrice;
        this.selectedcoupenTitle=coupenTitle;
        this.selectedcoupenExpiryDate=coupenExpiryDate;
        this.selectedcoupenBody=coupenBody;
        this.discountedprice=discountedprice;
    }

    public MyRewardAdapter(int cartItemPosition,List<RewardModel> rewardModelList, Boolean useminiLayout, RecyclerView coupenrecycleview, LinearLayout selectedcoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody,TextView discountedprice) {
        this.rewardModelList = rewardModelList;
        this.useminiLayout=useminiLayout;
        this.coupenrecycleview=coupenrecycleview;
        this.selectedcoupen=selectedcoupen;
        this.productOriginalPrice=productOriginalPrice;
        this.selectedcoupenTitle=coupenTitle;
        this.selectedcoupenExpiryDate=coupenExpiryDate;
        this.selectedcoupenBody=coupenBody;
        this.discountedprice=discountedprice;
        this.cartItemPosition = cartItemPosition;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(useminiLayout){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_reward_layout,parent,false);

        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout,parent,false);

        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        String coupenId = rewardModelList.get(position).getCoupenId();
        String type = rewardModelList.get(position).getType();
        Date validity=rewardModelList.get(position).getTimestamp();
        String body=rewardModelList.get(position).getCoupenbody();
        String lowerLimt=rewardModelList.get(position).getLowerlimit();
        String upperLimit=rewardModelList.get(position).getUpperlimit();
        String disoramt=rewardModelList.get(position).getDisOramt();
        Boolean alreadyUsed=rewardModelList.get(position).getAlreadyUsed();


        holder.setData(coupenId,type,validity,body,lowerLimt,upperLimit,disoramt,alreadyUsed);

    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView coupentitle;
        private TextView coupenvalidity;
        private TextView coupenbody;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            coupentitle=itemView.findViewById(R.id.coupentitle);
            coupenvalidity=itemView.findViewById(R.id.coupenvalidity);
            coupenbody=itemView.findViewById(R.id.coupenbody);
        }

        private void setData(final String coupenId,final String type, final Date validity, final String body, final String upperlimit, final String lowerlimit, final String disOramt, final boolean alreadyUsed){

           if(type.equals("Discount")){
               coupentitle.setText(type);
           }else{
               coupentitle.setText("Flat Rs."+disOramt+" OFF");
           }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
           if (alreadyUsed){
               coupenvalidity.setText("Already Used");
               coupenvalidity.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
           }else{
               coupenvalidity.setText(simpleDateFormat.format(validity));
               coupenbody.setText(body);
           }



            if(useminiLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!alreadyUsed) {
                            selectedcoupenTitle.setText(type);
                            selectedcoupenExpiryDate.setText(simpleDateFormat.format(validity));
                            coupenbody.setText(body);

                            if (Long.valueOf(productOriginalPrice) > Long.valueOf(lowerlimit) && (Long.valueOf(productOriginalPrice) < Long.valueOf(upperlimit))) {
                                if (type.equals("Discount")) {
                                    Long discountAmount = Long.valueOf(productOriginalPrice) * Long.valueOf(disOramt) / 100;
                                    discountedprice.setText(String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount));
                                } else {
                                    discountedprice.setText(String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(disOramt)));
                                }

                                DbQueries.cartItemModelList.get(cartItemPosition).setSelectedCoupenId(coupenId);
                            } else {
                                DbQueries.cartItemModelList.get(cartItemPosition).setSelectedCoupenId(null);
                                discountedprice.setText("Invalid");
                                discountedprice.setText("Product does not matches the coupen terms.");
                            }
                            if (coupenrecycleview.getVisibility() == View.GONE) {
                                coupenrecycleview.setVisibility(View.VISIBLE);
                                selectedcoupen.setVisibility(View.GONE);
                            } else {
                                coupenrecycleview.setVisibility(View.GONE);
                                selectedcoupen.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });
            }
        }

    }
}



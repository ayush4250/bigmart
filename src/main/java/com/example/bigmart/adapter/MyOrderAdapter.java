package com.example.bigmart.adapter;


import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bigmart.DbQueries;
import com.example.bigmart.OrderDetailsActivity;
import com.example.bigmart.ProductDetailsActivity;
import com.example.bigmart.R;
import com.example.bigmart.modal.MyOrderItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder> {
    private List<MyOrderItemModel> myOrderItemModels;
    private Dialog loadingdialog;


    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModels,Dialog loadingdialog) {
        this.myOrderItemModels = myOrderItemModels;
        this.loadingdialog = loadingdialog;
    }

    @NonNull
    @Override
    public MyOrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_itemlayut,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.Viewholder holder, int position) {
        String resource=myOrderItemModels.get(position).getProductImage();
        String productId=myOrderItemModels.get(position).getProductId();
        int rating=myOrderItemModels.get(position).getRating();
        String title=myOrderItemModels.get(position).getProductTitle();
        String orderStatus=myOrderItemModels.get(position).getOrderStatus();
        Date date;
        switch (orderStatus){
            case "Ordered":
                date = myOrderItemModels.get(position).getOrderedDate();
                break;
            case "Packed":
                date = myOrderItemModels.get(position).getPackedDate();
                break;
            case "Shipped":
                date = myOrderItemModels.get(position).getShippedDate();
                break;
            case "Delivered":
                date = myOrderItemModels.get(position).getDeliveredDate();
                break;
            case "Cancelled":
                date = myOrderItemModels.get(position).getCancelDate();
                break;
                default:
                    date = myOrderItemModels.get(position).getCancelDate();

        }

        holder.setData(resource,title,orderStatus,date,rating,productId,position);

    }

    @Override
    public int getItemCount() {
        return myOrderItemModels.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        private ImageView productimage,deliveryIndicator;
        private TextView producttitle,deliverystatus;
      //  private LinearLayout rateNowContainer;


        public Viewholder(@NonNull final View itemView) {
            super(itemView);

            productimage=itemView.findViewById(R.id.productimagemyorder);
            producttitle=itemView.findViewById(R.id.textmyorder);
            deliverystatus=itemView.findViewById(R.id.order_deliver_date);
            deliveryIndicator=itemView.findViewById(R.id.orderindicator);
           // rateNowContainer=itemView.findViewById(R.id.rate_now_container);


        }

        private void setData(String resource, String title, String orderStatus, Date date, final int rating, final String productId, final int position){
            Glide.with(itemView.getContext()).load(resource).into(productimage);

            producttitle.setText(title);
            if(orderStatus.equals("Cancelled")) {
                deliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.red)));
            }else{
                deliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.Green)));
            }
            deliverystatus.setText(orderStatus+String.valueOf(date));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderdetailsIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    orderdetailsIntent.putExtra("Position",position);
                    itemView.getContext().startActivity(orderdetailsIntent);

                }
            });


            setRating(rating);
            for(int x = 0; x< ProductDetailsActivity.ratenowcontainer.getChildCount(); x++){
                final int starPosition =x;
                ProductDetailsActivity.ratenowcontainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingdialog.show();
                        setRating(starPosition);
                        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Products").document(productId);

                        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                            @Nullable
                            @Override
                            public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                                DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                                if (rating!=0){
                                    Long increase = documentSnapshot.getLong(starPosition+1+"_star")+1;
                                    Long decrease = documentSnapshot.getLong(rating+1+"_star")-1;
                                    transaction.update(documentReference,starPosition+1+"_star",increase);
                                    transaction.update(documentReference,rating+1+"_star",decrease);
                                }else{
                                    Long increase = documentSnapshot.getLong(starPosition+1+"_star") +1;
                                    transaction.update(documentReference,starPosition+1+"_star",increase);
                                }
                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Object>() {

                            @Override
                            public void onSuccess(Object o) {

                                Map<String, Object> myrating = new HashMap<>();
                                if (DbQueries.myratedIds.contains(productId)) {

                                    myrating.put("rating_" + DbQueries.myratedIds.indexOf(productId), (long) starPosition + 1);

                                } else {

                                    myrating.put("list_size", (long) DbQueries.myratedIds.size() + 1);
                                    myrating.put("productid_" + DbQueries.myratedIds.size(), productId);
                                    myrating.put("rating_" + DbQueries.myratedIds.size(), (long) starPosition + 1);
                                }

                                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_data")
                                        .document("My_rating").update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                          @Override
                                                                                                          public void onComplete(@NonNull Task<Void> task) {
                                                                                                              if (task.isSuccessful()) {
                                                                                                                  DbQueries.myOrderItemModelList.get(position).setRating(starPosition);
                                                                                                                  if (DbQueries.myratedIds.contains(productId)){
                                                                                                                      DbQueries.myRating.set(DbQueries.myratedIds.indexOf(productId),Long.parseLong(String.valueOf(starPosition+1)));
                                                                                                                  }else{
                                                                                                                      DbQueries.myratedIds.add(productId);
                                                                                                                      DbQueries.myRating.add(Long.parseLong(String.valueOf(starPosition+1)));
                                                                                                                  }
                                                                                                              }else{
                                                                                                                  String error = task.getException().getMessage();
                                                                                                                  Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                                              }
                                                                                                              loadingdialog.dismiss();
                                                                                                          }
                                                                                                      });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingdialog.dismiss();
                            }
                        });
                    }
                });
            }


        }
        private void setRating(int starPosition){
            for(int x=0;x<ProductDetailsActivity.ratenowcontainer.getChildCount();x++){
                ImageView starBtn=(ImageView)ProductDetailsActivity.ratenowcontainer.getChildAt(x);
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if(x<=starPosition){
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }
            }
        }
    }
}






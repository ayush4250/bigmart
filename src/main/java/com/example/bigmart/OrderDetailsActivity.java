package com.example.bigmart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {


    private int position,rating;
    private TextView title,price,quantity;
    private ImageView productImage,orderedIndicator,packedIndicator,shippedIndicator,DeleveredIndicator;
    private ProgressBar o_p_progress, p_s_progress, s_d_progress;
    private TextView ordereddate,packeddate,shipeddate,deliverdate;
    private TextView orderedtitle,packedtitle,shippedtitle,deliveredtitle;
    private TextView bodyorder,bodypacked,bodyshiped,bodydelivered;
    private LinearLayout rateNowContainer;
   // private TextView fullName,address,pincode;
    private Dialog loadingDialog;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.producttitle);
        price = findViewById(R.id.product_price);
        productImage = findViewById(R.id.productimage5);
        quantity = findViewById(R.id.product_qty1);
        orderedIndicator = findViewById(R.id.orderindicator);
        packedIndicator = findViewById(R.id.packedindicator);
        shippedIndicator = findViewById(R.id.shipping_indicator);
        DeleveredIndicator = findViewById(R.id.delivered_indicator);
        ordereddate = findViewById(R.id.order_deliver_date);
        packeddate = findViewById(R.id.packeddate);
        shipeddate = findViewById(R.id.shipdate);
        deliverdate = findViewById(R.id.deliverdate);
        orderedtitle = findViewById(R.id.ordered_title);
        packedtitle = findViewById(R.id.packedtitle);
        shippedtitle = findViewById(R.id.shiptitle);
        deliveredtitle = findViewById(R.id.deliveredtitle);
        bodyorder = findViewById(R.id.ordered_body);
        bodypacked = findViewById(R.id.packedbody);
        bodyshiped = findViewById(R.id.shipbody);
        bodydelivered = findViewById(R.id.deliverbody);
        o_p_progress = findViewById(R.id.ordered_packed);
        p_s_progress = findViewById(R.id.packed_shipped);
        s_d_progress = findViewById(R.id.shipping_delivered);

        loadingDialog=new Dialog(OrderDetailsActivity.this);
        loadingDialog.setContentView(R.layout.payment_method);
        loadingDialog.setCancelable(false);
        // loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape2));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();



        rateNowContainer = findViewById(R.id.rate_now_container);

         position = getIntent().getIntExtra("Position",-1);

        final MyOrderItemModel model = DbQueries.myOrderItemModelList.get(position);

        title.setText(model.getProductTitle());
        if (model.getDiscountedPrice().equals("")) {
            price.setText("Rs."+model.getDiscountedPrice());
        }else{
            price.setText("Rs."+model.getProductPrice());
        }
        quantity.setText("Qty :"+String.valueOf(model.getProductQuantity()));
        Glide.with(this).load(model.getProductImage()).into(productImage);

        simpleDateFormat = new SimpleDateFormat("EEE-dd-MMM-YYYY-hh-mm-aa");

        switch (model.getOrderStatus()){
            case "Ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                ordereddate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                p_s_progress.setVisibility(View.GONE);
                s_d_progress.setVisibility(View.GONE);
                o_p_progress.setVisibility(View.GONE);

                packedIndicator.setVisibility(View.GONE);
                bodypacked.setVisibility(View.GONE);
                packeddate.setVisibility(View.GONE);
                packedtitle.setVisibility(View.GONE);

                shippedIndicator.setVisibility(View.GONE);
                bodyshiped.setVisibility(View.GONE);
                shipeddate.setVisibility(View.GONE);
                shippedtitle.setVisibility(View.GONE);

                DeleveredIndicator.setVisibility(View.GONE);
                bodydelivered.setVisibility(View.GONE);
                deliverdate.setVisibility(View.GONE);
                deliveredtitle.setVisibility(View.GONE);

                break;
            case "Packed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                ordereddate.setText(String.valueOf(model.getOrderedDate()));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                packeddate.setText(String.valueOf(model.getPackedDate()));

                o_p_progress.setProgress(100);

                p_s_progress.setVisibility(View.GONE);
                s_d_progress.setVisibility(View.GONE);

                shippedIndicator.setVisibility(View.GONE);
                bodyshiped.setVisibility(View.GONE);
                shipeddate.setVisibility(View.GONE);
                shippedtitle.setVisibility(View.GONE);

                DeleveredIndicator.setVisibility(View.GONE);
                bodydelivered.setVisibility(View.GONE);
                deliverdate.setVisibility(View.GONE);
                deliveredtitle.setVisibility(View.GONE);


                break;
            case "Shipped":

                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                ordereddate.setText(String.valueOf(model.getOrderedDate()));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                packeddate.setText(String.valueOf(model.getPackedDate()));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                shipeddate.setText(String.valueOf(model.getShippedDate()));

                o_p_progress.setProgress(100);
                p_s_progress.setProgress(100);


                s_d_progress.setVisibility(View.GONE);


                DeleveredIndicator.setVisibility(View.GONE);
                bodydelivered.setVisibility(View.GONE);
                deliverdate.setVisibility(View.GONE);
                deliveredtitle.setVisibility(View.GONE);


                break;
            case "Delivered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                ordereddate.setText(String.valueOf(model.getOrderedDate()));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                packeddate.setText(String.valueOf(model.getPackedDate()));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                shipeddate.setText(String.valueOf(model.getShippedDate()));

                DeleveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                deliverdate.setText(String.valueOf(model.getDeliveredDate()));

                o_p_progress.setProgress(100);
                p_s_progress.setProgress(100);
                s_d_progress.setProgress(100);


                break;
            case "Cancelled":
                if (model.getPackedDate().after(model.getOrderedDate())){
                    if (model.getShippedDate().after(model.getPackedDate())){
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                        ordereddate.setText(String.valueOf(model.getOrderedDate()));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                        packeddate.setText(String.valueOf(model.getPackedDate()));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                        shipeddate.setText(String.valueOf(model.getShippedDate()));

                        DeleveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        deliverdate.setText(String.valueOf(model.getCancelDate()));

                        deliveredtitle.setText("Cancelled");
                        bodydelivered.setText("Your Order Has Been Cancelled");

                        o_p_progress.setProgress(100);
                        p_s_progress.setProgress(100);
                        s_d_progress.setProgress(100);
                    }else{
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                        ordereddate.setText(String.valueOf(model.getOrderedDate()));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                        packeddate.setText(String.valueOf(model.getPackedDate()));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        shipeddate.setText(String.valueOf(model.getCancelDate()));

                        shippedtitle.setText("Cancelled");
                        bodyshiped.setText("Your Order Has Been Cancelled");

                        o_p_progress.setProgress(100);
                        p_s_progress.setProgress(100);


                        s_d_progress.setVisibility(View.GONE);


                        DeleveredIndicator.setVisibility(View.GONE);
                        bodydelivered.setVisibility(View.GONE);
                        deliverdate.setVisibility(View.GONE);
                        deliveredtitle.setVisibility(View.GONE);
                    }
                }else{
                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.Green)));
                    ordereddate.setText(String.valueOf(model.getOrderedDate()));

                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    packeddate.setText(String.valueOf(model.getCancelDate()));

                    packedtitle.setText("Cancelled");
                    bodypacked.setText("Your Order Has Been Cancelled");
                    o_p_progress.setProgress(100);

                    p_s_progress.setVisibility(View.GONE);
                    s_d_progress.setVisibility(View.GONE);

                    shippedIndicator.setVisibility(View.GONE);
                    bodyshiped.setVisibility(View.GONE);
                    shipeddate.setVisibility(View.GONE);
                    shippedtitle.setVisibility(View.GONE);

                    DeleveredIndicator.setVisibility(View.GONE);
                    bodydelivered.setVisibility(View.GONE);
                    deliverdate.setVisibility(View.GONE);
                    deliveredtitle.setVisibility(View.GONE);
                }
                break;

        }

        rating = model.getRating();
        loadingDialog.show();
        setRating(rating);
        for(int x = 0; x< ProductDetailsActivity.ratenowcontainer.getChildCount(); x++) {
            final int starPosition = x;
            ProductDetailsActivity.ratenowcontainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(starPosition);
                    final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Products").document(model.getProductId());

                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                        @Nullable
                        @Override
                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                            if (rating != 0) {
                                Long increase = documentSnapshot.getLong(starPosition + 1 + "_star") + 1;
                                Long decrease = documentSnapshot.getLong(rating + 1 + "_star") - 1;
                                transaction.update(documentReference, starPosition + 1 + "_star", increase);
                                transaction.update(documentReference, rating + 1 + "_star", decrease);
                            } else {
                                Long increase = documentSnapshot.getLong(starPosition + 1 + "_star") + 1;
                                transaction.update(documentReference, starPosition + 1 + "_star", increase);
                            }
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Object>() {

                        @Override
                        public void onSuccess(Object o) {

                            Map<String, Object> myrating = new HashMap<>();
                            if (DbQueries.myratedIds.contains(model.getProductId())) {

                                myrating.put("rating_" + DbQueries.myratedIds.indexOf(model.getProductId()), (long) starPosition + 1);

                            } else {

                                myrating.put("list_size", (long) DbQueries.myratedIds.size() + 1);
                                myrating.put("productid_" + DbQueries.myratedIds.size(), model.getProductId());
                                myrating.put("rating_" + DbQueries.myratedIds.size(), (long) starPosition + 1);
                            }

                            FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_data")
                                    .document("My_rating").update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DbQueries.myOrderItemModelList.get(position).setRating(starPosition);
                                        if (DbQueries.myratedIds.contains(model.getProductId())){
                                            DbQueries.myRating.set(DbQueries.myratedIds.indexOf(model.getProductId()),Long.parseLong(String.valueOf(starPosition+1)));
                                        }else{
                                            DbQueries.myratedIds.add(model.getProductId());
                                            DbQueries.myRating.add(Long.parseLong(String.valueOf(starPosition+1)));
                                        }
                                    }else{
                                        String error = task.getException().getMessage();
                                        Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    loadingDialog.dismiss();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismiss();
                        }
                    });
                }
            });
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

package com.example.bigmart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bigmart.adapter.CategoryAdapter;
import com.example.bigmart.adapter.HomePageAdapter;
import com.example.bigmart.adapter.MyOrderAdapter;
import com.example.bigmart.modal.AddressesModel;
import com.example.bigmart.modal.CartItemModel;
import com.example.bigmart.modal.CategoryModel;
import com.example.bigmart.modal.FavouriteModel;
import com.example.bigmart.modal.HomePageModel;
import com.example.bigmart.modal.HorizontalProductModel;
import com.example.bigmart.modal.MyOrderItemModel;
import com.example.bigmart.modal.RewardModel;
import com.example.bigmart.modal.SliderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbQueries {


    public static FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList=new ArrayList<>();
    public static List<List<HomePageModel>> lists= new ArrayList<>();
    public static List<String> wishlist=new ArrayList<>();
    public static List<FavouriteModel> favouriteModelList=new ArrayList<>();

    public static List<String> myratedIds=new ArrayList<>();
    public static List<Long> myRating=new ArrayList<>();
    public static List<FavouriteModel> viewallproductlist= new ArrayList<>();

    public static List<String> cartList=new ArrayList<>();
    public static List<CartItemModel> cartItemModelList=new ArrayList<>();

    public static List<AddressesModel> addressesModelList=new ArrayList<>();

    public static List<RewardModel> rewardModelList=new ArrayList<>();

    public static List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();

    public static int  selectedAddress =-1;



    public static void loadCategories(final RecyclerView categoruyRecyclerView, final Context context){

        categoryModelList.clear();
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Categories").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter=new CategoryAdapter(categoryModelList);
                            categoruyRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        }else{
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadFragmentData(final RecyclerView homepageRecyclerView, final Context context,final int index){
        firebaseFirestore.collection("Categories").document("Home").collection("TOP_DEALS")
                .orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){

                        if((long)documentSnapshot.get("view_type")==0){
                            List<SliderModel> sliderModelList=new ArrayList<>();

                            long no_of_banners=(long)documentSnapshot.get("no_of_banners");
                            for(long x=1;x<no_of_banners+1;x++){
                                sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+x).toString(),documentSnapshot.get("banner"+x+"_background").toString()));
                            }
                            lists.get(index).add(new HomePageModel(0,sliderModelList));
                        }else if((long)documentSnapshot.get("view_type")==2){

                             viewallproductlist=new ArrayList<>();
                            List<HorizontalProductModel> horizontalProductModels=new ArrayList<>();
                            long no_of_products=(long)documentSnapshot.get("no_of_products");
                            for(long x=1;x<no_of_products+1;x++){
                                horizontalProductModels.add(new HorizontalProductModel(documentSnapshot.get("product_id_"+x).toString(),
                                        documentSnapshot.get("product_image_" + x).toString(),
                                        documentSnapshot.get("product_title_" + x).toString(),
                                        documentSnapshot.get("product_subtitle_" + x).toString(),
                                        documentSnapshot.get("product_price_" + x).toString()));

                                viewallproductlist.add(new FavouriteModel(documentSnapshot.get("product_id_"+x).toString(),
                                        documentSnapshot.get("product_image_"+x).toString(),
                                        documentSnapshot.get("product_title_" + x).toString(),
                                        documentSnapshot.get("product_subtitle_" + x).toString(),
                                        (long)documentSnapshot.get("product_price_" + x),
                                        (boolean) documentSnapshot.get("in_stock_"+x)));
                            }
                            lists.get(index).add(new HomePageModel(1,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),horizontalProductModels,viewallproductlist));
                        }else if((long)documentSnapshot.get("view_type")==3) {
                            List<HorizontalProductModel> gridlayout = new ArrayList<>();
                            long no_of_products = (long) documentSnapshot.get("no_of_products");
                            for (long x = 1; x < no_of_products + 1; x++) {
                                gridlayout.add(new HorizontalProductModel(documentSnapshot.get("productid_" + x).toString(),
                                        documentSnapshot.get("productimage_" + x).toString(), documentSnapshot.get("producttitle_" + x).toString()
                                        , documentSnapshot.get("productsubtitle_" + x).toString(), documentSnapshot.get("productprice_" + x).toString()));
                            }
                            lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), gridlayout,viewallproductlist));
                        }
                    }
                    HomePageAdapter homePageAdapter=new HomePageAdapter(lists.get(index));
                    homepageRecyclerView.setAdapter(homePageAdapter);
                    homePageAdapter.notifyDataSetChanged();
                    HomeFragment.swipeRefreshLayout.setRefreshing(false);
                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public static void loadWishlist(final Context context, final Dialog dialog,final boolean loadProductData){
        wishlist.clear();
        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_data").document("My_wishlist")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    for(long x = 0; x< (long)task.getResult().get("list_size");x++) {
                        wishlist.add(task.getResult().get("productid_" + x).toString());
                        if(DbQueries.wishlist.contains(ProductDetailsActivity.productId)){
                            ProductDetailsActivity.addedtowishlist=true;
                            if(ProductDetailsActivity.addwishlist!=null) {
                                ProductDetailsActivity.addwishlist.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                            }
                        }else{
                            if(ProductDetailsActivity.addwishlist!=null) {
                                ProductDetailsActivity.addwishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                            }
                            ProductDetailsActivity.addedtowishlist=false;
                        }

                        if (loadProductData) {
                            favouriteModelList.clear();
                            final String productId=task.getResult().get("productid_" + x).toString();
                            firebaseFirestore.collection("Products").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        favouriteModelList.add(new FavouriteModel(productId,
                                                task.getResult().get("product_image_1").toString(),
                                                task.getResult().get("product_title").toString(),
                                                task.getResult().get("average_rating").toString(),
                                                (long) task.getResult().get("total_rating"),
                                                (boolean) task.getResult().get("in_stock")));

                                        WishlistFragment.favouriteAdapter.notifyDataSetChanged();

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void removeFromWishlist(final int index, final Context context){
        final String removeProductId=wishlist.get(index);
        wishlist.remove(index);
        Map<String,Object> updatewishlist = new HashMap<>();

        for(int x=0;x<wishlist.size();x++){
            updatewishlist.put("PRODUCT_ID_"+x,wishlist.get(x));
        }
        updatewishlist.put("list_size",(long)wishlist.size());

        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_data").document("My_wishlist")
                .set(updatewishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    if(favouriteModelList.size()!=0){
                        favouriteModelList.remove(index);
                        WishlistFragment.favouriteAdapter.notifyDataSetChanged();
                    }
                    ProductDetailsActivity.addedtowishlist=false;
                    Toast.makeText(context,"Remove Successfully",Toast.LENGTH_SHORT).show();

                }else{
                    if(ProductDetailsActivity.addwishlist!=null){
                        ProductDetailsActivity.addwishlist.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                    }
                    wishlist.add(index,removeProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                ProductDetailsActivity.runningwishlist_query=false;
            }
        });
    }

    public static void loadRatingList(final Context context){

        if(!ProductDetailsActivity.runningrating_query) {
            ProductDetailsActivity.runningrating_query=true;
            myratedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_data").document("My_rating")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> orderProductIds = new ArrayList<>();
                        for (int x=0;x<myOrderItemModelList.size();x++){
                            orderProductIds.add(myOrderItemModelList.get(x).getProductId());
                        }

                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myratedIds.add(task.getResult().get("productid_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("productid_" + x).toString().equals(ProductDetailsActivity.productId)) {
                                ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if(ProductDetailsActivity.ratenowcontainer!=null) {
                                    ProductDetailsActivity.setRating(ProductDetailsActivity.initialRating);
                                }
                            }

                            if (orderProductIds.contains(task.getResult().get("productid_"+x).toString())){
                                myOrderItemModelList.get(orderProductIds.indexOf(task.getResult().get("productid_"+x).toString())).setRating(Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1);
                            }

                        }
                        if (OrderFragment.myOrderAdapter != null){
                            OrderFragment.myOrderAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailsActivity.runningrating_query=false;

                }
            });
        }

    }

    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView badgecount,final TextView cartTotalamount){
        cartList.clear();
        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_data").document("My_Cart")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    for(long x = 0; x< (long)task.getResult().get("list_size");x++) {
                        cartList.add(task.getResult().get("productid_" + x).toString());

                        if(DbQueries.cartList.contains(ProductDetailsActivity.productId)){
                            ProductDetailsActivity.alreadyaddedtocart=true;
                        }else{
                            ProductDetailsActivity.alreadyaddedtocart=false;
                        }

                        if (loadProductData) {
                            cartItemModelList.clear();
                            final String productId=task.getResult().get("productid_" + x).toString();
                            firebaseFirestore.collection("Products").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int index=0;
                                        if(cartList.size()>=2){
                                            index=cartList.size()-2;
                                        }
                                        cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM,productId,
                                                task.getResult().get("product_image_1").toString(),
                                                task.getResult().get("product_title").toString(),
                                                (long) task.getResult().get("free_coupens"),
                                                task.getResult().get("product_price").toString(),
                                                task.getResult().get("cuted_price").toString(),
                                                (long)2,
                                                (long)1,
                                                (boolean)task.getResult().get("in_stock"),
                                                (long)task.getResult().get("max-quantity"),
                                                (long)1));


                                        if(cartList.size()==1){
                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                            LinearLayout parent = (LinearLayout) cartTotalamount.getParent().getParent();
                                            parent.setVisibility(View.VISIBLE);
                                        }

                                        if(cartList.size()==0){
                                            cartItemModelList.clear();
                                        }
                                        CartFragment.cartAdapter.notifyDataSetChanged();
                                        dialog.dismiss();



                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                        }


                    }
//                    if(cartList.size()!=0){
//                        badgecount.setVisibility(View.VISIBLE);
//                    }else{
//                        badgecount.setVisibility(View.GONE);
//                    }
//                    if(DbQueries.cartList.size()<50) {
//                        badgecount.setText(String.valueOf(DbQueries.cartList.size()));
//                    }else{
//                        badgecount.setText("50+");
//                    }
                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static void removeFromCart(final int index, final Context context, final TextView cartTotalAmount){
        final String removeProductId=cartList.get(index);
        cartList.remove(index);
        Map<String,Object> updatecartlist = new HashMap<>();

        for(int x=0;x<cartList.size();x++){
            updatecartlist.put("productid_"+x,cartList.get(x));
        }
        updatecartlist.put("list_size",(long)cartList.size());

        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_data").document("My_Cart")
                .set(updatecartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    if(cartItemModelList.size()!=0){
                        cartItemModelList.remove(index);
                        CartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if(cartList.size()==0){
                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);
                        cartItemModelList.clear();
                    }
                    Toast.makeText(context, "Remove Successfully", Toast.LENGTH_SHORT).show();


                }else{
                    cartList.add(index,removeProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                ProductDetailsActivity.runningcart_query=false;
            }
        });
    }

    public static void loadAddresses(final Context context,final Dialog loadingDialog){
        addressesModelList.clear();
        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_data").document("My_Addresses")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    Intent deliveryintent;

                    if((long)task.getResult().get("list_size")==0){
                        deliveryintent=new Intent(context,AddAddressActivity.class);
                        deliveryintent.putExtra("INTENT","deliveryintent");
                    }else{
                        for(long x=1;x<(long)task.getResult().get("list_size")+1;x++){
                            addressesModelList.add(new AddressesModel(task.getResult().get("fullname_"+x).toString(),
                                    task.getResult().get("pincode_"+x).toString(),
                                    task.getResult().get("address_"+x).toString(),
                                    (boolean) task.getResult().get("selected_"+x),
                                    task.getResult().getString("mobile_no_"+x)));

                            if((boolean) task.getResult().get("selected_"+x)){
                                selectedAddress =Integer.parseInt(String.valueOf(x-1));
                            }
                        }

                        deliveryintent=new Intent(context,DeliveryActivity.class);

                    }
                    context.startActivity(deliveryintent);
                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();

            }
        });
    }

    public static void loadRewards(final Context context,final Dialog loadingdialog,final Boolean OnRewardFragment){
        rewardModelList.clear();
        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            final Date lastseenDate = task.getResult().getDate("Last Seen");
                            firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user_rewards").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                    if(documentSnapshot.get("type").toString().equals("Discount") && lastseenDate.before(documentSnapshot.getDate("validity"))) {
                                                        rewardModelList.add(new RewardModel(documentSnapshot.getId(),documentSnapshot.get("type").toString(),
                                                                documentSnapshot.get("lower_limit").toString(),
                                                                documentSnapshot.get("upper_limit").toString(),
                                                                documentSnapshot.get("percentage").toString(),
                                                                documentSnapshot.get("body").toString(),
                                                                (Date)documentSnapshot.get("validity"),
                                                                (boolean)documentSnapshot.get("already_used")
                                                        ));
                                                    }else if(documentSnapshot.get("type").toString().equals("Flat Rs. OFF") && lastseenDate.before(documentSnapshot.getDate("validity"))){
                                                        rewardModelList.add(new RewardModel(documentSnapshot.getId(),documentSnapshot.get("type").toString(),
                                                                documentSnapshot.get("lower_limit").toString(),
                                                                documentSnapshot.get("upper_limit").toString(),
                                                                documentSnapshot.get("amount").toString(),
                                                                documentSnapshot.get("body").toString(),
                                                                (Date)documentSnapshot.get("validity"),
                                                                (boolean)documentSnapshot.get("already_used")));
                                                    }
                                                }
                                                if(OnRewardFragment) {
                                                    //WishlistFragment.favouriteAdapter.notifyDataSetChanged();
                                                }
                                            }else{
                                                String error=task.getException().getMessage();
                                                Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                                            }
                                            loadingdialog.dismiss();
                                        }
                                    });
                        }else{
                            loadingdialog.dismiss();
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public static void loadOrders(final Context context, final MyOrderAdapter myOrderAdapter, final Dialog loadingDialog){
        myOrderItemModelList.clear();
        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_Orders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                firebaseFirestore.collection("Orders").document(documentSnapshot.getString("order_id")).collection("OrderItems").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (DocumentSnapshot orderItems : task.getResult().getDocuments()){

                                                        final MyOrderItemModel myOrderItemModel = new MyOrderItemModel(orderItems.getString("Product Id"),orderItems.getString("Order Status"),
                                                                orderItems.getString("Address"),orderItems.getString("Coupen Id"),orderItems.getString("Cutted Price"),
                                                                orderItems.getDate("Ordered Date"),orderItems.getDate("Packed Date"),orderItems.getDate("Shipped Date"),
                                                                orderItems.getDate("Delivered Date"),orderItems.getDate("Cancelled Date"),orderItems.getString("Discounted Price"),
                                                                orderItems.getLong("Free Coupens"),orderItems.getString("Order ID"),orderItems.getString("Payment Method"),
                                                                orderItems.getString("Pin Code"),orderItems.getString("Product Price"),orderItems.getLong("Product Quantity"),
                                                                orderItems.getString("User Id"),orderItems.getString("Product Image"),orderItems.getString("Product Title"));

                                                        myOrderItemModelList.add(myOrderItemModel);

                                                    }

                                                    loadRatingList(context);
                                                    myOrderAdapter.notifyDataSetChanged();

                                                }else{
                                                    String error=task.getException().getMessage();
                                                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });
                            }
                        }else{
                            loadingDialog.dismiss();
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void clearData(){
        categoryModelList.clear();
        lists.clear();
        wishlist.clear();
        favouriteModelList.clear();
        cartList.clear();
        cartItemModelList.clear();
        rewardModelList.clear();
        myratedIds.clear();
        myRating.clear();
        addressesModelList.clear();
        myOrderItemModelList.clear();
    }
}





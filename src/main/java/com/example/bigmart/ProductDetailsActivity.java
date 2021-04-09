package com.example.bigmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmart.adapter.MyRewardAdapter;
import com.example.bigmart.adapter.ProductDetailsAdapter;
import com.example.bigmart.adapter.ProductImagesAdapter;
import com.example.bigmart.modal.CartItemModel;
import com.example.bigmart.modal.FavouriteModel;
import com.example.bigmart.modal.ProductSpecificationModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bigmart.RewardFragment.myRewardAdapter;

public class ProductDetailsActivity extends AppCompatActivity {

    private ViewPager productImagesviewpager;
    private TabLayout viewpagerindicator;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;


    ////////////////        ///////////////////

    public static boolean runningwishlist_query=false;
    public static boolean runningrating_query=false;
    public static boolean runningcart_query=false;
    public static Activity productDetailsActivity;


    private TextView producttitle;
    private TextView productprice,totalratingview,totalratingmini;
    private Button coupenReedembtn;
    public static FloatingActionButton addwishlist;
    public static boolean addedtowishlist=false;
    public static boolean alreadyaddedtocart=false;
    private Button buynowbtn,addtocartbtn;

    ////coupen dialog////
    private RecyclerView coupenrecycleview;
    private LinearLayout selectedCoupen;
    private TextView coupenTitle;
    private TextView coupenbody;
    private TextView coupenexpiry;
    private TextView discountedprice;
    private TextView originalprice;

    ////rating layout
    public static LinearLayout ratenowcontainer;
    private TextView totalRatings;
    private LinearLayout ratingsnumbercontainer;
    private TextView totalratingfigure;
    private LinearLayout ratingprogressbarcontainer;
    private TextView averagerating;
    public static int initialRating;
    ////rating layout

    private FirebaseFirestore firebaseFirestore;


    private RecyclerView prodouctItemRecycler;
    private Dialog signinDialog;
    private Dialog loadingDialog;
    private LinearLayout coupenReedemLayout;

    private FirebaseUser currentUser;
    private ConstraintLayout orderconfirmlayout;
    private Button continueshopping;
    public static String productId;

    private DocumentSnapshot documentSnapshot;
    public static MenuItem cartItem;
    private TextView badgecount;
    private LinearLayout addtocartbutton;
    private String productPricevalue;
    private TextView cuttedPrice;

    ///product description
    private ConstraintLayout productDetailsOnlycontainer;
    private ConstraintLayout productDetailsTabcontainer;
    private String productDescription;
    private String productOtherDetails;
   // public static int tabPosition = -1;
    private TextView productOnlyDescriptionBody;
    public static List<ProductSpecificationModal> productSpecificationModalList = new ArrayList<>(); // private List<> to change

    public static boolean fromSearch=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addwishlist = findViewById(R.id.addwishlist);
        currentUser= FirebaseAuth.getInstance().getCurrentUser();

        ///////////////                 /////////////

        ratenowcontainer = findViewById(R.id.rate_now_container);

        totalratingview=findViewById(R.id.tv_product_rating);
        totalratingmini=findViewById(R.id.total_rating_miniview);

        totalRatings=findViewById(R.id.total_ratings);
        ratingsnumbercontainer=findViewById(R.id.ratings_numbers);
        totalratingfigure=findViewById(R.id.total_ratings_lfigure);
        ratingprogressbarcontainer=findViewById(R.id.ratings_progressbarcontainer);
        averagerating=findViewById(R.id.average_rating);
        initialRating=-1;

        orderconfirmlayout=findViewById(R.id.orderconfirmation);
        continueshopping=findViewById(R.id.continueshoppingbtn);
        producttitle=findViewById(R.id.resturant_name);
        productprice=findViewById(R.id.resturant_title);
        coupenReedembtn=findViewById(R.id.coupen_redemption_button);
        cuttedPrice = findViewById(R.id.cutedprice);

        ////loading Dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        ////loading Dialog

        productDetailsTabcontainer = findViewById(R.id.product_details_tab_container);
        productDetailsOnlycontainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.textView13);
        buynowbtn = findViewById(R.id.buynowbtn);
        addtocartbtn = findViewById(R.id.addtocart);


        ////coupen Dialog




        final Dialog checkpricedialog=new Dialog(ProductDetailsActivity.this);
        checkpricedialog.setContentView(R.layout.coupen_reedem_dialog);
        checkpricedialog.setCancelable(true);
        checkpricedialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toglerecyclerview = checkpricedialog.findViewById(R.id.toggle_recycler);
        coupenrecycleview=checkpricedialog.findViewById(R.id.coupens_recycler);
        selectedCoupen=checkpricedialog.findViewById(R.id.selected_coupen);
        coupenTitle=checkpricedialog.findViewById(R.id.coupentitle);
        coupenexpiry=checkpricedialog.findViewById(R.id.coupenvalidity);
        coupenbody=checkpricedialog.findViewById(R.id.coupenbody);

        originalprice=checkpricedialog.findViewById(R.id.originalprice);
        discountedprice=checkpricedialog.findViewById(R.id.redemedprice);


        //////////////                //////////////


        productDetailsViewPager = findViewById(R.id.product_details_viewpager);
        productDetailsTabLayout = findViewById(R.id.product_details_tablayout);
        productImagesviewpager = findViewById(R.id.product_images_viewpager);
        viewpagerindicator = findViewById(R.id.viewpagerIndicator);



       ////////////

        LinearLayoutManager layoutManager=new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupenrecycleview.setLayoutManager(layoutManager);

        toglerecyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialogRecyclerview();
            }
        });



        firebaseFirestore=FirebaseFirestore.getInstance();
        final List<String> productImage=new ArrayList<>();
        productId  = getIntent().getStringExtra("PRODUCT_ID");

        firebaseFirestore.collection("Products").document(productId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            documentSnapshot=task.getResult();
                            for(long x=1;x<(long)documentSnapshot.get("no_of_images")+1;x++){
                                productImage.add(documentSnapshot.get("product_image_"+x).toString());
                            }
                            ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImage);
                            productImagesviewpager.setAdapter(productImagesAdapter);


                            producttitle.setText(documentSnapshot.get("product_title").toString());
                            productprice.setText(documentSnapshot.get("product_price").toString()+"/-");
                            ///for coupen dialog   chnaging the adapter access from Reward fragment
                            myRewardAdapter=new MyRewardAdapter(DbQueries.rewardModelList,true,coupenrecycleview,selectedCoupen,productPricevalue,coupenTitle,coupenexpiry,coupenbody,discountedprice);
                            coupenrecycleview.setAdapter(myRewardAdapter);
                            myRewardAdapter.notifyDataSetChanged();
                            ///for coupen dialog
                            originalprice.setText(productprice.getText());
                            productPricevalue = documentSnapshot.get("product_price").toString();
                            totalratingview.setText(documentSnapshot.get("average_rating").toString());
                            totalratingmini.setText((long)documentSnapshot.get("total_rating")+" total ratings");
                            cuttedPrice.setText(documentSnapshot.get("cuted_price").toString());

                            coupenTitle.setText((long)documentSnapshot.get("free_coupens")+documentSnapshot.get("free_coupen_title").toString());
                            coupenbody.setText(documentSnapshot.get("free_coupen_body").toString());

                            if((boolean)documentSnapshot.get("use_tab_layout")){
                                productDetailsTabcontainer.setVisibility(View.VISIBLE);
                                productDetailsOnlycontainer.setVisibility(View.GONE);
                                productDescription = documentSnapshot.get("product_description").toString();

                                productOtherDetails = documentSnapshot.get("product_other_details").toString();

                                for (long x = 1;x < (long)documentSnapshot.get("total_spec_title")+1;x++){
                                    productSpecificationModalList.add(new ProductSpecificationModal(0,documentSnapshot.get("spec_title_"+x).toString()));
                                   for (long y= 1;y<(long)documentSnapshot.get("spec_title_"+x+"_total_fields")+1;y++){
                                       productSpecificationModalList.add(new ProductSpecificationModal(1,documentSnapshot.get("spec_title_"+x+"_field_"+y+"_name").toString(),documentSnapshot.get("spec_title_"+x+"_field_"+y+"_value").toString()));
                                   }
                                }

                            }else{
                                productDetailsTabcontainer.setVisibility(View.GONE);
                                productDetailsOnlycontainer.setVisibility(View.VISIBLE);
                                productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                            }


                            totalRatings.setText((long)documentSnapshot.get("total_rating")+"ratings");
                            averagerating.setText(documentSnapshot.get("average_rating").toString());

                            for(int x=0;x<5;x++){
                                TextView rating= (TextView) ratingsnumbercontainer.getChildAt(x);
                                rating.setText(String.valueOf((long)documentSnapshot.get((5-x) +"_star")));

                                ProgressBar progressBar=(ProgressBar)ratingprogressbarcontainer.getChildAt(x);
                                int maxprogress =Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_rating")));
                                progressBar.setMax(maxprogress);
                                progressBar.setProgress(Integer.parseInt(String.valueOf((long)documentSnapshot.get((5-x) +"_star"))));

                            }
                            totalratingfigure.setText(String.valueOf((long)documentSnapshot.get("total_rating")));

                            if(currentUser!=null) {
                                if(DbQueries.myRating.size()==0){
                                    DbQueries.loadRatingList(ProductDetailsActivity.this);
                                }

                                if (DbQueries.cartList.size() == 0) {
                                    DbQueries.loadCartList(ProductDetailsActivity.this, loadingDialog,false,badgecount, new TextView(ProductDetailsActivity.this));

                                }

                                if (DbQueries.wishlist.size() == 0) {
                                    DbQueries.loadWishlist(ProductDetailsActivity.this, loadingDialog, false);

                                }
                                if (DbQueries.rewardModelList.size() == 0) {
                                    DbQueries.loadRewards(ProductDetailsActivity.this, loadingDialog,false);
                                }

                                if(DbQueries.cartList.size()!=0 && DbQueries.wishlist.size()!=0 && DbQueries.rewardModelList.size()!=0){
                                    loadingDialog.dismiss();
                                }


                            }else{
                                loadingDialog.dismiss();
                            }


                            if(DbQueries.myratedIds.contains(productId)){
                                int index = DbQueries.myratedIds.indexOf(productId);
                                initialRating=Integer.parseInt(String.valueOf(DbQueries.myRating.get(index)))-1;
                                setRating(initialRating);
                            }

                            if(DbQueries.cartList.contains(productId)){
                                alreadyaddedtocart=true;

                            }else{
                                alreadyaddedtocart=false;
                            }


                            if(DbQueries.wishlist.contains(productId)){
                                addedtowishlist=true;
                                addwishlist.setSupportImageTintList(getResources().getColorStateList(R.color.red));

                            }else{
                                addwishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                addedtowishlist=false;
                            }

                            if((boolean)documentSnapshot.get("in_stock")){

                                addtocartbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(currentUser==null){
                                            signinDialog.show();
                                        }else{
                                            if(!runningcart_query){
                                                runningcart_query=true;
                                                if (alreadyaddedtocart) {
                                                    runningcart_query=false;
                                                    Toast.makeText(ProductDetailsActivity.this,"Already added to cart",Toast.LENGTH_SHORT).show();
                                                } else {

                                                    Map<String, Object> addproduct = new HashMap<>();
                                                    addproduct.put("productid_"+ String.valueOf(DbQueries.cartList.size()), productId);
                                                    addproduct.put("list_size", (long) DbQueries.cartList.size() + 1);


                                                    firebaseFirestore.collection("users").document(currentUser.getUid()).collection("User_data").document("My_Cart")
                                                            .update(addproduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                if (DbQueries.cartItemModelList.size() != 0) {
                                                                    DbQueries.cartItemModelList.add(0,new CartItemModel(CartItemModel.CART_ITEM, productId,
                                                                            documentSnapshot.get("product_image_1").toString(),
                                                                            documentSnapshot.get("product_title").toString(),
                                                                            (long) documentSnapshot.get("free_coupens"),
                                                                            documentSnapshot.get("product_price").toString(),
                                                                            documentSnapshot.get("cuted_price").toString(),
                                                                            (long) 2,
                                                                            (long) 1,
                                                                            (boolean) documentSnapshot.get("in_stock"),
                                                                            (long)documentSnapshot.get("max-quantity"),
                                                                            (long)2));
                                                                }


                                                                alreadyaddedtocart = true;
                                                                DbQueries.cartList.add(productId);
                                                                Toast.makeText(ProductDetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                                                                invalidateOptionsMenu();
                                                                runningcart_query = false;
                                                            } else {
                                                                runningcart_query=false;
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }

                                            }
                                        }
                                    }
                                });


                            }else{
                                buynowbtn.setVisibility(View.GONE);
                                TextView outofStock = (TextView) addtocartbutton.getChildAt(0);
                                outofStock.setText("Out of Stock");
                                outofStock.setTextColor(getResources().getColor(R.color.white));
                                outofStock.setCompoundDrawables(null,null,null,null);


                            }

                        }else{
                            loadingDialog.dismiss();
                            String error=task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this,error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });



        for(int x=0;x<ratenowcontainer.getChildCount();x++){
            final int starPosition =x;
            ratenowcontainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentUser==null){
                        signinDialog.show();
                    }else {
                        if (starPosition != initialRating) {
                            if (!runningrating_query) {
                                runningrating_query = true;
                                setRating(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DbQueries.myratedIds.contains(productId)) {

                                    TextView oldRating = (TextView) ratingsnumbercontainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsnumbercontainer.getChildAt(5 - starPosition - 1);


                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));


                                } else {

                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));
                                    updateRating.put("total_rating", (long) documentSnapshot.get("total_rating") + 1);
                                }

                                firebaseFirestore.collection("Products").document(productId)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> myrating = new HashMap<>();
                                            if (DbQueries.myratedIds.contains(productId)) {

                                                myrating.put("rating_" + DbQueries.myratedIds.indexOf(productId), (long) starPosition + 1);

                                            } else {

                                                myrating.put("list_size", (long) DbQueries.myratedIds.size() + 1);
                                                myrating.put("productid_" + DbQueries.myratedIds.size(), productId);
                                                myrating.put("rating_" + DbQueries.myratedIds.size(), (long) starPosition + 1);
                                            }

                                            firebaseFirestore.collection("users").document(currentUser.getUid()).collection("User_data")
                                                    .document("My_rating").update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {


                                                        if (DbQueries.myratedIds.contains(productId)) {
                                                            DbQueries.myRating.set(DbQueries.myratedIds.indexOf(productId), (long) starPosition + 1);

                                                            TextView oldRating = (TextView) ratingsnumbercontainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalRating = (TextView) ratingsnumbercontainer.getChildAt(5 - starPosition - 1);

                                                            oldRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));

                                                        } else {

                                                            DbQueries.myratedIds.add(productId);
                                                            DbQueries.myRating.add((long) starPosition + 1);

                                                            TextView rating = (TextView) ratingsnumbercontainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));
                                                            totalratingview.setText(documentSnapshot.get("average_rating").toString());
                                                            totalratingfigure.setText(String.valueOf((long) documentSnapshot.get("total_rating") + 1));


                                                            Toast.makeText(ProductDetailsActivity.this, "Thanks for Ratings", Toast.LENGTH_SHORT).show();
                                                        }


                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingfigures = (TextView) ratingsnumbercontainer.getChildAt(x);

                                                            ProgressBar progressBar = (ProgressBar) ratingprogressbarcontainer.getChildAt(x);

                                                            int maxprogress = Integer.parseInt(totalratingfigure.getText().toString());
                                                            progressBar.setMax(maxprogress);

                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                        }
                                                        initialRating = starPosition;
                                                        totalratingmini.setText(calculateAverageRating(0, false));
                                                        averagerating.setText(calculateAverageRating(0, true));

                                                        if (DbQueries.wishlist.contains(productId) && DbQueries.favouriteModelList.size() != 0) {
                                                            int index = DbQueries.wishlist.indexOf(productId);
                                                            FavouriteModel changeRatings = DbQueries.favouriteModelList.get(index);
                                                            DbQueries.favouriteModelList.get(index).setTotalrating(Long.parseLong(averagerating.getText().toString()));
                                                            DbQueries.favouriteModelList.get(index).setTotalrating(Long.parseLong(totalratingfigure.getText().toString()));


                                                        }

                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    runningrating_query = false;
                                                }
                                            });

                                        } else {
                                            runningrating_query = false;
                                            setRating(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }
                        }
                    }

                }
            });
        }

        viewpagerindicator.setupWithViewPager(productImagesviewpager,true);


        productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),productDetailsTabLayout.getTabCount(),productDescription,productOtherDetails,productSpecificationModalList));

        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));

        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        buynowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser==null){
                    signinDialog.show();
                }else {
                    DeliveryActivity.fromCart = false;
                    loadingDialog.show();
                    productDetailsActivity = ProductDetailsActivity.this;
                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM, productId,
                            documentSnapshot.get("product_image_1").toString(),
                            documentSnapshot.get("product_title").toString(),
                            (long) documentSnapshot.get("free_coupens"),
                            documentSnapshot.get("product_price").toString(),
                            documentSnapshot.get("cuted_price").toString(),
                            (long) 2,
                            (long) 1,
                            (boolean) documentSnapshot.get("in_stock"),
                            (long)documentSnapshot.get("max-quantity"),
                            (long)2));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                    if(DbQueries.addressesModelList.size()==0){
                        DbQueries.loadAddresses(ProductDetailsActivity.this,loadingDialog);
                    }else{
                        loadingDialog.dismiss();
                        Intent deliveryIntet = new Intent(ProductDetailsActivity.this,DeliveryActivity.class);
                        startActivity(deliveryIntet);
                    }

                    Intent deliveryintent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(deliveryintent);
                }
            }
        });




        addwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signinDialog.show();
                } else {
                    //    addwishlist.setEnabled(false);
                    if(!runningwishlist_query){
                        runningwishlist_query=true;
                        if (addedtowishlist) {
                            int index=DbQueries.wishlist.indexOf(productId);
                            DbQueries.removeFromWishlist(index,ProductDetailsActivity.this);
                            // addedtowishlist = false;
                            addwishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {


                            Map<String, Object> addproduct = new HashMap<>();
                            addproduct.put("productid_" + String.valueOf(DbQueries.wishlist.size()), productId);
                            addproduct.put("list_size", (long) DbQueries.wishlist.size() + 1);


                            firebaseFirestore.collection("users").document(currentUser.getUid()).collection("User_data").document("My_wishlist")
                                    .update(addproduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (DbQueries.favouriteModelList.size() != 0) {
                                            DbQueries.favouriteModelList.add(new FavouriteModel(productId, documentSnapshot.get("product_image_1").toString(),
                                                    documentSnapshot.get("product_title").toString(),
                                                    documentSnapshot.get("productsubtitle").toString(),
                                                    (long) documentSnapshot.get("product_price"),
                                                    (boolean) documentSnapshot.get("in_stock")));
                                        }
                                        addedtowishlist = true;
                                        addwishlist.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                                        DbQueries.wishlist.add(productId);
                                        Toast.makeText(ProductDetailsActivity.this, "Added To Favourite", Toast.LENGTH_SHORT).show();
                                    } else {

                                        addwishlist.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    runningwishlist_query=false;
                                }
                            });

                        }

                    }
                }
            }
        });




        ///coupen Dialog

        coupenReedembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkpricedialog.show();
            }
        });



        ///signindialog
        signinDialog = new Dialog(ProductDetailsActivity.this);
        signinDialog.setContentView(R.layout.signin_dialog);
        signinDialog.setCancelable(true);

        signinDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button signindialogbtn = signinDialog.findViewById(R.id.signinbutton);
        Button signupdialogbtn= signinDialog.findViewById(R.id.signupbutton);
        final Intent registerIntent = new Intent(ProductDetailsActivity.this,RegisterActivity.class);
        signindialogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SigninFragment.disableclosebtn=true;
                SignupFragment.disableclosebtn=true;
                signinDialog.dismiss();
//                setSignupFragment=false;
                startActivity(registerIntent);
            }
        });

        signupdialogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SigninFragment.disableclosebtn=true;
                SignupFragment.disableclosebtn=true;
                signinDialog.dismiss();
//                setSignupFragment=true;
                startActivity(registerIntent);
            }
        });



        ////////////


    }

    private String calculateAverageRating(long currentUserRating,boolean update){
        Double totalStars = Double.valueOf(0);
        for(int x=1;x<6;x++){
            TextView ratingNo= (TextView) ratingsnumbercontainer.getChildAt(5-x);
            totalStars=totalStars+ (Long.parseLong(ratingNo.getText().toString())*x);
        }
        totalStars=totalStars+currentUserRating;
        if(update){
            return String.valueOf(totalStars / Long.parseLong(totalratingfigure.getText().toString())).substring(0,3);
        }else {
            return String.valueOf(totalStars / Long.parseLong(totalratingfigure.getText().toString()+1)).substring(0,3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_and_cart,menu);

        cartItem = menu.findItem(R.id.main_cart_icon);


        cartItem.setActionView(R.layout.badge_layout);
        ImageView badgeicon=cartItem.getActionView().findViewById(R.id.badge_icon);
        badgeicon.setImageResource(R.drawable.cart_white);
        TextView badgecount = cartItem.getActionView().findViewById(R.id.badge_count);


        if(currentUser!=null){
            if(DbQueries.cartList.size()==0){
                DbQueries.loadCartList(ProductDetailsActivity.this,loadingDialog,false,badgecount,new TextView(ProductDetailsActivity.this));
            }else{
                badgecount.setVisibility(View.VISIBLE);
                if(DbQueries.cartList.size()<50) {
                    badgecount.setText(String.valueOf(DbQueries.cartList.size()));
                }else{
                    badgecount.setText("50+");
                }
            }
        }
        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser==null) {
                    signinDialog.show();
                }else{
//                    Intent cartIntent = new Intent(ProductDetailsActivity.this,CartFragment.class);
//                    startActivity(cartIntent);
                }
            }
        });

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch = false;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==android.R.id.home){
            productDetailsActivity = null;
            finish();
            return true;
        }else if(id==R.id.main_search_icon){
            if(fromSearch){
                finish();
            }else{
//                Intent searchIntent = new Intent(this,SearchActivity.class);
//                startActivity(searchIntent);
            }
            return true;
        }else if(id==R.id.main_cart_icon){
            if(currentUser==null) {
                signinDialog.show();
            }else{
                Intent cartIntent = new Intent(ProductDetailsActivity.this,CartFragment.class);
                startActivity(cartIntent);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }



    public static void setRating(int starPosition){

        for(int x=0;x < ratenowcontainer.getChildCount();x++) {
            ImageView starBtn = (ImageView) ratenowcontainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    private void showdialogRecyclerview(){
        if(coupenrecycleview.getVisibility()==View.GONE){
            coupenrecycleview.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        }else{
            coupenrecycleview.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        super.onBackPressed();
    }
}


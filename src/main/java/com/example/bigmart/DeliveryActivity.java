package com.example.bigmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
 
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bigmart.adapter.CartAdapter;
import com.example.bigmart.modal.CartItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {



    public static List<CartItemModel> cartItemModelList;
    public static boolean fromCart;
    private RecyclerView deliveryRecyclerview;
    private Button changeOrAddbtn;
    public static final int SELECT_ADDRESS=0;
    private Button continuebutton;
    private Dialog loadingDialog;
    private Dialog paymentmenthodDialog;
    private ImageButton paytm,cod;
    private String name,mobileno;
    private TextView totalAmount;
    private ConstraintLayout orderConfirmationLaout;
    private Button continueshoppingbtn;
    private TextView orderId;
    private boolean successResponse = false;
    private String order_id;
    public static boolean codOrderConfirmed = false;
    private FirebaseFirestore firebaseFirestore;
    private boolean allProductsAvailable = true;
    public static boolean getQtyIds = true;
    private String paymentMethod = "PAYTM";
    private TextView fullName,fullAddress,pincode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");
        changeOrAddbtn=findViewById(R.id.changeoraddressbtn);
        fullName=findViewById(R.id.address_fulname);
        fullAddress=findViewById(R.id.fulladdress);
        pincode=findViewById(R.id.address_pincode);
        totalAmount = findViewById(R.id.total_cart_amount);
        cod=findViewById(R.id.cod);
        orderConfirmationLaout = findViewById(R.id.orderconfirmation);
        continueshoppingbtn = findViewById(R.id.continueshoppingbtn);
        orderId = findViewById(R.id.order_id);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIds = true;


        loadingDialog=new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.payment_method);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.shape2));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);




        ////payment dialog
        paymentmenthodDialog=new Dialog(DeliveryActivity.this);
        paymentmenthodDialog.setContentView(R.layout.payment_method);
        paymentmenthodDialog.setCancelable(true);
        paymentmenthodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.shape2));
        paymentmenthodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //payment dialog

        order_id = UUID.randomUUID().toString().substring(0,28);


        continuebutton=findViewById(R.id.cartcontinuebtn);
        paytm=paymentmenthodDialog.findViewById(R.id.paytm);
        deliveryRecyclerview=findViewById(R.id.deliveryRecycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerview.setLayoutManager(layoutManager);

        CartAdapter cartAdapter = new CartAdapter(DbQueries.cartItemModelList,totalAmount,false);
        deliveryRecyclerview.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeOrAddbtn.setVisibility(View.VISIBLE);
        changeOrAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIds = false;
                Intent myaddressesintent = new Intent(DeliveryActivity.this,MyaddressesActivity.class);
                myaddressesintent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(myaddressesintent);
            }
        });


        fullName.setText(DbQueries.addressesModelList.get(DbQueries.selectedAddress).getFunnName());
        fullAddress.setText(DbQueries.addressesModelList.get(DbQueries.selectedAddress).getAddress());
        pincode.setText(DbQueries.addressesModelList.get(DbQueries.selectedAddress).getPinCode());




        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allProductsAvailable) {
                    paymentmenthodDialog.show();
                }else{

                }

            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             paymentMethod = "COD";
             placeOrderDetails();
            }
        });

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "PAYTM";
                paytm();
            }
        });



    }


    @Override
    protected void onStart() {


        super.onStart();
        //accessing quantity
        if (getQtyIds) {
            for (int x = 0; x < DbQueries.cartItemModelList.size() - 1; x++) {
                final int finalX = x;
                final int finalX1 = x;
                firebaseFirestore.collection("Products").document(DbQueries.cartItemModelList.get(x).getProductId()).collection("Quantity").orderBy("available", Query.Direction.DESCENDING).limit(DbQueries.cartItemModelList.get(x).getProductQuantity()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        if ((boolean) queryDocumentSnapshot.get("available")) {
                                            firebaseFirestore.collection("Products").document(DbQueries.cartItemModelList.get(finalX1).getProductId()).collection("Quantity").document(queryDocumentSnapshot.getId()).update("available", false)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                DbQueries.cartItemModelList.get(finalX).getQtyIds().add(queryDocumentSnapshot.getId());
                                                            } else {

                                                            }
                                                        }
                                                    });

                                        } else {
                                            //not available
                                            allProductsAvailable = false;
                                            Toast.makeText(DeliveryActivity.this, "all products may not be available", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }else{
            getQtyIds = true;
        }

        name=DbQueries.addressesModelList.get(DbQueries.selectedAddress).getFunnName();
        mobileno=DbQueries.addressesModelList.get(DbQueries.selectedAddress).getMobileno();
        fullName.setText(name+" "+mobileno);
        fullName.setText(DbQueries.addressesModelList.get(DbQueries.selectedAddress).getFunnName());
        fullAddress.setText(DbQueries.addressesModelList.get(DbQueries.selectedAddress).getAddress());
        pincode.setText(DbQueries.addressesModelList.get(DbQueries.selectedAddress).getPinCode());
        if (codOrderConfirmed){
            showConfirmationLayout();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
        if (getQtyIds) {
            for (int x = 0; x < DbQueries.cartItemModelList.size() - 1; x++) {
                if (!successResponse) {
                    for (String qtyId : DbQueries.cartItemModelList.get(x).getQtyIds()) {
                        firebaseFirestore.collection("Products").document(DbQueries.cartItemModelList.get(x).getProductId()).collection("Quantity").document(qtyId).update("available", true);
                    }
                }
                DbQueries.cartItemModelList.get(x).getQtyIds().clear();
            }
        }
    }


    private void showConfirmationLayout(){
        successResponse = true;
        codOrderConfirmed = false;
        getQtyIds = false;

        for (int x=0;x<DbQueries.cartItemModelList.size()-1;x++){
            for (String qtyId : DbQueries.cartItemModelList.get(x).getQtyIds()){
                firebaseFirestore.collection("Products").document(DbQueries.cartItemModelList.get(x).getProductId()).collection("Quantity").document(qtyId).update("user_ID",FirebaseAuth.getInstance().getUid());


            }

        }

        if (MainActivity.mainActivity != null){
            MainActivity.mainActivity.finish();
            MainActivity.mainActivity = null;
            // MainActivity.showCart = false;
        }else{
            MainActivity.resetMainactivity = true;
        }

        if (ProductDetailsActivity.productDetailsActivity != null){
            ProductDetailsActivity.productDetailsActivity.finish();
            ProductDetailsActivity.productDetailsActivity = null;
        }

        //sent confirmation message
        String SMS_API="https://www.fast2sms.com/dev/bulk";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("authorization","ljhdfehwofioiewger59045jferfhoqiri4tu0fefjjpanvzpsrjagaf4");
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("sender_id","FSTSMS");
                body.put("language","FSTSMS");
                body.put("route","FSTSMS");
                body.put("numbers",mobileno);
                body.put("message","6706");
                body.put("variables","{#FF#}");
                body.put("variables_value",order_id);
                return body;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        requestQueue.add(stringRequest);
        //sent confirmation message




        if (fromCart){
            loadingDialog.show();
            Map<String,Object> updatecartlist = new HashMap<>();
            long cartListSize = 0;
            final List<Integer> indexList = new ArrayList<>();

            for(int x=0;x<DbQueries.cartList.size();x++){
                if (DbQueries.cartItemModelList.get(x).isInstock()){
                    updatecartlist.put("productid_"+cartListSize,DbQueries.cartItemModelList.get(x).getProductId());
                    cartListSize++;
                }else{
                    indexList.add(x);
                }
            }
            updatecartlist.put("list_size",cartListSize);

            FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_data").document("My_Cart")
                    .set(updatecartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        for (int x = 0; x<indexList.size();x++){
                            DbQueries.cartList.remove(indexList.get(x).intValue());
                            DbQueries.cartItemModelList.remove(indexList.get(x).intValue());
                            DbQueries.cartItemModelList.remove(DbQueries.cartItemModelList.size()-1);
                        }
                    }else{
                        String error = task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this,error,Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            });

        }

        continuebutton.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderId.setText("Order Id "+order_id);
        orderConfirmationLaout.setVisibility(View.VISIBLE);
        continueshoppingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void placeOrderDetails(){
        String userId = FirebaseAuth.getInstance().getUid();
        loadingDialog.show();
        for (CartItemModel cartItemModel : DbQueries.cartItemModelList){
            if (cartItemModel.getType() == CartItemModel.CART_ITEM){
                Map<String,Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER_ID",order_id);
                orderDetails.put("Product Id",cartItemModel.getProductId());
                orderDetails.put("Product Image",cartItemModel.getProductimage());
                orderDetails.put("Product Title",cartItemModel.getProducttitle());
                orderDetails.put("User Id",userId);
                orderDetails.put("Product Quantity",cartItemModel.getProductQuantity());
                if (cartItemModel.getCutprice() != null) {
                    orderDetails.put("Cutted Price", cartItemModel.getCutprice());
                }else{
                    orderDetails.put("Cutted Price", "");
                }
                orderDetails.put("Product Price",cartItemModel.getProductprice());
                if (cartItemModel.getSelectedCoupenId() != null) {
                    orderDetails.put("Coupen Id", cartItemModel.getSelectedCoupenId());
                }else{
                    orderDetails.put("Discount Price", "");
                }
                orderDetails.put("Ordered date", FieldValue.serverTimestamp());
                orderDetails.put("Packed date", FieldValue.serverTimestamp());
                orderDetails.put("Shipped date", FieldValue.serverTimestamp());
                orderDetails.put("Delivered date", FieldValue.serverTimestamp());
                orderDetails.put("Cancelled date", FieldValue.serverTimestamp());
                orderDetails.put("Ordered Status","Ordered");
                orderDetails.put("Payment Method","");
                orderDetails.put("Address",fullAddress.getText());
                orderDetails.put("Full Name",fullName.getText());
                orderDetails.put("PinCode",pincode.getText());
                orderDetails.put("Free Coupen",cartItemModel.getFreecoupen());

                firebaseFirestore.collection("Orders").document(order_id).collection("OrderItems").document(cartItemModel.getProductId())
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{
                Map<String,Object> orderDetails = new HashMap<>();
                orderDetails.put("Total Items",cartItemModel.getTotalitems());
                orderDetails.put("Total Items Price",cartItemModel.getTotalItemPrice());
                orderDetails.put("Delivery Price",cartItemModel.getDeliveryPrice());
                orderDetails.put("Total Amount",cartItemModel.getTotalAmount());
                orderDetails.put("Saved Amount",cartItemModel.getSavedAmount());
                orderDetails.put("Payment Status","not paid");
                orderDetails.put("Order Status","Cancelled");

                firebaseFirestore.collection("Orders").document(order_id).
                        set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (paymentMethod.equals("PAYTM")){
                                paytm();
                            }else{
                                cod();
                            }
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }


    private void paytm(){
        getQtyIds = false;
        paymentmenthodDialog.show();
        loadingDialog.show();
        if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        final String M_id = "diwldiwl430Sdiwlad";
        final String customer_id = FirebaseAuth.getInstance().getUid();
        String url = "https://bigmart.000wwebhostapp.com/paytm/generateChecksum.php";
        final String callBack_url = "https://securegw-stage.paytm.in/theia/paytmCallback";

        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH")){
                        String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                        //
                        PaytmPGService paytmPGService = PaytmPGService.getProductionService();
                        HashMap<String, String> paramMap = new HashMap<String,String>();
                        paramMap.put( "MID" , M_id);
                        paramMap.put( "ORDER_ID" , order_id);
                        paramMap.put( "CUST_ID" , customer_id);
                        paramMap.put( "CHANNEL_ID" , "WAP");
                        paramMap.put( "TXN_AMOUNT" , totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2));
                        paramMap.put( "WEBSITE" , "WEBSTAGING");
                        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                        paramMap.put( "CALLBACK_URL", callBack_url);
                        paramMap.put("CHECKSUMHASH",CHECKSUMHASH);

                        PaytmOrder order = new PaytmOrder(paramMap);

                        paytmPGService.initialize(order,null);
                        paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                            @Override
                            public void onTransactionResponse(Bundle inResponse) {
                                // Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                                if (inResponse.getString("STATUS").equals("TXN_SUCCESS")) {

                                        Map<String,Object> updateStatus = new HashMap<>();
                                        updateStatus.put("Payment Status","Paid");
                                        updateStatus.put("Order Status","Ordered");
                                        firebaseFirestore.collection("Order").document(order_id).update(updateStatus)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Map<String,Object> userOrder = new HashMap<>();
                                                            userOrder.put("Order_Id",order_id);
                                                            firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("User_Orders").document(order_id).set(userOrder)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                showConfirmationLayout();
                                                                            }else{
                                                                                Toast.makeText(DeliveryActivity.this, "Failed to Update user's Order List", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });

                                                        }else{
                                                            Toast.makeText(DeliveryActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                }
                            }

                            @Override
                            public void networkNotAvailable() {
                                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void clientAuthenticationFailed(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {

                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onBackPressedCancelTransaction() {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();

                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                Toast.makeText(DeliveryActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<String,String>();
                paramMap.put( "MID" , M_id);
                paramMap.put( "ORDER_ID" , order_id);
                paramMap.put( "CUST_ID" , customer_id);
                paramMap.put( "CHANNEL_ID" , "WAP");
                paramMap.put( "TXN_AMOUNT" , totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2));
                paramMap.put( "WEBSITE" , "WEBSTAGING");
                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                paramMap.put( "CALLBACK_URL", callBack_url);
                return paramMap;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void cod(){
        getQtyIds = false;
        paymentmenthodDialog.dismiss();
        Intent optintent = new Intent(DeliveryActivity.this,OtpVefificationActivity.class);
        optintent.putExtra("mobileNo",mobileno.substring(0,10));
        optintent.putExtra("Order_ID",order_id);

        startActivity(optintent);
    }


    @Override
    public void onBackPressed() {
        if (successResponse){
            finish();
            return;
        }
        super.onBackPressed();
    }
}









package com.example.bigmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OtpVefificationActivity extends AppCompatActivity {

    private TextView phoneno;
    private EditText otp;
    private Button verifybtn;
    private String userno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_vefification);

        phoneno=findViewById(R.id.phoneno);
        otp=findViewById(R.id.otp);
        verifybtn=findViewById(R.id.verify);
        userno=getIntent().getStringExtra("mobileNo");

        phoneno.setText("Verification code has been sent to +91"+ userno);


        Random random = new Random();
        final Integer Otpnumber=random.nextInt(999999-111111)+111111;
        String SMS_API="https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                verifybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(otp.getText().toString().equals(String.valueOf(Otpnumber))){
                            Map<String,Object> updateStatus = new HashMap<>();
                            updateStatus.put("Payment Status"," Not Paid");
                            updateStatus.put("Order Status","Ordered");
                            final String orderId = getIntent().getStringExtra("Order_ID");
                            FirebaseFirestore.getInstance().collection("Order").document(orderId).update(updateStatus)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Map<String,Object> userOrder = new HashMap<>();
                                                userOrder.put("Order_Id",orderId);
                                                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("User_Orders").document(orderId).set(userOrder)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    DeliveryActivity.codOrderConfirmed  = true;
                                                                    finish();
                                                                }else{
                                                                    Toast.makeText(OtpVefificationActivity.this, "Failed to Update user's Order List", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });


                                            }else{
                                                Toast.makeText(OtpVefificationActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }else {
                            Toast.makeText(OtpVefificationActivity.this, "Otp Incorrect", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                Toast.makeText(OtpVefificationActivity.this,"failed",Toast.LENGTH_SHORT).show();
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
                body.put("numbers",userno);
                body.put("message","6436");
                body.put("variables","{#BB#}");
                body.put("variables_value",String.valueOf(Otpnumber));
                return body;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
               5000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(OtpVefificationActivity.this);
        requestQueue.add(stringRequest);
    }
}

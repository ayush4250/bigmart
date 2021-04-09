package com.example.bigmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.Dialog;
import android.content.Intent; 
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.bigmart.modal.AddressesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddAddressActivity extends AppCompatActivity {

    private Button savebtn;

    private EditText city;
    private EditText locality;
    private EditText flatno;
    private EditText pincode;
    private EditText landmark;
    private EditText name;
    private EditText mobileno;
    private EditText alternatmobile;
    private Spinner statespinner;

    private String [] statelist;
    private String selectedState;
    private Dialog loadingDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        savebtn=findViewById(R.id.savebutton);
       // savebutton=findViewById(R.id.savebutton);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ////loading Dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading Dialog
       // statelist=getResources().getStringArray(R.array.india_states);

        city=findViewById(R.id.city);
        locality=findViewById(R.id.locality);
        flatno=findViewById(R.id.flatno);
        pincode=findViewById(R.id.pincode);
        landmark=findViewById(R.id.landmark);
        name=findViewById(R.id.username);
        mobileno=findViewById(R.id.mobileno);
        alternatmobile=findViewById(R.id.alternatemobileno);
//        statespinner=findViewById(R.id.state);

//        ArrayAdapter spinneradapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,statelist);
//        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        statespinner.setAdapter(spinneradapter);

//        statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedState=statelist[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });



        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!TextUtils.isEmpty(city.getText())){
                    if(!TextUtils.isEmpty(locality.getText())){
                        if(!TextUtils.isEmpty(flatno.getText())){
                            if(!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length()==6){
                                if(!TextUtils.isEmpty(name.getText())){
                                    if(!TextUtils.isEmpty(mobileno.getText()) && mobileno.getText().length()==10){

                                        loadingDialog.show();
                                        final String fullAddress = flatno.getText().toString()+" "+locality.getText().toString()+" "+landmark.getText().toString()+" "+city.getText().toString()+" "+selectedState;
                                        Map<String,Object> addAddress =new HashMap<>();
                                        addAddress.put("list_size",(long)DbQueries.addressesModelList.size()+1);
                                        if(TextUtils.isEmpty(alternatmobile.getText())) {
                                            addAddress.put("mobile_no" + String.valueOf((long) DbQueries.addressesModelList.size() + 1),  mobileno.getText().toString());
                                        }else{
                                            addAddress.put("mobile_no" + String.valueOf((long) DbQueries.addressesModelList.size() + 1), mobileno.getText().toString()+" Or "+alternatmobile.getText().toString());
                                        }
                                        addAddress.put("fullname_" + String.valueOf((long) DbQueries.addressesModelList.size() + 1), name.getText().toString());
                                        addAddress.put("address_"+String.valueOf((long)DbQueries.addressesModelList.size()+1),fullAddress);
                                        addAddress.put("pincode_"+String.valueOf((long)DbQueries.addressesModelList.size()+1),pincode.getText().toString());
                                        addAddress.put("selected_"+String.valueOf((long)DbQueries.addressesModelList.size()+1),true);
                                        if(DbQueries.addressesModelList.size()>0) {
                                            addAddress.put("selected_" + (DbQueries.selectedAddress + 1), false);
                                        }

                                        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid())
                                                .collection("User_data").document("My_Addresses")
                                                .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    if(DbQueries.addressesModelList.size()>0) {
                                                        DbQueries.addressesModelList.get(DbQueries.selectedAddress).setSelected(false);
                                                    }
                                                    if(TextUtils.isEmpty(alternatmobile.getText())) {
                                                        DbQueries.addressesModelList.add(new AddressesModel(name.getText().toString(), fullAddress, pincode.getText().toString(), true,  mobileno.getText().toString()));
                                                    }else{
                                                        DbQueries.addressesModelList.add(new AddressesModel(name.getText().toString() , fullAddress, pincode.getText().toString(), true,mobileno.getText().toString()+" or "+alternatmobile.getText().toString()));

                                                    }

                                                    if(getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent deliveryintent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                        startActivity(deliveryintent);
                                                    }else{
                                                        MyaddressesActivity.refreshItem(DbQueries.selectedAddress,DbQueries.addressesModelList.size()-1);
                                                    }
                                                    DbQueries.selectedAddress = DbQueries.addressesModelList.size() - 1;
                                                    finish();
                                                }else{
                                                    String error=task.getException().getMessage();
                                                    Toast.makeText(AddAddressActivity.this,error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });

                                    }else{

                                    }
                                }else{
                                    name.requestFocus();
                                    pincode.requestFocus();
                                    Toast.makeText(AddAddressActivity.this,"Please Enter Valid Mobile No.",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                pincode.requestFocus();
                                Toast.makeText(AddAddressActivity.this,"Please Enter Valid Pin Code",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            flatno.requestFocus();
                        }
                    }else{
                        locality.requestFocus();
                    }
                }else{
                    city.requestFocus();
                }


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

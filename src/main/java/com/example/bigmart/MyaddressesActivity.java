package com.example.bigmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup; 
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigmart.adapter.AddressesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bigmart.AccountFragment.MANAGE_ADDRESS;
import static com.example.bigmart.DeliveryActivity.SELECT_ADDRESS;


public class MyaddressesActivity extends AppCompatActivity {

    private RecyclerView myaddressesRecycler;
    private static AddressesAdapter addressesAdapter;
    private Button deliverherebutton;
    private LinearLayout addnewAddressbtn;
    private TextView addressSaved;
    private int previousAddress;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaddresses);
       
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Addresses");

        addnewAddressbtn=findViewById(R.id.add_newaddressbtn);
        addressSaved=findViewById(R.id.address_save);
        previousAddress=DbQueries.selectedAddress;  //-1
        myaddressesRecycler=findViewById(R.id.addressRecycler);
        deliverherebutton=findViewById(R.id.deliver_here_btn);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myaddressesRecycler.setLayoutManager(layoutManager);

 
        ////loading Dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ////loading Dialog


        int mode=getIntent().getIntExtra("MODE",-1);
        if(mode==SELECT_ADDRESS){
            deliverherebutton.setVisibility(View.VISIBLE);
        }else if (mode==MANAGE_ADDRESS){
            deliverherebutton.setVisibility(View.GONE);
        }

        addressesAdapter = new AddressesAdapter(DbQueries.addressesModelList,mode);
        myaddressesRecycler.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)myaddressesRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        deliverherebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DbQueries.selectedAddress!=previousAddress){
                    final int previousAddressIndex = previousAddress;

                    loadingDialog.show();
                    Map<String, Object> updateSelection = new HashMap<>();
                    updateSelection.put("selected_"+String.valueOf(previousAddress+1),false);
                    updateSelection.put("selected_"+String.valueOf(DbQueries.selectedAddress+1),true);

                    previousAddress = DbQueries.selectedAddress;

                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("User_data").document("My_Addresses")
                            .update(updateSelection).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                finish();
                            }else{
                                previousAddress=previousAddressIndex;
                                String error=task.getException().getMessage();
                                Toast.makeText(MyaddressesActivity.this,error, Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });

                }else{
                    finish();
                }
            }
        });



        Toast.makeText(this, ""+DbQueries.addressesModelList.size(), Toast.LENGTH_SHORT).show();


        addnewAddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressintent = new Intent(MyaddressesActivity.this,AddAddressActivity.class);
                addAddressintent.putExtra("INTENT","null");
                startActivity(addAddressintent);
            }
        });

        addressSaved.setText(String.valueOf(DbQueries.addressesModelList.size()+" saved addresses"));


    }

    @Override
    protected void onStart() {
        super.onStart();
        addressSaved.setText(String.valueOf(DbQueries.addressesModelList.size()+" saved addresses"));

    }

    public static void refreshItem(int deselect, int select){
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            if(DbQueries.selectedAddress!=previousAddress){
                DbQueries.addressesModelList.get(DbQueries.selectedAddress).setSelected(false);
                DbQueries.addressesModelList.get(previousAddress).setSelected(true);
                previousAddress=DbQueries.selectedAddress;
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(DbQueries.selectedAddress!=previousAddress){
            DbQueries.addressesModelList.get(DbQueries.selectedAddress).setSelected(false);
            DbQueries.addressesModelList.get(previousAddress).setSelected(true);
            DbQueries.selectedAddress=previousAddress;
        }
        super.onBackPressed();
    }
}

package com.example.bigmart;


import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.bigmart.adapter.CartAdapter;
import com.example.bigmart.modal.CartItemModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {


    public CartFragment() {
        // Required empty public constructor
    }
    private RecyclerView cartItemsRecyclerView;
    private Button cartcontinuebtn;
    private Dialog loadingDialog;
    public static CartAdapter cartAdapter;
    private TextView totalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart, container, false);

        ////loading Dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        ////loading Dialog

        cartcontinuebtn=view.findViewById(R.id.cartcontinuebtn);
        cartItemsRecyclerView=view.findViewById(R.id.cart_itm_recyclerview);
        totalAmount = view.findViewById(R.id.total_cart_amount);


        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);



        cartAdapter = new CartAdapter(DbQueries.cartItemModelList,totalAmount,true);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        cartcontinuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();  // change to uncomment
                DbQueries.loadAddresses(getContext(),loadingDialog);  // change to uncomment
                DeliveryActivity.cartItemModelList = new ArrayList<>();
                DeliveryActivity.fromCart =true;

                for (int x = 0; x<DbQueries.cartItemModelList.size();x++){
                    CartItemModel cartItemModel = DbQueries.cartItemModelList.get(x);
                    if(cartItemModel.isInstock()){
                        DeliveryActivity.cartItemModelList.add(cartItemModel);
                    }
                }
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                loadingDialog.show();

                if(DbQueries.addressesModelList.size()==0){
                    DbQueries.loadAddresses(getContext(),loadingDialog);
                }else{
                    loadingDialog.dismiss();
                    Intent deliveryIntet = new Intent(getContext(),DeliveryActivity.class);
                    startActivity(deliveryIntet);
                }
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        cartAdapter.notifyDataSetChanged();
//        if(DbQueries.cartItemModelList.size()==0) {
//            DbQueries.cartList.clear();
//           //change DbQueries.loadCartList(getContext(), loadingDialog, true,new TextView(getContext()),totalAmount);
//        }else {
//            if (DbQueries.cartItemModelList.get(DbQueries.cartItemModelList.size()-1).getType()==CartItemModel.TOTAL_AMOUNT){
//                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
//                parent.setVisibility(View.VISIBLE);
//            }
//            loadingDialog.dismiss();
//        }

//        if (DbQueries.rewardModelList.size()==0){
//            loadingDialog.show();
//            DbQueries.loadRewards(getContext(),loadingDialog,false);
//        }
//        if(DbQueries.cartItemModelList.size() == 0){
//            DbQueries.cartList.clear();
//            DbQueries.loadCartList(getContext(),loadingDialog,true,new TextView(getContext()),totalAmount);
//        }else{
//            if (DbQueries.cartItemModelList.get(DbQueries.cartItemModelList.size()-1).getType() == CartItemModel.TOTAL_AMOUNT){
//               // LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
//               // parent.setVisibility(View.VISIBLE);
//            }
//            loadingDialog.dismiss();
//        }
    }
}

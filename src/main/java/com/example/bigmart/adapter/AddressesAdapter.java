package com.example.bigmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigmart.DbQueries;
import com.example.bigmart.R;
import com.example.bigmart.modal.AddressesModel;

import java.util.List;

import static com.example.bigmart.AccountFragment.MANAGE_ADDRESS;
import static com.example.bigmart.DeliveryActivity.SELECT_ADDRESS;
import static com.example.bigmart.MyaddressesActivity.refreshItem;


public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int preSelectedPos;

    public AddressesAdapter(List<AddressesModel> addressesModelList,int MODE) {
        this.addressesModelList = addressesModelList;
        this.MODE=MODE;
        preSelectedPos= DbQueries.selectedAddress;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_itemlayout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        String name=addressesModelList.get(position).getFunnName();
        String mobileno=addressesModelList.get(position).getMobileno();
        String address=addressesModelList.get(position).getAddress();
        String pincode=addressesModelList.get(position).getPinCode();
        Boolean selected = addressesModelList.get(position).getSelected();


        holder.setData(name,mobileno,address,pincode,selected,position);


    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView fullName, pincode, address;
        private ImageView icon;
        private LinearLayout optionsContainer;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.address_fulname2);
            pincode = itemView.findViewById(R.id.address_pincode2);
            address = itemView.findViewById(R.id.fulladdress2);
            icon = itemView.findViewById(R.id.icon_view);
            optionsContainer=itemView.findViewById(R.id.options_container);
        }

        private void setData(String username,String mobileno, String useraddress, String userpincode, final Boolean selected, final int posititon) {
            fullName.setText(username+" - "+mobileno);
            pincode.setText(userpincode);
            address.setText(useraddress);

            if (MODE == SELECT_ADDRESS) {
                icon.setImageResource(R.drawable.ic_check);
                if (selected) {
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPos = posititon;
                } else {
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (preSelectedPos != posititon) {
                            addressesModelList.get(posititon).setSelected(true);
                            addressesModelList.get(preSelectedPos).setSelected(false);
                            refreshItem(preSelectedPos, posititon);
                            preSelectedPos = posititon;
                            DbQueries.selectedAddress=posititon;
                        }

                    }
                });

            }else if(MODE == MANAGE_ADDRESS){
                optionsContainer.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.ic_more_vert);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionsContainer.setVisibility(View.VISIBLE);
                        icon.setVisibility(View.GONE);  ///change to uncomment
                        refreshItem(preSelectedPos,posititon);  ///change 2 parameter to preselectexPos to position
                        preSelectedPos=posititon;
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        icon.setVisibility(View.VISIBLE);  //change to uncomment
                        refreshItem(preSelectedPos,posititon);   // change 2 parameter to preselectedPos to position
                        preSelectedPos=-1;
                    }
                });

            }

        }



        }

    }


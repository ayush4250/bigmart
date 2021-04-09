package com.example.bigmart;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View; 
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }
    private Button viewallAddress;
    public static final int MANAGE_ADDRESS=1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account, container, false);
        viewallAddress=view.findViewById(R.id.viewalladdressbutton);

        viewallAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myaddressIntent = new Intent(getContext(),MyaddressesActivity.class);
                myaddressIntent.putExtra("MODE",MANAGE_ADDRESS);
                startActivity(myaddressIntent);

            }
        });
        return view;
    }

}


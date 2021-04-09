package com.example.bigmart;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bigmart.adapter.ProductSpecificationAdpter;



/**
 * A simple {@link Fragment} subclass.
 */
public class ProductSpecificationFragment extends Fragment {


    public ProductSpecificationFragment() {
        // Required empty public constructor
    }

    private RecyclerView productSpecificationRecyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_specification, container, false);
        productSpecificationRecyclerView = view.findViewById(R.id.product_specefication_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        productSpecificationRecyclerView.setLayoutManager(linearLayoutManager);


        ProductSpecificationAdpter productSpecificationAdpter = new ProductSpecificationAdpter(ProductDetailsActivity.productSpecificationModalList);
        productSpecificationRecyclerView.setAdapter(productSpecificationAdpter);
        productSpecificationAdpter.notifyDataSetChanged();

        return view;
    }

}

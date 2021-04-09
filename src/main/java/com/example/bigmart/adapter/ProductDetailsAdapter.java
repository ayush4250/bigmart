package com.example.bigmart.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bigmart.ProductDescriptionFragment;
import com.example.bigmart.ProductSpecificationFragment;
import com.example.bigmart.modal.ProductSpecificationModal;

import java.util.List;

public class ProductDetailsAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    private String productDescription;
    private String productOtherDetails;
    private List<ProductSpecificationModal> productSpecificationModalList;

    public ProductDetailsAdapter(FragmentManager fm, int totalTabs, String productDescription, String productOtherDetails, List<ProductSpecificationModal> productSpecificationModalList) {
        super(fm);
        this.totalTabs = totalTabs;
        this.productDescription = productDescription;
        this.productOtherDetails = productOtherDetails;
        this.productSpecificationModalList = productSpecificationModalList;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ProductDescriptionFragment productDescriptionFragment1 = new ProductDescriptionFragment();
                productDescriptionFragment1.body = productDescription;
                return productDescriptionFragment1;

            case 1:
                ProductSpecificationFragment productSpecificationFragment = new ProductSpecificationFragment();
               // productSpecificationFragment.productSpecificationModalList=productSpecificationModalList;
                return productSpecificationFragment;

            case 2:
                ProductDescriptionFragment productDescriptionFragment2 = new ProductDescriptionFragment();
                productDescriptionFragment2.body = productOtherDetails;
                return productDescriptionFragment2;
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}

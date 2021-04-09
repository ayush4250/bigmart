package com.example.bigmart;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.bigmart.adapter.CategoryAdapter;
import com.example.bigmart.adapter.HomePageAdapter;
import com.example.bigmart.modal.CategoryModel;
import com.example.bigmart.modal.FavouriteModel;
import com.example.bigmart.modal.HomePageModel;
import com.example.bigmart.modal.HorizontalProductModel;
import com.example.bigmart.modal.SliderModel;


import java.util.ArrayList;
import java.util.List;

import static com.example.bigmart.DbQueries.categoryModelList;
import static com.example.bigmart.DbQueries.lists;
import static com.example.bigmart.DbQueries.loadCategories;
import static com.example.bigmart.DbQueries.loadFragmentData;

public class HomeFragment extends Fragment {

    private RecyclerView categoryRecyclerview;
    private RecyclerView homepageRecyclerview;

    private HomePageAdapter adapter;
    private CategoryAdapter categoryAdapter;

    private ImageView nointernetconnection;
    private Button Retrybtn;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    private List<CategoryModel> categoryModelFakeList=new ArrayList<>();
    private List<HomePageModel> homePageModelFakeList=new ArrayList<>();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toast.makeText(getContext(), "Fragment Started", Toast.LENGTH_SHORT).show();

        nointernetconnection=view.findViewById(R.id.no_internet_connection);
        Retrybtn=view.findViewById(R.id.retry_btn);
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);
        homepageRecyclerview = view.findViewById(R.id.homepageRecyclerView);
        categoryRecyclerview=view.findViewById(R.id.categoryRecyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerview.setLayoutManager(layoutManager);

        //category fake list
        categoryModelFakeList.add(new CategoryModel("null","xxxxx"));
        categoryModelFakeList.add(new CategoryModel("null","xxxxx"));
        categoryModelFakeList.add(new CategoryModel("null","xxxxx"));
        categoryModelFakeList.add(new CategoryModel("null","xxxxx"));
        categoryModelFakeList.add(new CategoryModel("null","xxxxx"));
        categoryModelFakeList.add(new CategoryModel("null","xxxxx"));
        categoryModelFakeList.add(new CategoryModel("null","xxxxx"));
        categoryModelFakeList.add(new CategoryModel("null","xxxxx"));
        categoryModelFakeList.add(new CategoryModel("null","xxxxx"));
        //category fake list




        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homepageRecyclerview.setLayoutManager(testingLayoutManager);

        //home page fake list
        List<SliderModel> sliderModelfakeList=new ArrayList<>();
        sliderModelfakeList.add(new SliderModel("null","#000000"));
        sliderModelfakeList.add(new SliderModel("null","#000000"));
        sliderModelfakeList.add(new SliderModel("null","#000000"));
        sliderModelfakeList.add(new SliderModel("null","#000000"));
        sliderModelfakeList.add(new SliderModel("null","#000000"));


        List<HorizontalProductModel> horizontalProductModefakelList=new ArrayList<>();
        horizontalProductModefakelList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModefakelList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModefakelList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModefakelList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModefakelList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModefakelList.add(new HorizontalProductModel("","","","",""));
        horizontalProductModefakelList.add(new HorizontalProductModel("","","","",""));


        homePageModelFakeList.add(new HomePageModel(0,sliderModelfakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#dfdfdf",horizontalProductModefakelList,new ArrayList<FavouriteModel>()));
        homePageModelFakeList.add(new HomePageModel(2,"","#ffffff",horizontalProductModefakelList,new ArrayList<FavouriteModel>()));

        //home page fake list
        categoryAdapter = new CategoryAdapter(categoryModelFakeList);
        categoryRecyclerview.setAdapter(categoryAdapter);

        adapter = new HomePageAdapter(homePageModelFakeList);
        homepageRecyclerview.setAdapter(adapter);



        connectivityManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo=connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()==true){
            // MainActivity.drawer.setDrawerLockMode(1);
            nointernetconnection.setVisibility(View.GONE);
            Retrybtn.setVisibility(View.GONE);
            categoryRecyclerview.setVisibility(View.VISIBLE);
            homepageRecyclerview.setVisibility(View.VISIBLE);

            categoryAdapter=new CategoryAdapter(categoryModelFakeList);
            adapter=new HomePageAdapter(homePageModelFakeList);


            if(categoryModelList.size()==0){
                loadCategories(categoryRecyclerview,getContext());
            }else{
                categoryAdapter.notifyDataSetChanged();
            }


            if(lists.size()==0){
                lists.add(new ArrayList<HomePageModel>());
                loadFragmentData(homepageRecyclerview,getContext(),0);
            }else{
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }


        }else{
            categoryRecyclerview.setVisibility(View.GONE);
            homepageRecyclerview.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.ic_error_black).into(nointernetconnection);
            nointernetconnection.setVisibility(View.VISIBLE);
            Retrybtn.setVisibility(View.VISIBLE);
        }


        /////refresh layput
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

               // swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.blue),getContext().getResources().getColor(R.color.blue),getContext().getResources().getColor(R.color.blue));
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();

            }
        });

        ////refresh

        Retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });


        return view;
    }

    private void reloadPage(){

        networkInfo=connectivityManager.getActiveNetworkInfo();

        DbQueries.clearData();

        if(networkInfo!=null && networkInfo.isConnected()==true){
            // MainActivity.drawer.setDrawerLockMode(0);
            nointernetconnection.setVisibility(View.GONE);
            Retrybtn.setVisibility(View.GONE);
            categoryRecyclerview.setVisibility(View.VISIBLE);
            homepageRecyclerview.setVisibility(View.VISIBLE);

           categoryAdapter = new CategoryAdapter(categoryModelFakeList);
            adapter = new HomePageAdapter(homePageModelFakeList);

           categoryRecyclerview.setAdapter(categoryAdapter);
            homepageRecyclerview.setAdapter(adapter);



            loadCategories(categoryRecyclerview,getContext());
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homepageRecyclerview,getContext(),0);


        }else{
            //MainActivity.drawer.setDrawerLockMode(1);
            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            categoryRecyclerview.setVisibility(View.GONE);
            homepageRecyclerview.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.ic_error_black).into(nointernetconnection);
            nointernetconnection.setVisibility(View.VISIBLE);
            Retrybtn.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        reloadPage();
//    }
}





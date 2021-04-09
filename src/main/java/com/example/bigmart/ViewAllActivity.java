package com.example.bigmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.bigmart.adapter.FavouriteAdapter;
import com.example.bigmart.adapter.GridProductLayoutAdapter;
import com.example.bigmart.modal.FavouriteModel;
import com.example.bigmart.modal.HorizontalProductModel;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;
    public static List<FavouriteModel> wishlistmodellist;
    public static List<HorizontalProductModel> horizontalProductModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recyclerview);
        gridView=findViewById(R.id.gridview);

        int layout_code=getIntent().getIntExtra("layout_code",-1);

        if(layout_code==0) {


            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            List<FavouriteModel> favouriteModels = new ArrayList<>();

            FavouriteAdapter favouriteAdapter = new FavouriteAdapter(favouriteModels, false);
            recyclerView.setAdapter(favouriteAdapter);
            favouriteAdapter.notifyDataSetChanged();
        }else if(layout_code==1) {

            gridView.setVisibility(View.VISIBLE);


            GridProductLayoutAdapter gridlayoutAdapter = new GridProductLayoutAdapter(horizontalProductModels);
            gridView.setAdapter(gridlayoutAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}






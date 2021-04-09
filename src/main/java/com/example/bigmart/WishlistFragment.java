package com.example.bigmart;



import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bigmart.adapter.FavouriteAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class WishlistFragment extends Fragment {


    public WishlistFragment() {
        // Required empty public constructor
    }

    private RecyclerView favouriterecycleview;
    private Dialog loadingDialog;
    public static FavouriteAdapter favouriteAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wishlist, container, false);


        ////loading Dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        ////loading Dialog


        favouriterecycleview=view.findViewById(R.id.mywishlistrecycleview);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        favouriterecycleview.setLayoutManager(linearLayoutManager);






        if(DbQueries.favouriteModelList.size()==0) {
            DbQueries.wishlist.clear();
            DbQueries.loadWishlist(getContext(), loadingDialog, true);
        }else {
            loadingDialog.dismiss();
        }

        favouriteAdapter=new FavouriteAdapter(DbQueries.favouriteModelList,true);
        favouriterecycleview.setAdapter(favouriteAdapter);
        favouriteAdapter.notifyDataSetChanged();

        return view;
    }

}


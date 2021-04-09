package com.example.bigmart;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bigmart.adapter.MyRewardAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class RewardFragment extends Fragment {


    public RewardFragment() {
        // Required empty public constructor
    }
    private RecyclerView rewardrecyclerview;
    private Dialog loadingDialog;
    public static MyRewardAdapter myRewardAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reward, container, false);

        ////loading Dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        ////loading Dialog
        rewardrecyclerview=view.findViewById(R.id.myrewardsrecyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardrecyclerview.setLayoutManager(layoutManager);

//        if(DbQueries.rewardModelList.size()==0){
//            DbQueries.loadRewards(getContext(),loadingDialog,true);
//        }else{
//            loadingDialog.dismiss();
//        }

        myRewardAdapter=new MyRewardAdapter(DbQueries.rewardModelList,false);
        rewardrecyclerview.setAdapter(myRewardAdapter);
        myRewardAdapter.notifyDataSetChanged();




        return view;
    }

}

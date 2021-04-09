package com.example.bigmart.adapter;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bigmart.ProductDetailsActivity;
import com.example.bigmart.R;
import com.example.bigmart.ViewAllActivity;
import com.example.bigmart.modal.FavouriteModel;
import com.example.bigmart.modal.HomePageModel;
import com.example.bigmart.modal.HorizontalProductModel;
import com.example.bigmart.modal.SliderModel;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {
    private int lastPosition=-1;

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;


    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool=new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()){
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;
            case 2:
                return HomePageModel.GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case HomePageModel.BANNER_SLIDER:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout,parent,false);
                return new BannerSliderViewholder(view);
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizonatalproductview = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout,parent,false);
                return new HorizontalProductViewholder(horizonatalproductview);
            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridproductview=LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_view,parent,false);
                return new GridProductViewholder(gridproductview);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()){
            case HomePageModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewholder)holder).setBannersliderviewpager(sliderModelList);
                break;
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                String color=homePageModelList.get(position).getBackground();
                String horizotallayouttitle = homePageModelList.get(position).getTitle();
                List<HorizontalProductModel> horizontalProductModels = homePageModelList.get(position).getHorizontalProductModels();
                List<FavouriteModel> viewallProductlist = homePageModelList.get(position).getFavouriteModelList();
                ((HorizontalProductViewholder)holder).setHorizontalProductModelsLayout(horizontalProductModels,horizotallayouttitle,color,viewallProductlist);
                break;

            case HomePageModel.GRID_PRODUCT_VIEW:
                String colors=homePageModelList.get(position).getBackground();
                String gridlayouttitle=homePageModelList.get(position).getTitle();
                List<HorizontalProductModel> horizontalProductModelList=homePageModelList.get(position).getHorizontalProductModels();
                ((GridProductViewholder)holder).setGridProductLayout(horizontalProductModelList,gridlayouttitle,colors);


            default:
                return;
        }
       /* if(lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.);
            holder.itemView.setAnimation(animation);
            lastPosition=position;
        }*/
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class BannerSliderViewholder extends RecyclerView.ViewHolder {

        private ViewPager bannersliderviewpager;
        private int currentPage=2;
        private Timer timer;
        final private long delay_time= 3000;
        final private long period_time=3000;

        public BannerSliderViewholder(@NonNull View itemView) {
            super(itemView);
            bannersliderviewpager =itemView.findViewById(R.id.banner_slider_viewpager);
            ////
        }

        private void setBannersliderviewpager(final List<SliderModel> sliderModelList){

            SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
            bannersliderviewpager.setAdapter(sliderAdapter);
            bannersliderviewpager.setClipToPadding(false);
            bannersliderviewpager.setPageMargin(20);

            bannersliderviewpager.setCurrentItem(currentPage);

            ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }


                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state==ViewPager.SCROLL_STATE_IDLE){
                        pageLopper(sliderModelList);
                    }
                }
            };
            bannersliderviewpager.addOnPageChangeListener(onPageChangeListener);

            startBannerSlideshow(sliderModelList);

            bannersliderviewpager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pageLopper(sliderModelList);
                    stopBannerslideshow();
                    if(event.getAction()== MotionEvent.ACTION_UP){
                        startBannerSlideshow(sliderModelList);
                    }
                    return false;
                }
            });
        }
        private void pageLopper(List<SliderModel> sliderModelList) {
            if (currentPage == sliderModelList.size() - 2) {
                currentPage = 2;
                bannersliderviewpager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1) {
                currentPage = sliderModelList.size() - 3;
                bannersliderviewpager.setCurrentItem(currentPage, false);
            }



        }
        private void startBannerSlideshow(final List<SliderModel> sliderModelList){
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if(currentPage>=sliderModelList.size()){
                        currentPage=1;
                    }
                    bannersliderviewpager.setCurrentItem(currentPage++,true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            },delay_time,period_time);
        }
        private void stopBannerslideshow(){
            timer.cancel();
        }
    }

    public class HorizontalProductViewholder extends RecyclerView.ViewHolder{
        private ConstraintLayout container;

        private RecyclerView horizontalrecycleview;
        private TextView horizontallayouttitle;
        private Button horizontalviewallbutton;

        public HorizontalProductViewholder(@NonNull View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.containers);
            horizontallayouttitle=itemView.findViewById(R.id.horizontal_title);
            horizontalrecycleview=itemView.findViewById(R.id.horizontal_recycler);
            horizontalviewallbutton=itemView.findViewById(R.id.horizontal_viewall_btn);
            horizontalrecycleview.setRecycledViewPool(recycledViewPool);


        }
        private void setHorizontalProductModelsLayout(List<HorizontalProductModel> horizontalProductModels, final String title, String color, final List<FavouriteModel> viewallproductlist){
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontallayouttitle.setText(title);
            if(horizontalProductModels.size()>3){
                horizontalviewallbutton.setVisibility(View.VISIBLE);
                horizontalviewallbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.wishlistmodellist = viewallproductlist;
                        Intent viewallintent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewallintent.putExtra("layout_code",0);
                        viewallintent.putExtra("title",title);
                        itemView.getContext().startActivity(viewallintent);
                    }
                });
            }else{
                horizontalviewallbutton.setVisibility(View.INVISIBLE);
            }
            HorizontalProductAdapter horizontalProductAdapter=new HorizontalProductAdapter(horizontalProductModels);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalrecycleview.setLayoutManager(linearLayoutManager);

            horizontalrecycleview.setAdapter(horizontalProductAdapter);
            horizontalProductAdapter.notifyDataSetChanged();

        }

    }

    public class GridProductViewholder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private TextView gridLayoutTitle;
        private Button gridButton;
        private GridLayout gridLayout;

        public GridProductViewholder(@NonNull View itemView) {
            super(itemView);
            gridLayoutTitle=itemView.findViewById(R.id.gridtext);
            gridButton=itemView.findViewById(R.id.gridbtn);
            gridLayout=itemView.findViewById(R.id.GridLayout);
            container=itemView.findViewById(R.id.containers);

        }
        private void setGridProductLayout(final List<HorizontalProductModel> horizontalProductModelList, final String title, String color){
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);

            for(int x=0;x<4;x++) {
                ImageView productImage = gridLayout.getChildAt(x).findViewById(R.id.h_s_image);
                TextView productTitle = gridLayout.getChildAt(x).findViewById(R.id.h_s_title);
                TextView description = gridLayout.getChildAt(x).findViewById(R.id.h_h_desc);
                TextView price = gridLayout.getChildAt(x).findViewById(R.id.h_s_price);

                Glide.with(itemView.getContext()).load(horizontalProductModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.ic_delete_black)).into(productImage);
                // productImage.setImageResource(Integer.parseInt((horizontalProductModelList.get(x).getProductImage())));
                productTitle.setText(horizontalProductModelList.get(x).getProductTitle());
                description.setText(horizontalProductModelList.get(x).getProductDescription());
                price.setText("Rs."+horizontalProductModelList.get(x).getProductPrice());
                gridLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#ffffff"));

                if (!title.equals("")) {
                    final int finalX = x;
                    gridLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                      @Override
                    public void onClick(View v) {
                            Intent productdetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                            productdetailsIntent.putExtra("PRODUCT_ID",horizontalProductModelList.get(finalX).getProductId());
                            itemView.getContext().startActivity(productdetailsIntent);
                       }
                    });
               }
          }

            gridButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewAllActivity.horizontalProductModels=horizontalProductModelList;
                    Intent viewallintent = new Intent(itemView.getContext(), ViewAllActivity.class);
                    viewallintent.putExtra("layout_code",1);
                    viewallintent.putExtra("title",title);
                    itemView.getContext().startActivity(viewallintent);

                }
            });


        }
    }

}
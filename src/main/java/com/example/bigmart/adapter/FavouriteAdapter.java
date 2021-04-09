package com.example.bigmart.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bigmart.DbQueries;
import com.example.bigmart.ProductDetailsActivity;
import com.example.bigmart.R;
import com.example.bigmart.modal.FavouriteModel;


import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.viewhoder> {

    private boolean fromSearch;
    private List<FavouriteModel> favouriteModelList;
    private Boolean wishlist;

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public FavouriteAdapter(List<FavouriteModel> favouriteModelList, Boolean wishlist) {
        this.favouriteModelList = favouriteModelList;
        this.wishlist=wishlist;
    }

    public List<FavouriteModel> getFavouriteModelList() {
        return favouriteModelList;
    }

    public void setFavouriteModelList(List<FavouriteModel> favouriteModelList) {
        this.favouriteModelList = favouriteModelList;
    }

    @NonNull
    @Override
    public viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item,parent,false);
        return new viewhoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewhoder holder, int position) {
        String productid=favouriteModelList.get(position).getProductid();
        String resource=favouriteModelList.get(position).getResturantImage();
        String title=favouriteModelList.get(position).getResturantName();
        String averagerating=favouriteModelList.get(position).getAveragerating();
        long totalratingNo=favouriteModelList.get(position).getTotalrating();
        boolean inStock = favouriteModelList.get(position).isInStock();


        holder.setData(productid,resource,title,averagerating,totalratingNo,position,inStock);

    }

    @Override
    public int getItemCount() {
        return favouriteModelList.size();
    }

    public class viewhoder extends RecyclerView.ViewHolder {
        private ImageView resturnatImage;
        private TextView resturanttitle,totalRating,averagerating;
        private ImageView deletebutton;

        public viewhoder(@NonNull View itemView) {
            super(itemView);

            resturnatImage=itemView.findViewById(R.id.productimage5);
            resturanttitle=itemView.findViewById(R.id.producttitle);
            averagerating=itemView.findViewById(R.id.tv_product_rating);
            totalRating=itemView.findViewById(R.id.totalrating5);
            deletebutton=itemView.findViewById(R.id.deletebutton);

        }

        private void setData(final String productid, String resource, String title, String averagerate, long totalratingNo, final int index,boolean inStock){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_delete_black)).into(resturnatImage);
            resturanttitle.setText(title);
            averagerating.setText(averagerate);


            if (inStock){
                totalRating.setText(totalratingNo+"(rating)");
            }


            if(wishlist){
                deletebutton.setVisibility(View.VISIBLE);
            }else{
                deletebutton.setVisibility(View.GONE);
            }
            deletebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ProductDetailsActivity.runningwishlist_query)
                    ProductDetailsActivity.runningwishlist_query=true;
                    DbQueries.removeFromWishlist(index,itemView.getContext());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fromSearch) {
                        ProductDetailsActivity.fromSearch = true;
                    }
                        Intent productdetailsintent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                        productdetailsintent.putExtra("PRODUCT_ID", productid);
                        itemView.getContext().startActivity(productdetailsintent);
                }
            });

        }
    }
}

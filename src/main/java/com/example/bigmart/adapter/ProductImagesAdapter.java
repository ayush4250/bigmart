package com.example.bigmart.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bigmart.R;

import java.util.List;

public class ProductImagesAdapter extends PagerAdapter {

    private List<String> ProductImages;

    public ProductImagesAdapter(List<String> productImages) {
        this.ProductImages = productImages;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView productImage = new ImageView(container.getContext());
        Glide.with(container.getContext()).load(ProductImages.get(position)).apply(new RequestOptions().placeholder(R.drawable.ic_close_black)).into(productImage);
        container.addView(productImage,0);
        return productImage;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);

    }

    @Override
    public int getCount() {
        return ProductImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}

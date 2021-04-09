package com.example.bigmart.adapter;

import android.app.Dialog;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bigmart.DbQueries;
import com.example.bigmart.DeliveryActivity;
import com.example.bigmart.MainActivity;
import com.example.bigmart.ProductDetailsActivity;
import com.example.bigmart.R;
import com.example.bigmart.modal.CartItemModel;
import com.example.bigmart.modal.RewardModel;

import java.util.List;

import static com.example.bigmart.RewardFragment.myRewardAdapter;


public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModels;
    private int lastposition=-1;
    private TextView cartTotalAmount;
    private boolean showDeletebtn;

    public CartAdapter(List<CartItemModel> cartItemModels,TextView cartTotalAmount,boolean showDeletebtn) {
        this.cartItemModels = cartItemModels;
        this.showDeletebtn=showDeletebtn;
        this.cartTotalAmount = cartTotalAmount;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModels.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartItemViewHolder(view);
            case CartItemModel.TOTAL_AMOUNT:
                View cartview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new CartTotalAmountViewHolder(cartview);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartItemModels.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productId=cartItemModels.get(position).getProductId();
                String resource = cartItemModels.get(position).getProductimage();
                String title = cartItemModels.get(position).getProducttitle();
                Long freecoupens = cartItemModels.get(position).getFreecoupen();
                String productprice = cartItemModels.get(position).getProductprice();
                String cuttedprices=cartItemModels.get(position).getCutprice();
                Long offersApplied = cartItemModels.get(position).getOffersApplied();
                boolean inStock=cartItemModels.get(position).isInstock();
                Long productQuantity = cartItemModels.get(position).getProductQuantity();
                Long maxQunatity=cartItemModels.get(position).getMaxQunatity();


                ((CartItemViewHolder) holder).setItemDetails(productId,resource, title, freecoupens,productprice,cuttedprices,offersApplied,position,inStock,productQuantity,maxQunatity);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalitems=0;
                int totalItemsPrece=0;
                String deliveryprice;
                int totalamount;
                int savedAmount=0;

                for (int x=0;x<cartItemModels.size();x++) {
                    if (cartItemModels.get(x).getType() == CartItemModel.CART_ITEM && cartItemModels.get(x).isInstock()) {
                        totalitems++;
                        totalItemsPrece=totalItemsPrece+Integer.parseInt(cartItemModels.get(x).getProductprice());

                    }
                }
                if(totalItemsPrece>50000){
                    deliveryprice="FREE";
                    totalamount=totalItemsPrece;
                }else{
                    deliveryprice="40";
                    totalamount=totalItemsPrece+40;
                }

                cartItemModels.get(position).setTotalitems(totalitems);
                cartItemModels.get(position).setTotalItemPrice(totalItemsPrece);
                cartItemModels.get(position).setDeliveryPrice(deliveryprice);
                cartItemModels.get(position).setTotalAmount(totalamount);
                cartItemModels.get(position).setSavedAmount(savedAmount);
                ((CartTotalAmountViewHolder) holder).setTotalamount(totalitems, totalItemsPrece, deliveryprice, totalamount, savedAmount);
                break;  // change to write break;
            default:
                return;
        }

        if(lastposition<position){
            Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.nav_default_enter_anim);
            holder.itemView.setAnimation(animation);
            lastposition=position;
        }

    }


    @Override
    public int getItemCount() {
        return cartItemModels.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView productimage,freecoupenicon;
        private TextView producttitle,freecoupen,cuttedprice,offersapplied,coupensapplied;
        private TextView productprice, productquantity;
        private LinearLayout deletebutton;
        private LinearLayout coupenReedemptionlayout;
        private Button reedemBtn;

        //coupen dialog
        private RecyclerView coupenrecycleview;
        private LinearLayout selectedCoupen;
        private TextView coupenTitle;
        private TextView coupenbody;
        private TextView coupenexpiry;
        private TextView discountedprice;
        private TextView originalprice;
        private Button removeCoupenBtn;
        private Button applyBtn;
        private LinearLayout applylinearBtn;
        private String productPricevalue;
        private TextView coupenRedemptionBody;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productimage = itemView.findViewById(R.id.product_image);
            freecoupenicon=itemView.findViewById(R.id.coupenimage);
            producttitle = itemView.findViewById(R.id.resturant_name);
            freecoupen=itemView.findViewById(R.id.tv_freecoupen);
            cuttedprice=itemView.findViewById(R.id.cut_price);
            offersapplied=itemView.findViewById(R.id.offer_applied);
            coupensapplied=itemView.findViewById(R.id.coupen_applied);
            productprice = itemView.findViewById(R.id.product_price);
            productquantity = itemView.findViewById(R.id.product_qty1);
            deletebutton=itemView.findViewById(R.id.remove_item_btn);
            coupenReedemptionlayout=itemView.findViewById(R.id.coupen_redemption);
            reedemBtn=itemView.findViewById(R.id.coupen_redemption_button);
            applylinearBtn = coupenReedemptionlayout.findViewById(R.id.applyorRemovebtn);
            coupenRedemptionBody = itemView.findViewById(R.id.tv_coupen_reedem);

        }

        private void setItemDetails(String productId, String resource, String title, Long freecoupenno, String pricetext, String cuttedprices, Long offersAppliedNo, final int position, boolean instock, Long quantity, final Long maxQuantity) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_delete_black)).into(productimage);
            producttitle.setText(title);
            if (freecoupenno > 0) {
                freecoupenicon.setVisibility(View.VISIBLE);
                freecoupen.setVisibility(View.VISIBLE);
                if (freecoupenno == 1) {
                    freecoupen.setText("free " + freecoupenno + " Coupen");
                } else {
                    freecoupen.setText("free " + freecoupenno + " Coupens");
                }
            } else {
                freecoupenicon.setVisibility(View.INVISIBLE);
                freecoupen.setVisibility(View.INVISIBLE);

            }
            if(instock) {
                cuttedprice.setText("Rs."+cuttedprices);
                productprice.setTextColor(Color.parseColor("#000000"));
                productprice.setText(pricetext);
                coupenReedemptionlayout.setVisibility(View.VISIBLE);

            }else{
                productprice.setText("Out of Stock");
                productprice.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                cuttedprice.setText("");
                coupenReedemptionlayout.setVisibility(View.GONE);

            }

            if (offersAppliedNo > 0){
                offersapplied.setVisibility(View.VISIBLE);
                String offerDiscountedAmount = String.valueOf(Long.valueOf(cuttedprices)-Long.valueOf(pricetext));  //productPriceText
                offersapplied.setText("-"+offerDiscountedAmount);
            }else{
                offersapplied.setVisibility(View.INVISIBLE);
            }




            productquantity.setText("Qty:" + quantity);
            productquantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog quantityDialog=new Dialog(itemView.getContext());
                    quantityDialog.setContentView(R.layout.quantity_dialog);
                    quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    quantityDialog.setCancelable(false);

                    final EditText quantitynumber=quantityDialog.findViewById(R.id.quantity_number);
                    Button cancelBtn=quantityDialog.findViewById(R.id.cancel_btn);
                    Button okbtn=quantityDialog.findViewById(R.id.ok_btn);
                    quantitynumber.setHint("Max "+String.valueOf(maxQuantity));

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quantityDialog.dismiss();
                        }
                    });

                    okbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(quantitynumber.getText()))
                            if (Long.parseLong(quantitynumber.getText().toString()) <= maxQuantity && Long.parseLong(quantitynumber.getText().toString()) != 0) {
                                if (itemView.getContext() instanceof MainActivity){
                                    DbQueries.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantitynumber.getText().toString()));
                                }else {
                                    if (DeliveryActivity.fromCart) {
                                        DbQueries.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantitynumber.getText().toString()));
                                    }
                                }
                                    productquantity.setText("Qty:" + quantitynumber.getText());
                            }else{
                                Toast.makeText(itemView.getContext(), "Invalid Quantity", Toast.LENGTH_SHORT).show();
                            }
                            quantityDialog.dismiss();
                        }

                    });

                    quantityDialog.show();
                }
            });
            if(showDeletebtn){
                deletebutton.setVisibility(View.VISIBLE);
            }else{
                deletebutton.setVisibility(View.GONE);
            }

            final Dialog checkpricedialog=new Dialog(itemView.getContext());
            checkpricedialog.setContentView(R.layout.coupen_reedem_dialog);
            checkpricedialog.setCancelable(true);
            checkpricedialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            ImageView toglerecyclerview = checkpricedialog.findViewById(R.id.toggle_recycler);
            coupenrecycleview=checkpricedialog.findViewById(R.id.coupens_recycler);
            selectedCoupen=checkpricedialog.findViewById(R.id.selected_coupen);
            coupenTitle=checkpricedialog.findViewById(R.id.coupentitle);
            coupenexpiry=checkpricedialog.findViewById(R.id.coupenvalidity);
            coupenbody=checkpricedialog.findViewById(R.id.coupenbody);
            removeCoupenBtn = checkpricedialog.findViewById(R.id.removebutton);
            applyBtn = checkpricedialog.findViewById(R.id.applybtn);

          //  applylinearBtn.setVisibility(View.VISIBLE);
            originalprice=checkpricedialog.findViewById(R.id.originalprice);
            discountedprice=checkpricedialog.findViewById(R.id.redemedprice);

            originalprice.setText(productprice.getText());
           // productPricevalue = productPriceText;
            myRewardAdapter=new MyRewardAdapter(position,DbQueries.rewardModelList,true,coupenrecycleview,selectedCoupen,productPricevalue,coupenTitle,coupenexpiry,coupenbody,discountedprice);
            coupenrecycleview.setAdapter(myRewardAdapter);
            myRewardAdapter.notifyDataSetChanged();

            applyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(DbQueries.cartItemModelList.get(position).getSelectedCoupenId())) {
                        for (RewardModel rewardModel : DbQueries.rewardModelList) {
                            if (rewardModel.getCoupenId().equals(DbQueries.cartItemModelList.get(position).getSelectedCoupenId())) {
                                rewardModel.setAlreadyUsed(true);

                                coupenReedemptionlayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                                coupenRedemptionBody.setText(rewardModel.getCoupenbody());
                                reedemBtn.setText("Coupen");
                            }
                        }
                    }
                    coupensapplied.setVisibility(View.VISIBLE);
                    String offerDiscountedAmount = String.valueOf(Long.valueOf(productPricevalue)-Long.valueOf(discountedprice.getText().toString().substring(2,discountedprice.getText().length()-2)));  //productPriceText

                    coupensapplied.setText("Coupen applied" +offerDiscountedAmount);
                    checkpricedialog.dismiss();
                }
            });

//            removeCoupenBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    for (RewardModel rewardModel: DbQueries.rewardModelList) {
//                        if (rewardModel.getCoupenId().equals(DbQueries.cartItemModelList.get(position).getSelectedCoupenId())){
//                            rewardModel.setAlreadyUsed(false);
//                        }
//                    }
//                    coupenTitle.setText("Coupen");
//                    coupenexpiry.setText("Validity");
//                    coupenbody.setText(itemView.getContext().getResources().getString(R.string.coupen_body));
//                    coupensapplied.setVisibility(View.INVISIBLE);
//                    coupenReedemptionlayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.red));
//                    coupenRedemptionBody.setText("Apply Your Coupen Here");
//                    reedemBtn.setText("Reedem");
//                    DbQueries.cartItemModelList.get(position).setSelectedCoupenId(null);
//                    checkpricedialog.dismiss();
//                }
//            });

            toglerecyclerview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showdialogRecyclerview();
                }
            });

            reedemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   checkpricedialog.show();
                }
            });

            deletebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ProductDetailsActivity.runningcart_query){
                        ProductDetailsActivity.runningcart_query=true;
                        DbQueries.removeFromCart(position,itemView.getContext(),cartTotalAmount);
                    }
                }
            });

        }

        private void showdialogRecyclerview(){
            if(coupenrecycleview.getVisibility()==View.GONE){
                coupenrecycleview.setVisibility(View.VISIBLE);
                selectedCoupen.setVisibility(View.GONE);
            }else{
                coupenrecycleview.setVisibility(View.GONE);
                selectedCoupen.setVisibility(View.VISIBLE);
            }
        }
    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView totalitems;
        private TextView totalitemprice;
        private TextView deliveryprice;
        private TextView totalamount;
        private TextView savedAmount;


        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            totalitems = itemView.findViewById(R.id.total_item);
            totalitemprice = itemView.findViewById(R.id.total_item_price);
            deliveryprice = itemView.findViewById(R.id.delivery_charge_price);
            totalamount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
        }

        private void setTotalamount(int totalitemtext, int totalitempricetext, String deliverypricetext, int totalamountt, int savedamounttext) {
            totalitems.setText("Price("+totalitemtext+"items)");
            totalitemprice.setText("Rs."+totalitempricetext+"/-");
            if(deliverypricetext.equals("FREE")){
                deliveryprice.setText(deliverypricetext);
            }else{
                deliveryprice.setText("RS."+deliverypricetext+"/-");
            }


            totalamount.setText("Rs."+totalamountt+"/-");
            cartTotalAmount.setText("Rs."+totalamountt);
            savedAmount.setText("You Saved Rs."+savedamounttext+ " On this order");
            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if(totalitempricetext==0){
                DbQueries.cartItemModelList.remove(DbQueries.cartItemModelList.size()-1);
                parent.setVisibility(View.GONE);
            }else{
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}

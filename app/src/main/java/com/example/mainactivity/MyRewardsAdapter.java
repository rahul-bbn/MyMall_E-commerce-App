package com.example.mainactivity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.ViewHolder> {

    private List<RewardModel> rewardModelList;
    private List<CartItemModel> cartItemModelList;
    private Boolean useMiniLayout = false;
    private RecyclerView coupensRecyclerView;
    private LinearLayout selectedCoupen;
    private String productOriginalPrice;
    private TextView selectedCoupenTitle;
    private TextView selectedCoupenExpiryDate;
    private TextView selectedCoupenBody;
    private TextView discountedPrice;
    private int position=-1;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody,TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
    }

    public MyRewardsAdapter(int position,List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody,TextView discountedPrice,List<CartItemModel> cartItemModelList) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
        this.position = position;
        this.cartItemModelList=cartItemModelList;
    }

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (useMiniLayout){
              view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);
        }else {
              view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String type=rewardModelList.get(position).getType();
        String upperLimit=rewardModelList.get(position).getUpperLimit();
        String lowerLimit=rewardModelList.get(position).getLowerLimit();
        String discORamt=rewardModelList.get(position).getDiscORamt();
        String body=rewardModelList.get(position).getCoupenBody();
        Date validity=rewardModelList.get(position).getTimestamp();
        boolean alreadyUsed=rewardModelList.get(position).isAlreadyUsed();
        String coupenId=rewardModelList.get(position).getCoupenId();
        viewHolder.setData(type,validity,body,upperLimit,lowerLimit,discORamt,alreadyUsed,coupenId);

    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coupenTitle = itemView.findViewById(R.id.coupen_title);
            coupenExpiryDate = itemView.findViewById(R.id.coupen_validity);
            coupenBody = itemView.findViewById(R.id.coupen_body);
        }

        private void setData(final String type, final Date validity, final String body, final String upperLimit, final String lowerLimit, final String discORamt, final boolean alreadyUsed, final String coupenId) {

            if(type.equals("Discount")){
                coupenTitle.setText(type);
            }else {
                coupenTitle.setText("FLAT Rs."+discORamt+" OFF");
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");

            if(alreadyUsed){
                coupenExpiryDate.setText("Already Used");
                coupenExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                coupenBody.setTextColor(Color.parseColor("#50ffffff"));
                coupenTitle.setTextColor(Color.parseColor("#50ffffff"));
            }else {
                coupenBody.setTextColor(Color.parseColor("#ffffff"));
                coupenTitle.setTextColor(Color.parseColor("#ffffff"));
                coupenExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.coupenPurple));
                coupenExpiryDate.setText("till " + simpleDateFormat.format(validity));
            }
            coupenBody.setText(body);

            if (useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!alreadyUsed) {

                            if (type.equals("Discount")) {
                                selectedCoupenTitle.setText(type);
                            } else {
                                selectedCoupenTitle.setText("FLAT Rs." + discORamt + " OFF");
                            }
                            selectedCoupenExpiryDate.setText("till " + simpleDateFormat.format(validity));
                            selectedCoupenBody.setText(body);

                            if (Long.valueOf(productOriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upperLimit)) {
                                if (type.equals("Discount")) {
                                    Long discountAmount = Long.valueOf(productOriginalPrice) * Long.valueOf(discORamt) / 100;
                                    discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount) + "/-");
                                } else {
                                    discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(discORamt)) + "/-");

                                }
                                if(position != -1) {
                                    cartItemModelList.get(position).setSelectedCoupenId(coupenId);
                                }
                            } else {
                                if(position != -1) {
                                    cartItemModelList.get(position).setSelectedCoupenId(null);
                                }
                                Toast.makeText(itemView.getContext(), "Product doesn't matches Coupan terms.", Toast.LENGTH_SHORT).show();
                            }

                            if (coupensRecyclerView.getVisibility() == View.GONE) {
                                coupensRecyclerView.setVisibility(View.VISIBLE);
                                selectedCoupen.setVisibility(View.GONE);
                            } else {
                                coupensRecyclerView.setVisibility(View.GONE);
                                selectedCoupen.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        }
    }
}

package com.example.mainactivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {


    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemRecyclerView;
    private Button continueBtn;

    private Dialog loadingDialog;
    public static CartAdapter cartAdapter;
    private TextView totalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        /////////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        /////////loading dialog

        cartItemRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        continueBtn=view.findViewById(R.id.cart_continue);
        totalAmount=view.findViewById(R.id.total_cart_amount);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemRecyclerView.setLayoutManager(layoutManager);



        cartAdapter = new CartAdapter(DBqueries.cartItemModelList,totalAmount,true);
        cartItemRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryActivity.cartItemModelList = new ArrayList<>();
                DeliveryActivity.fromCart =true;

                for (int x = 0; x < DBqueries.cartItemModelList.size();x++){
                    CartItemModel cartItemModel = DBqueries.cartItemModelList.get(x);
                    if (cartItemModel.isInStock()){
                        DeliveryActivity.cartItemModelList.add(cartItemModel);
                    }
                }
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                loadingDialog.show();
                if (DBqueries.addressesModelList.size() == 0) {
                    DBqueries.loadAddresses(getContext(), loadingDialog,true);
                }else {
                    loadingDialog.dismiss();
                    Intent deliveryIntent = new Intent(getContext(), DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.notifyDataSetChanged();

        if (DBqueries.rewardModelList.size() == 0){
            loadingDialog.show();
            DBqueries.loadRewards(getContext(),loadingDialog,false);
        }

        if (DBqueries.cartItemModelList.size() == 0){
            DBqueries.cartList.clear();
            DBqueries.loadCartList(getContext(),loadingDialog,true,new TextView(getContext()),totalAmount);
        }else {
            if (DBqueries.cartItemModelList.get(DBqueries.cartItemModelList.size()-1).getType() == CartItemModel.TOTAL_AMOUNT){
                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (CartItemModel cartItemModel : DBqueries.cartItemModelList){
            if (!TextUtils.isEmpty(cartItemModel.getSelectedCoupenId())){
                for(RewardModel rewardModel: DBqueries.rewardModelList){
                    if(rewardModel.getCoupenId().equals(cartItemModel.getSelectedCoupenId())){
                        rewardModel.setAlreadyUsed(false);

                    }
                }
                cartItemModel.setSelectedCoupenId(null);
                if(MyRewardsFragment.myRewardsAdapter != null){
                    MyRewardsFragment.myRewardsAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
package com.example.mainactivity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MyOrderFragment extends Fragment {


    public MyOrderFragment() {
        // Required empty public constructor
    }

    private Dialog loadingDialog;
    private RecyclerView myOrdersRecyclerView;
    public static MyOrderAdapter myOrderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_order, container, false);
        myOrdersRecyclerView=view.findViewById(R.id.my_orders_recyclerview);

        //////////loading dialog

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        //////////loading dialog

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        myOrderAdapter=new MyOrderAdapter(DBqueries.myOrderItemModelList,loadingDialog);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);

        if (DBqueries.myOrderItemModelList.size()== 0) {
            DBqueries.loadOrders(getContext(), myOrderAdapter, loadingDialog);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myOrderAdapter.notifyDataSetChanged();
    }
}
package com.framgia.project1.humanresourcemanagement.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.ui.activity.InputStaffInfoActivity;
import com.framgia.project1.humanresourcemanagement.ui.activity.ProfileActivity;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyCreateMenuContextListener;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyLongClickListener;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyOnClickListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewStaffAdapter extends RecyclerView.Adapter<RecyclerViewStaffAdapter.StaffViewHolder> {
    private List<Staff> mListStaff;
    private Context mContext;
    private int position;
    private MyOnClickListener mMyOnClickListener;
    private MyCreateMenuContextListener mMyCreateMenuContextListener;

    public RecyclerViewStaffAdapter(Context context, List<Staff> listStaff) {
        mContext = context;
        mListStaff = listStaff;
    }

    public void resetAdapter(List<Staff> listStaff) {
        mListStaff.clear();
        mListStaff.addAll(listStaff);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(MyOnClickListener listener) {
        this.mMyOnClickListener = listener;
    }

    public void setOnCreateMenuContextListener(MyCreateMenuContextListener listener) {
        this.mMyCreateMenuContextListener = listener;
    }

    @Override
    public StaffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
        StaffViewHolder staffViewHolder = new StaffViewHolder(view, mMyOnClickListener, mMyCreateMenuContextListener);
        return staffViewHolder;
    }

    @Override
    public void onBindViewHolder(final StaffViewHolder holder, int position) {
        holder.textViewName.setText(mListStaff.get(position).getName());
        holder.mPosition = position;
        if(mListStaff.get(position).getStatus() == Constant.STATUS_LEFT_JOB) {
            holder.textViewName.setBackgroundResource(android.R.color.darker_gray);
        }
        else {
            holder.textViewName.setBackgroundResource(android.R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return mListStaff.size();
    }

    class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
            , View.OnClickListener {
        protected TextView textViewName;
        private MyOnClickListener mMyOnclickListener;
        private MyCreateMenuContextListener mMyCreateMenuContextListener;
        private int mPosition;

        public StaffViewHolder(View itemView, MyOnClickListener listener, MyCreateMenuContextListener createContextMenuListener) {
            super(itemView);
            this.mMyOnclickListener = listener;
            this.mMyCreateMenuContextListener = createContextMenuListener;
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
            textViewName = (TextView) itemView.findViewById(R.id.text_view_staff_name);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if(mMyCreateMenuContextListener != null) {
                mMyCreateMenuContextListener.onCreateContextMenu(menu, v, menuInfo ,mPosition);
            }
        }

        @Override
        public void onClick(View v) {
            if(mMyOnclickListener != null) {
                mMyOnclickListener.onItemClick(v, mPosition);
            }
        }
    }
}
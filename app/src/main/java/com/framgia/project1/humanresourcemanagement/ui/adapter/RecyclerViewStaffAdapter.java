package com.framgia.project1.humanresourcemanagement.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyCreateMenuContextListener;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyOnClickListener;
import com.framgia.project1.humanresourcemanagement.ui.widget.ImageUtils;

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
        List<Staff> storeList = new ArrayList<>();
        storeList.addAll(listStaff);
        mListStaff.clear();
        mListStaff.addAll(storeList);
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
        String[] listPosition = mContext.getResources().getStringArray(R.array.array_position);
        holder.textViewName.setText(mListStaff.get(position).getName());
        holder.textViewPosition.setText(listPosition[mListStaff.get(position).getPosition()]);
        if (mListStaff.get(position).getStatus() == Constant.STATUS_LEFT_JOB) {
            holder.textViewName.setBackgroundResource(android.R.color.darker_gray);
        } else {
            holder.textViewName.setBackgroundResource(android.R.color.white);
        }
        String pathImage = mListStaff.get(position).getImageAvatar();
        if (pathImage.equals(Constant.NOAVARTAR))
            holder.imageViewStaff.setImageResource(R.drawable.origin_avatar);
        else {
            holder.imageViewStaff.setImageBitmap(ImageUtils.decodeBitmapFromPath(pathImage, Constant.IMAGE_MAX_SIZE, Constant.IMAGE_MAX_SIZE));
        }
        holder.mPosition = position;
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity runningActivity = (Activity)mContext;
                runningActivity.openContextMenu(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListStaff.size();
    }

    class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
            , View.OnClickListener {
        protected TextView textViewName;
        protected TextView textViewPosition;
        protected ImageView imageViewStaff;
        protected ImageButton imageButton;
        private MyOnClickListener mMyOnclickListener;
        private MyCreateMenuContextListener mMyCreateMenuContextListener;
        private int mPosition;

        public StaffViewHolder(View itemView, MyOnClickListener listener, MyCreateMenuContextListener createContextMenuListener) {
            super(itemView);
            this.mMyOnclickListener = listener;
            this.mMyCreateMenuContextListener = createContextMenuListener;
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
            imageViewStaff = (ImageView) itemView.findViewById(R.id.image_view_staff);
            textViewName = (TextView) itemView.findViewById(R.id.text_view_staff_name);
            textViewPosition = (TextView) itemView.findViewById(R.id.staff_position);
            imageButton = (ImageButton) itemView.findViewById(R.id.button_context_menu);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (mMyCreateMenuContextListener != null) {
                mMyCreateMenuContextListener.onCreateContextMenu(menu, v, menuInfo, mPosition);
            }
        }

        @Override
        public void onClick(View v) {
            if (mMyOnclickListener != null) {
                mMyOnclickListener.onItemClick(v, mPosition);
            }
        }
    }
}
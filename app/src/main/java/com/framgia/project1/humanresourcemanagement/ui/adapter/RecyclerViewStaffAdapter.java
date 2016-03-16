package com.framgia.project1.humanresourcemanagement.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.ui.activity.ProfileActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hacks_000 on 3/14/2016.
 */
public class RecyclerViewStaffAdapter extends RecyclerView.Adapter<RecyclerViewStaffAdapter.StaffViewHolder> {
    private List<Staff> mListStaff;
    private Context mContext;

    public RecyclerViewStaffAdapter(Context context, List<Staff> listStaff) {
        mContext = context;
        mListStaff = listStaff;
    }

    @Override
    public StaffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
        StaffViewHolder staffViewHolder = new StaffViewHolder(view);
        return staffViewHolder;
    }

    @Override
    public void onBindViewHolder(StaffViewHolder holder, int position) {
        holder.textViewName.setText(mListStaff.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mListStaff.size();
    }

    class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewName;
        public StaffViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewName = (TextView) itemView.findViewById(R.id.text_view_staff_name);
        }

        @Override
        public void onClick(View v) {
            Staff staff = mListStaff.get(getPosition());
            ArrayList<String> profileData = new ArrayList<String>();
            profileData.add(Constant.PLACE_OF_BIRTH, staff.getPlaceOfBirth());
            profileData.add(Constant.BIRTH_DAY, staff.getBirthday());
            profileData.add(Constant.PHONE_NUMBER, staff.getPhoneNumber());
            profileData.add(Constant.POSITION, "");
            profileData.add(Constant.STATUS, "");
            Intent intent = new Intent(mContext, ProfileActivity.class);
            intent.putStringArrayListExtra(Constant.INTENT_DATA, profileData);
            intent.putExtra(Constant.INTENT_DATA_TITLE, staff.getName());
            mContext.startActivity(intent);
        }
    }
}

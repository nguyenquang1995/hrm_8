package com.framgia.project1.humanresourcemanagement.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.Department;
import com.framgia.project1.humanresourcemanagement.ui.activity.ListStaffActivity;

import java.util.List;

/**
 * Created by hacks_000 on 3/9/2016.
 */
public class RecyclerViewDepartmentAdapter extends RecyclerView.Adapter<RecyclerViewDepartmentAdapter.DepartmentViewHolder> {
    private List<Department> listDepartment;
    private Context mContext;

    public RecyclerViewDepartmentAdapter(Context context, List<Department> listDepartment) {
        this.mContext = context;
        this.listDepartment = listDepartment;
    }

    @Override
    public DepartmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_department, viewGroup, false);
        DepartmentViewHolder departmentViewHolder = new DepartmentViewHolder(view);
        return departmentViewHolder;
    }

    @Override
    public void onBindViewHolder(DepartmentViewHolder departmentViewHolder, int i) {
        departmentViewHolder.textView.setText(listDepartment.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return listDepartment.size();
    }

    class DepartmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textView;

        DepartmentViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.text_view_department);
        }

        @Override
        public void onClick(View v) {
            Department department = listDepartment.get(getPosition());
            Intent intent = new Intent(mContext, ListStaffActivity.class);
            intent.putExtra(Constant.INTENT_DATA_TITLE, department.getName());
            mContext.startActivity(intent);
        }
    }
}

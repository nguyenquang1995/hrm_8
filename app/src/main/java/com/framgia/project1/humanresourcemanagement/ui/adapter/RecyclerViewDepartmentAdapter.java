package com.framgia.project1.humanresourcemanagement.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Department;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyOnClickListener;

import java.util.List;

/**
 * Created by hacks_000 on 3/9/2016.
 */
public class RecyclerViewDepartmentAdapter extends RecyclerView.Adapter<RecyclerViewDepartmentAdapter.DepartmentViewHolder> {
    private List<Department> listDepartment;
    private Context mContext;
    private MyOnClickListener mMyOnClickListener;

    public RecyclerViewDepartmentAdapter(Context context, List<Department> listDepartment) {
        this.mContext = context;
        this.listDepartment = listDepartment;
    }

    @Override
    public DepartmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_department, viewGroup, false);
        DepartmentViewHolder departmentViewHolder = new DepartmentViewHolder(view, mMyOnClickListener);
        return departmentViewHolder;
    }

    @Override
    public void onBindViewHolder(DepartmentViewHolder departmentViewHolder, int i) {
        departmentViewHolder.textView.setText(listDepartment.get(i).getName());
        departmentViewHolder.mPosition = i;
    }

    @Override
    public int getItemCount() {
        return listDepartment.size();
    }

    public void setOnItemClickListener(MyOnClickListener listener) {
        this.mMyOnClickListener = listener;
    }

    class DepartmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textView;
        private MyOnClickListener mMyOnClickListener;
        private int mPosition;

        DepartmentViewHolder(View itemView, MyOnClickListener listener) {
            super(itemView);
            this.mMyOnClickListener = listener;
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.text_view_department);
        }

        @Override
        public void onClick(View v) {
            if(mMyOnClickListener != null) {
                mMyOnClickListener.onItemClick(v, mPosition);
            }
        }
    }
}
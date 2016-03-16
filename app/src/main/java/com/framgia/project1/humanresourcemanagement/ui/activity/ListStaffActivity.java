package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.DepartmentDAO;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.ui.adapter.RecyclerViewStaffAdapter;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import java.util.List;

public class ListStaffActivity extends AppCompatActivity implements Paginate.Callbacks {
    private List<Staff> listStaff;
    private List<Staff> allStaff;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    RecyclerViewStaffAdapter recyclerViewStaffAdapter;
    private boolean loading = false;
    private int itemLoaded = 0;
    private Handler handler;
    private Paginate paginate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_staff);
        findView();
        handler = new Handler();
        setupPagination();
    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getCharSequenceExtra(Constant.INTENT_DATA_TITLE));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_staff);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        getListStaff();
        recyclerViewStaffAdapter = new RecyclerViewStaffAdapter(this, listStaff);
        recyclerView.setAdapter(recyclerViewStaffAdapter);
    }

    private void getListStaff() {
        allStaff = DepartmentDAO.createDummyStaffData();
        listStaff = allStaff.subList(0, Constant.STAFF_PER_PAGE);
    }

    private void setupPagination() {
        if(paginate != null) {
            paginate.unbind();
        }
        handler.removeCallbacks(fakeCallback);
        paginate = Paginate.with(recyclerView, this)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(new CustomLoadingItemCreator())
                .build();
    }

    @Override
    public void onLoadMore() {
        loading = true;
        handler.postDelayed(fakeCallback, Constant.DELAY_TIME);
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        if(itemLoaded == allStaff.size()) {
            return true;
        }
        return false;
    }

    private Runnable fakeCallback = new Runnable() {
        @Override
        public void run() {
            int end = itemLoaded + Constant.STAFF_PER_PAGE;
            if(end >= allStaff.size()) {
                end = allStaff.size();
            }
            listStaff = allStaff.subList(itemLoaded, end);
            recyclerViewStaffAdapter = new RecyclerViewStaffAdapter(ListStaffActivity.this, listStaff);
            recyclerView.setAdapter(recyclerViewStaffAdapter);
            itemLoaded = end;
            loading = false;
        }
    };

    private class CustomLoadingItemCreator implements LoadingListItemCreator {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_staff, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.textViewName.setText("Loadding next page...");
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textViewName;

        public MyViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            textViewName = (TextView) itemView.findViewById(R.id.text_view_staff_name);
        }

        @Override
        public void onClick(View v) {

        }
    }
}

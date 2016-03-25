package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.DepartmentDAO;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.data.remote.DatabaseRemote;
import com.framgia.project1.humanresourcemanagement.ui.adapter.RecyclerViewStaffAdapter;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyCreateMenuContextListener;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyOnClickListener;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListStaffActivity extends AppCompatActivity implements Paginate.Callbacks, MyCreateMenuContextListener, MyOnClickListener {
    private List<Staff> mListStaff;
    private List<Staff> mListNextPage;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerViewStaffAdapter mRecyclerViewStaffAdapter;
    private boolean loading;
    private Handler mHandler;
    private Paginate mPaginate;
    private DatabaseRemote mDatabaseRemote;
    private int departmentId;
    private int mStartIndex;
    private int mStaffChoosedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_staff);
        init();
        findView();
        setupPagination();
    }

    private void init() {
        mDatabaseRemote = new DatabaseRemote(this);
        mHandler = new Handler();
        mStartIndex = -1;
        departmentId = getIntent().getIntExtra(Constant.INTENT_DATA, 0);
        loading = false;
        mListStaff = new ArrayList<>();
        mListNextPage = new ArrayList<>();
        DepartmentDAO departmentDAO = new DepartmentDAO(this);
        departmentDAO.creatDummyStaffIfStaffIsEmpty(departmentId);
    }

    private void findView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getIntent().getCharSequenceExtra(Constant.INTENT_DATA_TITLE));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list_staff);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        getListStaff();
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerViewStaffAdapter = new RecyclerViewStaffAdapter(this, mListStaff);
        mRecyclerViewStaffAdapter.setOnItemClickListener(this);
        mRecyclerViewStaffAdapter.setOnCreateMenuContextListener(this);
        mRecyclerView.setAdapter(mRecyclerViewStaffAdapter);
        registerForContextMenu(mRecyclerView);
    }

    private void getListStaff() {
        try {
            mDatabaseRemote.openDataBase();
            mListNextPage = mDatabaseRemote.getListStaff(mStartIndex, departmentId);
            mListStaff = (mListNextPage.size() > 0) ? mListNextPage : mListStaff;
            mDatabaseRemote.closeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupPagination() {
        if (mListStaff.size() == Constant.STAFF_PER_PAGE) {
            if (mPaginate != null) {
                mPaginate.unbind();
            }
            mHandler.removeCallbacks(fakeCallback);
            mPaginate = Paginate.with(mRecyclerView, this)
                    .addLoadingListItem(!hasLoadedAllItems())
                    .setLoadingListItemCreator(new CustomLoadingItemCreator())
                    .build();
        }
    }

    @Override
    public synchronized void onLoadMore() {
        loading = true;
        mHandler.postDelayed(fakeCallback, Constant.DELAY_TIME);
    }

    @Override
    public synchronized boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        boolean isLoadAll = false;
        if (mListNextPage.size() == 0) {
            isLoadAll = true;
        }
        return isLoadAll;
    }

    private Runnable fakeCallback = new Runnable() {
        @Override
        public void run() {
            mStartIndex = mStartIndex + Constant.STAFF_PER_PAGE;
            getListStaff();
            mRecyclerViewStaffAdapter.resetAdapter(mListStaff);
            loading = false;
            setupPagination();
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo, int position) {
        mStaffChoosedPosition = position;
        String[] menuItems = getResources().getStringArray(R.array.array_context_menu_list_staff);
        menu.setHeaderTitle(mListStaff.get(position).getName());
        int menuLength = menuItems.length;
        for (int i = 0; i < menuLength; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == Constant.CONTEXT_MENU_LEFT_JOB) {
            changeStaffToLeftJob(mStaffChoosedPosition);
            return true;
        } else if (item.getItemId() == Constant.CONTEXT_MENU_EDIT) {
            startEditProfile(mStaffChoosedPosition);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        startProfileActivity(position);
    }

    private void startProfileActivity(int position) {
        Staff staff = mListStaff.get(position);
        Intent intent = new Intent(ListStaffActivity.this, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.INTENT_DATA, staff);
        intent.putExtra(Constant.INTENT_DATA, bundle);
        startActivity(intent);
    }

    private void startEditProfile(int position) {
        Staff staff = mListStaff.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.ID_INTENT, staff.getId());
        Intent intent = new Intent(ListStaffActivity.this, InputStaffInfoActivity.class);
        intent.putExtra(Constant.ID, bundle);
        startActivity(intent);
    }

    private void changeStaffToLeftJob(int position) {
        try {
            mDatabaseRemote.openDataBase();
            mDatabaseRemote.updateStatus(mListStaff.get(position).getId(), Constant.STATUS_LEFT_JOB);
            getListStaff();
            mRecyclerViewStaffAdapter.resetAdapter(mListStaff);
            mDatabaseRemote.closeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class CustomLoadingItemCreator implements LoadingListItemCreator {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.custom_loading_list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getListStaff();
        mRecyclerViewStaffAdapter.resetAdapter(mListStaff);
    }
}
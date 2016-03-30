package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.DepartmentDAO;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.data.remote.DatabaseRemote;
import com.framgia.project1.humanresourcemanagement.ui.adapter.RecyclerViewStaffAdapter;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyCreateMenuContextListener;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyOnClickListener;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyRecyclerViewScrollListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListStaffActivity extends AppCompatActivity implements MyCreateMenuContextListener, MyOnClickListener {
    private List<Staff> mListStaff;
    private List<Staff> mListNextPage;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerViewStaffAdapter mRecyclerViewStaffAdapter;
    private DatabaseRemote mDatabaseRemote;
    private int mDepartmentId;
    private int mStartIndex;
    private int mEndIndex;
    private int mStaffChoosedPosition;
    private ProgressDialog mProgressDialog;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean mStopLoadding;
    private boolean mAsynTaskFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_staff);
        init();
        findView();
        GetListStaffTask getListStaffTask = new GetListStaffTask();
        getListStaffTask.execute();
        mRecyclerViewStaffAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStaffChoosedPosition != -1) {
            try {
                mDatabaseRemote.openDataBase();
                Cursor cursor = mDatabaseRemote.searchStaff(mListStaff.get(mStaffChoosedPosition).getId());
                Staff staff = new Staff(cursor);
                mListStaff.remove(mStaffChoosedPosition);
                if (staff.getIdDepartment() == mDepartmentId) {
                    mListStaff.add(mStaffChoosedPosition, staff);
                    mRecyclerViewStaffAdapter.notifyItemChanged(mStaffChoosedPosition);
                }
                else {
                    mRecyclerViewStaffAdapter.notifyItemRemoved(mStaffChoosedPosition);
                }

                mDatabaseRemote.closeDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        mDatabaseRemote = new DatabaseRemote(this);
        mStartIndex = -1;
        mStaffChoosedPosition = -1;
        mEndIndex = 0;
        mStopLoadding = false;
        mAsynTaskFinish = true;
        mDepartmentId = getIntent().getIntExtra(Constant.INTENT_DATA, 0);
        mListStaff = new ArrayList<>();
        mListNextPage = new ArrayList<>();
        DepartmentDAO departmentDAO = new DepartmentDAO(this);
        departmentDAO.creatDummyStaffIfStaffIsEmpty(mDepartmentId);
    }

    private void findView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getIntent().getCharSequenceExtra(Constant.INTENT_DATA_TITLE));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list_staff);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerViewStaffAdapter = new RecyclerViewStaffAdapter(this, mListStaff);
        mRecyclerViewStaffAdapter.setOnItemClickListener(this);
        mRecyclerViewStaffAdapter.setOnCreateMenuContextListener(this);
        mRecyclerView.setAdapter(mRecyclerViewStaffAdapter);
        mRecyclerView.addOnScrollListener(new MyRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isAbleLoadMore(mEndIndex) && dy > 0 && !mStopLoadding && mAsynTaskFinish) {
                    GetListStaffTask getListStaffTask = new GetListStaffTask();
                    getListStaffTask.execute();
                }
            }
        });
        registerForContextMenu(mRecyclerView);
    }

    private void getListStaff() {
        try {
            mDatabaseRemote.openDataBase();
            mListNextPage = mDatabaseRemote.getListStaff(mStartIndex, mDepartmentId);
            if (mListNextPage.size() == 0) {
                mStopLoadding = true;
            }
            mListStaff.addAll(mListNextPage);
            mEndIndex = mListStaff.size();
            mDatabaseRemote.closeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
        mStaffChoosedPosition = position;
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
            mListStaff.get(position).setStatus(Constant.STATUS_LEFT_JOB);
            mRecyclerViewStaffAdapter.notifyItemChanged(position);
            mDatabaseRemote.closeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class GetListStaffTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            mAsynTaskFinish = false;
            mProgressDialog = new ProgressDialog(ListStaffActivity.this);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setProgress(Constant.PROGRESS_MIN);
            mProgressDialog.setMax(Constant.PROGRESS_MAX);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getListStaff();
            try {
                Thread.sleep(Constant.DELAY_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAsynTaskFinish = true;
            mStartIndex += Constant.STAFF_PER_PAGE;
            mProgressDialog.hide();
            mRecyclerViewStaffAdapter.notifyDataSetChanged();
        }
    }
}
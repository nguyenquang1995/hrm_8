package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.data.remote.DatabaseRemote;
import com.framgia.project1.humanresourcemanagement.ui.adapter.RecyclerViewStaffAdapter;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyCreateMenuContextListener;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyOnClickListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity implements MyOnClickListener, MyCreateMenuContextListener {
    private Toolbar mToolbar;
    private String textSearch;
    private SearchView.OnQueryTextListener mSearchViewListener;
    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;
    private RecyclerView recyclerView;
    private RecyclerViewStaffAdapter recyclerViewStaffAdapter;
    private MaterialSpinner mSpinnerTypeSearch;
    private List<Staff> mListStaff;
    private DatabaseRemote mDatabaseRemote;
    private int mTypeSearch;
    private boolean isOnQueryTextChangeSearch;
    private int mStaffChoosedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        init();
        findView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mStaffChoosedPosition != -1) {
            try {
                mDatabaseRemote.openDataBase();
                Cursor cursor = mDatabaseRemote.searchStaff(mListStaff.get(mStaffChoosedPosition).getId());
                Staff staff = new Staff(cursor);
                mListStaff.remove(mStaffChoosedPosition);
                mListStaff.add(mStaffChoosedPosition, staff);
                recyclerViewStaffAdapter.notifyItemChanged(mStaffChoosedPosition);
                mDatabaseRemote.closeDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        mTypeSearch = Constant.SEARCH_BY_NAME;
        isOnQueryTextChangeSearch = false;
        mStaffChoosedPosition = -1;
    }

    private void findView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textSearch = getIntent().getStringExtra(Constant.INTENT_DATA);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_staff);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        doSearch();
        recyclerViewStaffAdapter = new RecyclerViewStaffAdapter(this, mListStaff);
        recyclerViewStaffAdapter.setOnItemClickListener(this);
        recyclerViewStaffAdapter.setOnCreateMenuContextListener(this);
        recyclerView.setAdapter(recyclerViewStaffAdapter);
        mSearchViewListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textSearch = query;
                doSearch();
                recyclerViewStaffAdapter.resetAdapter(mListStaff);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    if (isOnQueryTextChangeSearch) {
                        textSearch = newText;
                        doSearch();
                        recyclerViewStaffAdapter.resetAdapter(mListStaff);
                    } else {
                        isOnQueryTextChangeSearch = true;
                    }
                }
                return false;
            }
        };
        mSpinnerTypeSearch = (MaterialSpinner) findViewById(R.id.spinner_type_search);
        final String[] types = getResources().getStringArray(R.array.array_spinner_type_search);
        List<String> listTypeSearch = new ArrayList<String>();
        for (String s : types) {
            listTypeSearch.add(s);
        }
        mSpinnerTypeSearch.setItems(listTypeSearch);
        mSpinnerTypeSearch.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mTypeSearch = position;
                if (mTypeSearch == Constant.SEARCH_BY_PHONENUMBER) {
                    textSearch = "";
                    mSearchView.setQuery("", false);
                    mSearchView.setInputType(InputType.TYPE_CLASS_PHONE);
                } else {
                    mSearchView.setQuery("", false);
                    mSearchView.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }

    private void getListStaff() {
        mDatabaseRemote = new DatabaseRemote(this);
        try {
            mDatabaseRemote.openDataBase();
            switch (mTypeSearch) {
                case Constant.SEARCH_BY_NAME:
                    mListStaff = mDatabaseRemote.getListStaff(textSearch);
                    break;
                case Constant.SEARCH_BY_DEPARTMENT:
                    mListStaff = mDatabaseRemote.getListStaffByDepartment(textSearch);
                    break;
                case Constant.SEARCH_BY_PHONENUMBER:
                    mListStaff = mDatabaseRemote.getListStaffByPhoneNumber(textSearch);
                    break;
            }

            mDatabaseRemote.closeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void doSearch() {
        getListStaff();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(mSearchViewListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        startProfileActivity(position);
    }

    private void startProfileActivity(int position) {
        Staff staff = mListStaff.get(position);
        Intent intent = new Intent(SearchResultActivity.this, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.INTENT_DATA, staff);
        intent.putExtra(Constant.INTENT_DATA, bundle);
        startActivity(intent);
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

    private void startEditProfile(int position) {
        Staff staff = mListStaff.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.ID_INTENT, staff.getId());
        Intent intent = new Intent(SearchResultActivity.this, InputStaffInfoActivity.class);
        intent.putExtra(Constant.ID, bundle);
        startActivity(intent);
    }

    private void changeStaffToLeftJob(int position) {
        try {
            mDatabaseRemote.openDataBase();
            mDatabaseRemote.updateStatus(mListStaff.get(position).getId(), Constant.STATUS_LEFT_JOB);
            mListStaff.get(position).setStatus(Constant.STATUS_LEFT_JOB);
            recyclerViewStaffAdapter.notifyItemChanged(position);
            mDatabaseRemote.closeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.data.remote.DatabaseRemote;
import com.framgia.project1.humanresourcemanagement.ui.adapter.RecyclerViewStaffAdapter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchResultActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private String textSearch;
    private SearchView.OnQueryTextListener mSearchViewListener;
    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;
    private RecyclerView recyclerView;
    private RecyclerViewStaffAdapter recyclerViewStaffAdapter;
    private Spinner mSpinnerTypeSearch;
    private List<Staff> listStaff;
    private DatabaseRemote mDatabaseRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        findView();
    }

    private void findView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textSearch = getIntent().getStringExtra(Constant.INTENT_DATA);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_staff);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        doSearch();
        recyclerViewStaffAdapter = new RecyclerViewStaffAdapter(this, listStaff);
        recyclerView.setAdapter(recyclerViewStaffAdapter);
        mSearchViewListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textSearch = query;
                doSearch();
                recyclerViewStaffAdapter = new RecyclerViewStaffAdapter(getApplicationContext(), listStaff);
                recyclerView.setAdapter(recyclerViewStaffAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textSearch = newText;
                doSearch();
                recyclerViewStaffAdapter = new RecyclerViewStaffAdapter(getApplicationContext(), listStaff);
                recyclerView.setAdapter(recyclerViewStaffAdapter);
                return false;
            }
        };
        mSpinnerTypeSearch = (Spinner) findViewById(R.id.spinner_type_search);
        String[] types = getResources().getStringArray(R.array.array_spinner_type_search);
        List<String> listTypeSearch = new ArrayList<String>();
        for (String s : types) {
            listTypeSearch.add(s);
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTypeSearch.setAdapter(spinnerAdapter);
        mSpinnerTypeSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doSearch();
                recyclerViewStaffAdapter = new RecyclerViewStaffAdapter(getApplicationContext(), listStaff);
                recyclerView.setAdapter(recyclerViewStaffAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getListStaff() {
        mDatabaseRemote = new DatabaseRemote(this);
        try {
            mDatabaseRemote.openDataBase();
            listStaff = mDatabaseRemote.getListStaff(textSearch);
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
}

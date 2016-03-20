package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.DepartmentDAO;
import com.framgia.project1.humanresourcemanagement.ui.adapter.RecyclerViewDepartmentAdapter;
import com.framgia.project1.humanresourcemanagement.data.model.Department;
import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.ui.mylistener.MyOnClickListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MyOnClickListener {
    private List<Department> listDepartment;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SearchView.OnQueryTextListener mSearchViewListener;
    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;
    private DepartmentDAO departmentDAO;
    RecyclerViewDepartmentAdapter mRecyclerViewDepartmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_home);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        departmentDAO = new DepartmentDAO(this);
        listDepartment = departmentDAO.getListDepartment();
        mRecyclerViewDepartmentAdapter = new RecyclerViewDepartmentAdapter(this, listDepartment);
        mRecyclerViewDepartmentAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mRecyclerViewDepartmentAdapter);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputStaffInfoActivity.class);
                startActivity(intent);
            }
        });
        mSearchViewListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                intent.putExtra(Constant.INTENT_DATA, query);
                startActivity(intent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
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
        Department department = listDepartment.get(position);
        Intent intent = new Intent(MainActivity.this, ListStaffActivity.class);
        intent.putExtra(Constant.INTENT_DATA_TITLE, department.getName());
        intent.putExtra(Constant.INTENT_DATA, department.getId());
        startActivity(intent);
    }
}
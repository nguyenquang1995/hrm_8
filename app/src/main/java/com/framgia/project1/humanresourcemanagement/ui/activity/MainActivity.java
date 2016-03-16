package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.framgia.project1.humanresourcemanagement.data.model.DepartmentDAO;
import com.framgia.project1.humanresourcemanagement.ui.adapter.RecyclerViewDepartmentAdapter;
import com.framgia.project1.humanresourcemanagement.data.model.Department;
import com.framgia.project1.humanresourcemanagement.R;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Department> listDepartment;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

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
        getListDepartment();
        RecyclerViewDepartmentAdapter recyclerViewDepartmentAdapter = new RecyclerViewDepartmentAdapter(this, listDepartment);
        recyclerView.setAdapter(recyclerViewDepartmentAdapter);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void getListDepartment() {
        listDepartment = DepartmentDAO.createDummyDepartmentData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

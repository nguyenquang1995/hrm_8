package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.ui.adapter.RecyclerViewProfileAdapter;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private List mProfileData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findView();
    }

    private void findView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getIntent().getCharSequenceExtra(Constant.INTENT_DATA_TITLE));
        setSupportActionBar(mToolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_profile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        getProfileData();
        RecyclerViewProfileAdapter recyclerViewProfileAdapter = new RecyclerViewProfileAdapter(mProfileData);
        mRecyclerView.setAdapter(recyclerViewProfileAdapter);
    }

    private void getProfileData() {
        mProfileData = getIntent().getStringArrayListExtra(Constant.INTENT_DATA);
    }

}

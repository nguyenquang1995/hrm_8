package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.ui.adapter.RecyclerViewProfileAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private List mProfileData;
    private Staff mStaff;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_profile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        getProfileData();
        RecyclerViewProfileAdapter recyclerViewProfileAdapter = new RecyclerViewProfileAdapter(mProfileData);
        mRecyclerView.setAdapter(recyclerViewProfileAdapter);
    }

    private void getProfileData() {
        Bundle bundle = getIntent().getBundleExtra(Constant.INTENT_DATA);
        mStaff = bundle.getParcelable(Constant.INTENT_DATA);
        String[] listStatus = getResources().getStringArray(R.array.array_status);
        String[] listPosition = getResources().getStringArray(R.array.array_position);
        mProfileData = new ArrayList();
        mProfileData.add(Constant.PLACE_OF_BIRTH, mStaff.getPlaceOfBirth());
        mProfileData.add(Constant.BIRTH_DAY, mStaff.getBirthday());
        mProfileData.add(Constant.PHONE_NUMBER, mStaff.getPhoneNumber());
        mProfileData.add(Constant.POSITION, listPosition[mStaff.getPosition()]);
        mProfileData.add(Constant.STATUS, listStatus[mStaff.getStatus()]);
    }
}
package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.data.remote.DatabaseRemote;
import com.framgia.project1.humanresourcemanagement.ui.adapter.RecyclerViewProfileAdapter;
import com.framgia.project1.humanresourcemanagement.ui.widget.ImageUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private List mProfileData;
    private Staff mStaff;
    private ImageView mStaffAvatar;
    private TextView mStaffName;
    private ImageView mImageCover;
    private FloatingActionButton mFab;
    private DatabaseRemote mDatabaseRemote;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private  RecyclerViewProfileAdapter mRecyclerViewProfileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        getProfileData();
        findView();
    }

    private void init() {
        mDatabaseRemote = new DatabaseRemote(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int staffId = mStaff.getId();
        try {
            mDatabaseRemote.openDataBase();
            Cursor cursor = mDatabaseRemote.searchStaff(staffId);
            if(cursor != null) {
                mStaff = new Staff(cursor);
                updateInfo();
            }
            mDatabaseRemote.closeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateInfo() {
        String avatar = mStaff.getImageAvatar();
        if (avatar.equals(Constant.NOAVARTAR))
            mStaffAvatar.setImageResource(R.drawable.origin_avatar);
        else {
            mStaffAvatar.setImageBitmap(ImageUtils.decodeBitmapFromPath(avatar, Constant.IMAGE_MAX_SIZE, Constant.IMAGE_MAX_SIZE));
        }
        mStaffName.setText(mStaff.getName());
        mToolbar.setTitle(mStaff.getName());
        mCollapsingToolbarLayout.setTitle(mStaff.getName());
        String[] listStatus = getResources().getStringArray(R.array.array_status);
        String[] listPosition = getResources().getStringArray(R.array.array_position);
        mProfileData = new ArrayList();
        mProfileData.add(Constant.PLACE_OF_BIRTH, mStaff.getPlaceOfBirth());
        mProfileData.add(Constant.BIRTH_DAY, mStaff.getBirthday());
        mProfileData.add(Constant.PHONE_NUMBER, mStaff.getPhoneNumber());
        mProfileData.add(Constant.POSITION, listPosition[mStaff.getPosition()]);
        mProfileData.add(Constant.STATUS, listStatus[mStaff.getStatus()]);
        mRecyclerViewProfileAdapter.resetAdapter(mProfileData);
    }

    private void findView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(mStaff.getName());
        mStaffAvatar = (ImageView) findViewById(R.id.imageViewStaff);
        mStaffName = (TextView) findViewById(R.id.textViewStaff);
        String avatar = mStaff.getImageAvatar();
        if (avatar.equals(Constant.NOAVARTAR))
            mStaffAvatar.setImageResource(R.drawable.origin_avatar);
        else {
            mStaffAvatar.setImageBitmap(ImageUtils.decodeBitmapFromPath(avatar, Constant.IMAGE_MAX_SIZE, Constant.IMAGE_MAX_SIZE));
        }
        mStaffName.setText(mStaff.getName());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_profile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        getProfileData();
        mRecyclerViewProfileAdapter = new RecyclerViewProfileAdapter(mProfileData);
        mRecyclerView.setAdapter(mRecyclerViewProfileAdapter);
        mImageCover = (ImageView) findViewById(R.id.image_cover);
        mImageCover.setImageBitmap(ImageUtils.decodeSampleBitmapFromResource(getResources(), R.drawable.singapore, Constant.IMAGE_MAX_SIZE, Constant.IMAGE_MAX_SIZE));
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.ID_INTENT, mStaff.getId());
                Intent intent = new Intent(ProfileActivity.this, InputStaffInfoActivity.class);
                intent.putExtra(Constant.ID, bundle);
                startActivity(intent);
            }
        });
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TransparentText);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
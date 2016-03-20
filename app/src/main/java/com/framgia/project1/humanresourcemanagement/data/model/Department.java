package com.framgia.project1.humanresourcemanagement.data.model;

import android.database.Cursor;
import com.framgia.project1.humanresourcemanagement.data.local.DataBaseHelper;

public class Department implements DBSchemaConstant {
    private String mName;
    private int mId;
    private String image;

    public Department(String name, int id, String image) {
        this.mName = name;
        this.mId = id;
        this.image = image;
    }

    public Department(Cursor cursor) {
        this.mId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DEPARTMENT));
        this.mName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DEPARTMENT));
        this.image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_DEPARTMENT));
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
package com.framgia.project1.humanresourcemanagement.data.remote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.framgia.project1.humanresourcemanagement.data.local.DataBaseHelper;
import com.framgia.project1.humanresourcemanagement.data.model.DBSchemaConstant;
import com.framgia.project1.humanresourcemanagement.data.model.Department;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;

import java.sql.SQLData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nguyenxuantung on 11/03/2016.
 */
public class DatabaseRemote implements DBSchemaConstant {

    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;

    public DatabaseRemote(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    //open database
    public void openDataBase() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    //close database
    public void closeDataBase() throws SQLException {
        database.close();
    }

    //insert data into table department
    public long insertDepartment(Department department) {
        long result = -1;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_DEPARTMENT, department.getName());
        contentValues.put(COLUMN_IMAGE_DEPARTMENT, department.getImage());
        try {
            result = database.insertOrThrow(DBSchemaConstant.TABLE_DEPARTMENT, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // get list of department
    public List<Department> getDepartmentList() {
        String quey = "select * from " + DBSchemaConstant.TABLE_DEPARTMENT;
        int id;
        String name, image;
        Department de;
        Cursor cursor = null;
        cursor = database.rawQuery(quey, null);
        List<Department> departmentList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                departmentList.add(new Department(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return departmentList;
    }

    //delete a record in department
    public boolean deleteDepartment(int id) {
        try {
            database.delete(TABLE_DEPARTMENT, COLUMN_ID_DEPARTMENT + " = " + id, null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //insert a record into table staff
    public long insertStaff(Staff staff) {
        long result = -1;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_DEPARTMENT, staff.getIdDepartment());
        values.put(COLUMN_NAME_STAFF, staff.getName());
        values.put(COLUMN_PHONENUMBER, staff.getPhoneNumber());
        values.put(COLUMN_PLACEOFBIRTH, staff.getPlaceOfBirth());
        values.put(COLUMN_POSITION, staff.getPosition());
        values.put(COLUMN_STATUS, staff.getStatus());
        values.put(COLUMN_AVATAR, staff.getImageAvatar());
        values.put(COLUMN_BIRTHDAY, staff.getBirthday());
        try {
            result = database.insertOrThrow(TABLE_STAFF, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //get list of staff
    public List<Staff> getListStaff() {
        String query = "select * from " + TABLE_STAFF;
        Cursor cursor = null;
        cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        List<Staff> staffList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            staffList.add(new Staff(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return staffList;
    }

    //delete a record in staff
    public boolean deleteStaff(int id) {
        try {
            database.delete(TABLE_STAFF, COLUMN_ID_STAFF + " = " + id, null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
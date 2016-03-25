package com.framgia.project1.humanresourcemanagement.data.remote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.framgia.project1.humanresourcemanagement.data.local.DataBaseHelper;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.DBSchemaConstant;
import com.framgia.project1.humanresourcemanagement.data.model.Department;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;

import java.sql.SQLData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRemote implements DBSchemaConstant {
    private SQLiteDatabase mDatabase;
    private DataBaseHelper mDbHelper;

    public DatabaseRemote(Context context) {
        mDbHelper = new DataBaseHelper(context);
    }

    //open database
    public void openDataBase() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    //close database
    public void closeDataBase() throws SQLException {
        mDatabase.close();
    }

    //insert data into table department
    public long insertDepartment(Department department) {
        long result = -1;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_DEPARTMENT, department.getName());
        contentValues.put(COLUMN_IMAGE_DEPARTMENT, department.getImage());
        try {
            result = mDatabase.insertOrThrow(TABLE_DEPARTMENT, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // get list of department
    public List<Department> getDepartmentList() {
        String quey = "select * from " + TABLE_DEPARTMENT;
        int id;
        String name, image;
        Department de;
        Cursor cursor = null;
        cursor = mDatabase.rawQuery(quey, null);
        List<Department> departmentList = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                departmentList.add(new Department(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return departmentList;
    }

    //find department by name
    public int getIdDepartment(String name) {
        int i = -1;
        String query = (COLUMN_NAME_DEPARTMENT + " like '%" + name + "%'");
        Cursor cursor = null;
        cursor = mDatabase.query(true, TABLE_DEPARTMENT, null, query, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            i = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DEPARTMENT));
        }
        cursor.close();
        return i;
    }

    //get name department from id
    public String getNameDepartment(int id) {
        String result = null;
        String query = COLUMN_ID_DEPARTMENT + " = " + id;
        Cursor cursor = mDatabase.query(true, TABLE_DEPARTMENT, null, query, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DEPARTMENT));
        }
        cursor.close();
        return result;
    }

    //delete a record in department
    public boolean deleteDepartment(int id) {
        try {
            mDatabase.delete(TABLE_DEPARTMENT, COLUMN_ID_DEPARTMENT + " = " + id, null);
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
            result = mDatabase.insertOrThrow(TABLE_STAFF, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Staff> getListStaff(int start, int departmentId) {
        List<Staff> staffList = new ArrayList<>();
        String query = "select * from " + TABLE_STAFF + " where " + COLUMN_ID_DEPARTMENT + " = " + departmentId
                + " ORDER BY " + COLUMN_NAME_STAFF + " LIMIT " + Constant.STAFF_PER_PAGE + " " + "OFFSET " + start;
        Cursor cursor = null;
        cursor = mDatabase.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                staffList.add(new Staff(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return staffList;
    }

    public int updateStaff(int id, Staff staff) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_STAFF, staff.getName());
        values.put(COLUMN_AVATAR, staff.getImageAvatar());
        values.put(COLUMN_ID_DEPARTMENT, staff.getIdDepartment());
        values.put(COLUMN_BIRTHDAY, staff.getBirthday());
        values.put(COLUMN_PHONENUMBER, staff.getPhoneNumber());
        values.put(COLUMN_PLACEOFBIRTH, staff.getPlaceOfBirth());
        values.put(COLUMN_POSITION, staff.getPosition());
        values.put(COLUMN_STATUS, staff.getStatus());
        String[] whereArgs = new String[]{String.valueOf(id)};
        int rowChange = mDatabase.update(TABLE_STAFF, values, COLUMN_ID_STAFF + " = ?", whereArgs);
        return rowChange;
    }

    //get Staff from id
    public Cursor searchStaff(int id) {
        int i = -1;
        String query = COLUMN_ID_STAFF + " = " + id;
        Cursor cursor = null;
        cursor = mDatabase.query(true, TABLE_STAFF, null, query, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public List<Staff> getListStaff(String staffName) {
        List<Staff> staffList = new ArrayList<>();
        String query = "select * from " + TABLE_STAFF + " where " + COLUMN_NAME_STAFF
                + " like '%" + staffName + "%'";
        Cursor cursor = null;
        cursor = mDatabase.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                staffList.add(new Staff(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return staffList;
    }

    public boolean updateStatus(int staffId, int status) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        String whereClause = COLUMN_ID_STAFF + " = " + staffId;
        int result = mDatabase.update(TABLE_STAFF, values, whereClause, null);
        return result > 0;
    }

    public List<Staff> getListStaffByPhoneNumber(String phoneNumber) {
        List<Staff> staffList = new ArrayList<>();
        String query = "select * from " + TABLE_STAFF + " where " + COLUMN_PHONENUMBER + " like '%" + phoneNumber + "%'";
        Cursor cursor = null;
        cursor = mDatabase.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                staffList.add(new Staff(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return staffList;
    }

    public List<Staff> getListStaffByDepartment(String departmentName) {
        List<Staff> staffList = new ArrayList<>();
        String query = "select " + TABLE_STAFF + "." + COLUMN_ID_STAFF + ", "
                + TABLE_DEPARTMENT + "." + COLUMN_NAME_DEPARTMENT + ", "
                + TABLE_STAFF + "." + COLUMN_ID_DEPARTMENT + ", "
                + TABLE_STAFF + "." + COLUMN_STATUS + ", "
                + TABLE_STAFF + "." + COLUMN_POSITION + ", "
                + TABLE_STAFF + "." + COLUMN_NAME_STAFF + ", "
                + TABLE_STAFF + "." + COLUMN_BIRTHDAY + ", "
                + TABLE_STAFF + "." + COLUMN_PLACEOFBIRTH + ", "
                + TABLE_STAFF + "." + COLUMN_PHONENUMBER + ", "
                + TABLE_STAFF + "." + COLUMN_AVATAR
                + " from " + TABLE_STAFF + " INNER JOIN " + TABLE_DEPARTMENT
                + " on " + TABLE_STAFF + "." + COLUMN_ID_DEPARTMENT + " = " + TABLE_DEPARTMENT + "." + COLUMN_ID_DEPARTMENT
                + " where " + TABLE_DEPARTMENT + "." + COLUMN_NAME_DEPARTMENT + " like '%" + departmentName + "%'";

        Cursor cursor = null;
        cursor = mDatabase.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                staffList.add(new Staff(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return staffList;
    }

    //delete a record in staff
    public boolean deleteStaff(int id) {
        try {
            mDatabase.delete(TABLE_STAFF, COLUMN_ID_STAFF + " = " + id, null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
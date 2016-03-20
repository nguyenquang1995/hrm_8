package com.framgia.project1.humanresourcemanagement.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.framgia.project1.humanresourcemanagement.data.model.DBSchemaConstant;

public class DataBaseHelper extends SQLiteOpenHelper implements DBSchemaConstant {
    private static final String DATABASE_NAME = "staffManager.db"; // name of database
    private static final int DATABASE_VERSION = 1; // database version

    //create table department
    private static final String CREATE_TABLE_DEPARTMENT = "create table " + DBSchemaConstant.TABLE_DEPARTMENT + " ( "
            + COLUMN_ID_DEPARTMENT + " integer primary key autoincrement,"
            + COLUMN_NAME_DEPARTMENT + " text,"
            + COLUMN_IMAGE_DEPARTMENT + " text)";

    //create table staff
    private static final String CREATE_TABLE_STAFF = "create table " + DBSchemaConstant.TABLE_STAFF + " ( "
            + COLUMN_ID_STAFF + " integer primary key autoincrement, "
            + COLUMN_NAME_STAFF + " text,"
            + COLUMN_PLACEOFBIRTH + " text,"
            + COLUMN_BIRTHDAY + " text,"
            + COLUMN_PHONENUMBER + " text,"
            + COLUMN_STATUS + " integer,"
            + COLUMN_POSITION + " integer,"
            + COLUMN_AVATAR + " text,"
            + COLUMN_ID_DEPARTMENT + " integer)";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_DEPARTMENT);
        sqLiteDatabase.execSQL(CREATE_TABLE_STAFF);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + DBSchemaConstant.TABLE_DEPARTMENT);
        sqLiteDatabase.execSQL("drop table if exists " + DBSchemaConstant.TABLE_STAFF);
        onCreate(sqLiteDatabase);
    }
}
package com.framgia.project1.humanresourcemanagement.data.model;

import android.database.Cursor;
import com.framgia.project1.humanresourcemanagement.data.local.DataBaseHelper;
/**
 * Created by nguyenxuantung on 11/03/2016.
 */
public class Staff implements DBSchemaConstant {
    private int id;
    private int idDepartment;
    private int status;
    private int position;
    private String name;
    private String birthday;
    private String placeOfBirth;
    private String phoneNumber;
    private String imageAvatar;

    public Staff(int id, int idDepartment, int status, int position, String name, String birthday, String placeOfBirth, String phoneNumber, String imageAvatar) {
        this.id = id;
        this.idDepartment = idDepartment;
        this.status = status;
        this.position = position;
        this.name = name;
        this.birthday = birthday;
        this.placeOfBirth = placeOfBirth;
        this.phoneNumber = phoneNumber;
        this.imageAvatar = imageAvatar;
    }

    public Staff(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_STAFF));
        this.idDepartment = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_STAFF));
        this.status = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_STAFF));
        this.position = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_STAFF));
        this.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STAFF));
        this.birthday = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY));
        this.placeOfBirth = cursor.getString(cursor.getColumnIndex(COLUMN_PLACEOFBIRTH));
        this.phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONENUMBER));
        this.imageAvatar = cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR));
    }

    public int getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(int idDepartment) {
        this.idDepartment = idDepartment;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImageAvatar() {
        return imageAvatar;
    }

    public void setImageAvatar(String imageAvatar) {
        this.imageAvatar = imageAvatar;
    }
}

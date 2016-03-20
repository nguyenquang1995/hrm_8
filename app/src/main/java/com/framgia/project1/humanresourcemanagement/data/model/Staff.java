package com.framgia.project1.humanresourcemanagement.data.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.project1.humanresourcemanagement.data.local.DataBaseHelper;

public class Staff implements DBSchemaConstant, Parcelable {
    private int mId;
    private int mIdDepartment;
    private int mStatus;
    private int mPosition;
    private String mName;
    private String mBirthday;
    private String mPlaceOfBirth;
    private String mPhonenumber;
    private String mImageAvatar;

    public Staff(int id, int idDepartment, int status, int position, String name,
                 String birthday, String placeOfBirth, String phoneNumber, String imageAvatar) {
        this.mId = id;
        this.mIdDepartment = idDepartment;
        this.mStatus = status;
        this.mPosition = position;
        this.mName = name;
        this.mBirthday = birthday;
        this.mPlaceOfBirth = placeOfBirth;
        this.mPhonenumber = phoneNumber;
        this.mImageAvatar = imageAvatar;
    }

    public Staff(int idDepartment, int status, int position, String name,
                 String birthday, String placeOfBirth, String phoneNumber, String imageAvatar) {
        this.mIdDepartment = idDepartment;
        this.mStatus = status;
        this.mPosition = position;
        this.mName = name;
        this.mBirthday = birthday;
        this.mPlaceOfBirth = placeOfBirth;
        this.mPhonenumber = phoneNumber;
        this.mImageAvatar = imageAvatar;
    }

    public Staff(Cursor cursor) {
        this.mId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_STAFF));
        this.mIdDepartment = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DEPARTMENT));
        this.mStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS));
        this.mPosition = cursor.getInt(cursor.getColumnIndex(COLUMN_POSITION));
        this.mName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STAFF));
        this.mBirthday = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY));
        this.mPlaceOfBirth = cursor.getString(cursor.getColumnIndex(COLUMN_PLACEOFBIRTH));
        this.mPhonenumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONENUMBER));
        this.mImageAvatar = cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR));
    }

    public int getIdDepartment() {
        return mIdDepartment;
    }

    public void setIdDepartment(int idDepartment) {
        this.mIdDepartment = idDepartment;
    }

    public String getBirthday() {
        return mBirthday;
    }

    public void setBirthday(String birthday) {
        this.mBirthday = birthday;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPhoneNumber() {
        return mPhonenumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhonenumber = phoneNumber;
    }

    public String getPlaceOfBirth() {
        return mPlaceOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.mPlaceOfBirth = placeOfBirth;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public String getImageAvatar() {
        return mImageAvatar;
    }

    public void setImageAvatar(String imageAvatar) {
        this.mImageAvatar = imageAvatar;
    }

    protected Staff(Parcel in) {
        mId = in.readInt();
        mIdDepartment = in.readInt();
        mStatus = in.readInt();
        mPosition = in.readInt();
        mName = in.readString();
        mBirthday = in.readString();
        mPlaceOfBirth = in.readString();
        mPhonenumber = in.readString();
        mImageAvatar = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mIdDepartment);
        dest.writeInt(mStatus);
        dest.writeInt(mPosition);
        dest.writeString(mName);
        dest.writeString(mBirthday);
        dest.writeString(mPlaceOfBirth);
        dest.writeString(mPhonenumber);
        dest.writeString(mImageAvatar);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Staff> CREATOR = new Parcelable.Creator<Staff>() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };
}
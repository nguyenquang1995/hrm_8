package com.framgia.project1.humanresourcemanagement.data.model;

/**
 * Created by nguyenxuantung on 14/03/2016.
 */
public interface DBSchemaConstant {

    // columns of tblDepartment
    public static final String COLUMN_ID_DEPARTMENT = "idDep";
    public static final String COLUMN_NAME_DEPARTMENT = "nameDep";
    public static final String COLUMN_IMAGE_DEPARTMENT = "imageDep";

    //columns of tblStaff
    public static final String COLUMN_ID_STAFF = "idSta";
    public static final String COLUMN_NAME_STAFF = "nameSta";
    public static final String COLUMN_PLACEOFBIRTH = "placeOfBirth";
    public static final String COLUMN_BIRTHDAY = "birthDay";
    public static final String COLUMN_PHONENUMBER = "phoneNumber";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_AVATAR = "avatar";

    // department table's name
    public static final String TABLE_DEPARTMENT = "tblDepartment";

    // staff table's name
    public static final String TABLE_STAFF = "tblStaff";
}
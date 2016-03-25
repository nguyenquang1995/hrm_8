package com.framgia.project1.humanresourcemanagement.data.model;

import android.content.Context;

import com.framgia.project1.humanresourcemanagement.data.remote.DatabaseRemote;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    private Context mContext;
    private DatabaseRemote mDatabaseRemote;
    private List<Department> mListDepartment;
    public static List createDummyDepartmentData() {
        List<Department> listDepartment = new ArrayList<Department>();
        listDepartment.add(new Department("Trainning", 1, ""));
        listDepartment.add(new Department("Singapore", 2, ""));
        listDepartment.add(new Department("crew", 3, ""));
        listDepartment.add(new Department("Asia", 4, ""));
        listDepartment.add(new Department("HR Department", 5, ""));
        return listDepartment;
    }
    public DepartmentDAO(Context context) {
        this.mContext = context;
    }
    public List<Department> getListDepartment() {
        mDatabaseRemote = new DatabaseRemote(mContext);
        try {
            mDatabaseRemote.openDataBase();
            if (mDatabaseRemote.getDepartmentList().size() <= 0) {
                mListDepartment = DepartmentDAO.createDummyDepartmentData();
                int listDepartmentSize = mListDepartment.size();
                for (int i = 0; i < listDepartmentSize; i++) {
                    mDatabaseRemote.insertDepartment(mListDepartment.get(i));
                }
            } else
                mListDepartment = mDatabaseRemote.getDepartmentList();
            mDatabaseRemote.closeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mListDepartment;
    }

    public void creatDummyStaffIfStaffIsEmpty(int departMentId) {
        mDatabaseRemote = new DatabaseRemote(mContext);
        try {
            mDatabaseRemote.openDataBase();
            if (mDatabaseRemote.getListStaff(-1, departMentId).size() <= 0) {
                for (int i = 0; i < 30; i++) {
                    mDatabaseRemote.insertStaff(new Staff(4, departMentId, 2, 1, "Nguyen Thi Tuyet Nhung", "15-5-1994", "Nam Dinh", "0987402568", Constant.NOAVARTAR));
                }
                mDatabaseRemote.insertStaff(new Staff(1, departMentId, 0, 0, "Nguyễn Văn Quang", "2-9-1995", "Hà Nội", "0915057307", Constant.NOAVARTAR));
                mDatabaseRemote.insertStaff(new Staff(2, departMentId, 2, 1, "Trần Mạnh Tiến", "12-12-1995", "Đông Anh", "0146753424646", Constant.NOAVARTAR));
                mDatabaseRemote.insertStaff(new Staff(3, departMentId, 2, 2, "Trần Thị Hồng Thủy", "27-7-1994", "Nam Định", "07953854548", Constant.NOAVARTAR));
            }
            mDatabaseRemote.closeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
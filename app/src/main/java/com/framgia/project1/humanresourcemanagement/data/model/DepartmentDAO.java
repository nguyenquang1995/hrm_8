package com.framgia.project1.humanresourcemanagement.data.model;

import android.content.Context;
import com.framgia.project1.humanresourcemanagement.data.remote.DatabaseRemote;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    private Context context;
    private DatabaseRemote mDatabaseRemote;
    private List<Department> listDepartment;
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
        this.context = context;
    }
    public List<Department> getListDepartment() {
        mDatabaseRemote = new DatabaseRemote(context);
        try {
            mDatabaseRemote.openDataBase();
            if (mDatabaseRemote.getDepartmentList().size() < 0) {
                listDepartment = DepartmentDAO.createDummyDepartmentData();
                int listDepartmentSize = listDepartment.size();
                for (int i = 0; i < listDepartmentSize; i++) {
                    mDatabaseRemote.insertDepartment(listDepartment.get(i));
                }
            } else
                listDepartment = mDatabaseRemote.getDepartmentList();
            mDatabaseRemote.closeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listDepartment;
    }
}
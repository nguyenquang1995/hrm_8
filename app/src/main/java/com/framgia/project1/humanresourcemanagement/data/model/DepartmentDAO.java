package com.framgia.project1.humanresourcemanagement.data.model;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    private List<Department> listDepartment;

    public static List createDummyDepartmentData() {
        List<Department> listDepartment = new ArrayList<Department>();
        listDepartment.add(new Department("Trainning", 1, ""));
        listDepartment.add(new Department("Singapore", 2, ""));
        listDepartment.add(new Department("crew", 3, ""));
        listDepartment.add(new Department("Trainning", 4, ""));
        listDepartment.add(new Department("Singapore", 5, ""));
        listDepartment.add(new Department("crew", 6, ""));
        listDepartment.add(new Department("Trainning", 7, ""));
        listDepartment.add(new Department("Singapore", 8, ""));
        listDepartment.add(new Department("crew", 9, ""));
        return listDepartment;
    }
}

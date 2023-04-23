package com.service;

import java.util.Set;

public class DepartmentPrinter {
    public static void printAllDeptInfo(Company company) {
        System.out.println("==================================");

        for (Department dept : company.getDepartments()) {
            System.out.println(String.format("%s, %d", dept.getName(), dept.getTotal()));
            printAllSub(dept, 1);
        }

        System.out.println("==================================");
    }

    private static void printAllSub(Department department, int depth) {
        if (!department.getSubDepartments().isEmpty()) {
            for (Department subDept : department.getSubDepartments()) {

                String name = subDept.getName();
                long total = subDept.getTotal();
                System.out.println(String.format("%s└─ %s, %d", "  ".repeat(depth), name, total));

                printAllSub(subDept, depth + 1);
            }
        }
    }
}

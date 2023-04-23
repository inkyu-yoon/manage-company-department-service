package com.service;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Department {
    private Set<Department> subDepartments = new HashSet<>();
    private Department parentDept;
    private String name;
    private long headCount;

    public Department(String name, long headCount) {
        this.name = name;
        this.headCount = headCount;
    }

    public static Department of(String name, long headCount) {
        return new Department(name, headCount);
    }

    // 하위부서 목록에 하위부서를 등록하고, 하위부서는 상위부서에 해당 객체를 등록
    public void makeRelation(Department subDept) {
        this.subDepartments.add(subDept);
        subDept.setParentDept(this);
    }

    // 상위 부서 등록
    public void setParentDept(Department parentDept) {
        this.parentDept = parentDept;
    }

    // 상위 부서가 존재한다면, 상위부서의 하위부서 목록에서 해당 부서를 제외 & 해당 부서에서 상위 부서를 null로 변경
    public void removeDeptRelation() {
        if (this.parentDept != null) {
            this.parentDept.removeSubDept(this);
            this.parentDept = null;
        }
    }
    // 하위 부서 목록에서 해당 하위부서 제거
    private void removeSubDept(Department subDept) {
        this.subDepartments.remove(subDept);
    }

    // 하위 부서들 목록에서 해당 name에 해당하는 부서를 찾을때까지 재귀
    public Department findSubDept(String name) {
            if (this.name.equals(name)) {
                return this;
            } else {
                for (Department subDept : this.subDepartments) {
                    Department foundDept = subDept.findSubDept(name);
                    if (foundDept != null) {
                        return foundDept;
                    }
                }
            }
            return null;
    }

    // 하위 부서들을 탐색하면서 인원수 합 구하기
    public long getTotal() {
        long total = headCount;

        if (!subDepartments.isEmpty()) {
            for (Department subDepartment : subDepartments) {
                total += subDepartment.getTotal();
            }
        }

        return total;
    }

    // 최상위 부서인지 확인
    public boolean isTopDept() {
        return this.parentDept == null;
    }
}

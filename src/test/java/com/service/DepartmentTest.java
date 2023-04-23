package com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DepartmentTest {

    Company company;

    @BeforeEach
    public void setUp() {
        company = new Company(new HashSet<>());
    }

    @Nested
    @DisplayName("해당 부서와 하위부서 내의 모든 직원수를 합한 수를 제공하는지 테스트")
    class GetTotalCount {

        @Test
        @DisplayName("상위 부서에 인원수 총합을 출력하는지 테스트")
        void success(){
            //given
            company.saveDept(new String[]{"A","10"});
            company.saveDept(new String[]{"B","10"});
            company.saveDept(new String[]{"C","10"});

            company.setRelation(new String[]{"A","B"});
            company.setRelation(new String[]{"B","C"});

            //then
            Set<Department> departments = company.getDepartments();
            assertThat(1).isEqualTo(departments.size());

            Department department = departments.iterator().next();
            assertThat(30).isEqualTo(department.getTotal());
            assertThat(10).isEqualTo(department.getHeadCount());

        }
    }
}
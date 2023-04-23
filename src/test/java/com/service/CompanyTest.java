package com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    Company company;

    @BeforeEach
    public void setUp() {
        company = new Company(new HashSet<>());
    }

    @Nested
    @DisplayName("부서 정보 저장 테스트")
    class SaveDeptTest {

        private static Stream<Arguments> testCases() {
            return Stream.of(
                    Arguments.of("A", "100"),
                    Arguments.of("B", "20")
            );
        }

        @ParameterizedTest
        @MethodSource("testCases")
        @DisplayName("성공 테스트")
        void success(String name, String headCount){
            //given
            String[] info = {name, headCount};

            //when
            assertDoesNotThrow(() -> company.saveDept(info));

            //then
            Set<Department> departments = company.getDepartments();
            assertThat(1).isEqualTo(departments.size());

        }

        @ParameterizedTest
        @ValueSource(strings = {"a", "As", "1as"})
        @DisplayName("실패 테스트 (부서명이 영어 대문자가 아닌 경우)")
        void error1(String name){
            //given
            String headCount = "100";

            //when
            String[] info = {name, headCount};

            //then
            CustomException customException = assertThrows(CustomException.class, () -> company.saveDept(info));
            assertThat(customException.getMessage()).isEqualTo("부서명은 영문자 대문자만 가능합니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"100A", "a", "10.0"})
        @DisplayName("실패 테스트 (인원수가 숫자로 입력되지 않은 경우)")
        void error2(String headCount){
            //given
            String name = "A";

            //when
            String[] info = {name, headCount};

            //then
            CustomException customException = assertThrows(CustomException.class, () -> company.saveDept(info));
            assertThat(customException.getMessage()).isEqualTo("인원수는 숫자로만 입력해주세요.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"-10", "1001"})
        @DisplayName("실패 테스트 (인원수가 0~1000 범위 숫자로 입력되지 않은 경우)")
        void error3(String headCount){
            //given
            String name = "A";

            //when
            String[] info = {name, headCount};

            //then
            CustomException customException = assertThrows(CustomException.class, () -> company.saveDept(info));
            assertThat(customException.getMessage()).isEqualTo("인원수는 최소 0명에서 1000명의 범위로 입력해주세요.");
        }

        @Test
        @DisplayName("실패 테스트 (이미 등록된 부서명인 경우)")
        void error4(){
            //given
            String name = "A";
            String headCount = "100";
            String[] info = {name, headCount};
            company.saveDept(info);

            //when
            String otherName = name;

            //then
            CustomException customException = assertThrows(CustomException.class, () -> company.saveDept(new String[]{otherName, headCount}));
            assertThat(customException.getMessage()).isEqualTo("이미 등록되어있는 부서명입니다.");
        }
    }

    @Nested
    @DisplayName("부서 구성도 입력 테스트")
    class SetRelation{

        private static Stream<Arguments> testCases() {
            return Stream.of(
                    Arguments.of("A", "B"),
                    Arguments.of("B", "C")
            );
        }

        @ParameterizedTest
        @MethodSource("testCases")
        @DisplayName("성공 테스트")
        void success(String parent, String sub){
            //given
            company.saveDept(new String[]{parent, "10"});
            company.saveDept(new String[]{sub, "10"});

            String[] info = {parent, sub};

            //when
            assertDoesNotThrow(() ->  company.setRelation(info));

            //then
            Set<Department> departments = company.getDepartments();
            assertThat(1).isEqualTo(departments.size());

            Department department = departments.iterator().next();
            assertThat(parent).isEqualTo(department.getName());

            Set<Department> subDepartments = department.getSubDepartments();
            Department subDept = subDepartments.iterator().next();
            assertThat(sub).isEqualTo(subDept.getName());

        }

        private static Stream<Arguments> testCases2() {
            return Stream.of(
                    Arguments.of("A", "A"),
                    Arguments.of("B", "B")
            );
        }

        @ParameterizedTest
        @MethodSource("testCases2")
        @DisplayName("실패 테스트 (상위 부서와 하위 부서 이름을 동일하게 요청하는 경우)")
        void error(String parent, String sub){
            //given
            String[] info = {parent, sub};

            //then
            CustomException customException = assertThrows(CustomException.class, () -> company.setRelation(info));
            assertThat(customException.getMessage()).isEqualTo("상위 부서와 하위 부서 이름은 동일할 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"B", "C"})
        @DisplayName("실패 테스트 (존재하지 않는 부서를 요청하는 경우)")
        void error2(String sub){
            //given
            String parent = "A";
            company.saveDept(new String[]{parent, "10"});
            String[] info = {parent, sub};

            //then
            CustomException customException = assertThrows(CustomException.class, () -> company.setRelation(info));
            assertThat(customException.getMessage()).isEqualTo(String.format("%s 는 존재하지 않는 부서명 입니다.", sub));
        }


    }
}
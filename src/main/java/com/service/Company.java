package com.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;

@Getter
@AllArgsConstructor
public class Company {
    private final Set<Department> departments;

    // 부서 정보 저장 메서드
    public void saveDept(String[] info) {
        String name = nameValid(info);
        long headCount = headCountValid(info);

        Department department = Department.of(name, headCount);

        // 등록하려는 부서 이름으로, 이미 등록된 부서가 없는 경우 등록 가능
        if (getDept(name).isEmpty()) {
            this.departments.add(department);
        } else {
            throw new CustomException("이미 등록되어있는 부서명입니다.");
        }
    }

    // 부서명이 영문자 대문자로만 이루어져 있는지 유효성 확인
    public String nameValid(String[] info) {
        String name = info[0];
        for (int i = 0; i < name.length(); i++) {
            char chr = name.charAt(i);
            if ('A' > chr || chr > 'Z') {
                throw new CustomException("부서명은 영문자 대문자만 가능합니다.");
            }
        }
        return name;
    }

    // 인원수가 숫자이고, 0~1000 범위 숫자인지 유효성 확인
    public long headCountValid(String[] info) {

        long headCount = 0;
        try {
            headCount = Long.parseLong(info[1]);
        } catch (NumberFormatException e) {
            throw new CustomException("인원수는 숫자로만 입력해주세요.");
        }

        if (headCount < 0 || headCount > 1000) {
            throw new CustomException("인원수는 최소 0명에서 1000명의 범위로 입력해주세요.");
        }
        return headCount;
    }

    // 부서 구성도 입력 시, 관계 설정하는 메서드
    public void setRelation(String[] info) {
        String parent = info[0];
        String sub = info[1];

        // 상위 부서 이름과 하위 부서 이름이 동일한 경우 예외처리
        if (parent.equals(sub)) {
            throw new CustomException("상위 부서와 하위 부서 이름은 동일할 수 없습니다.");
        }

        // 부서 구성도 입력 시, 존재하는 부서 2개로 구성도 입력 시
        if (!parent.equals("*")) {
            this.updateRelation(parent, sub);

            // *>A 와 같이 최상위로 부서를 명시해서 구성도 입력 시
        } else {
            this.updateRelation(sub);
        }
    }

    public void updateRelation(String parent, String sub) {

        // 입력한 부서 이름에 해당하는 부서를 찾을 수 없는 경우 예외 처리
        Department parentDept = getDept(parent)
                .orElseThrow(() -> new CustomException(String.format("%s 는 존재하지 않는 부서명 입니다.", parent)));
        Department subDept = getDept(sub)
                .orElseThrow(() -> new CustomException(String.format("%s 는 존재하지 않는 부서명 입니다.", sub)));

        // 하위 부서에 상위 부서가 입력되어 있는 경우, 이동할 하위부서는 상위부서를 삭제,기존 상위부서에서 역시 이동할 하위부서를 삭제
        if (!subDept.isTopDept()) {
            subDept.removeDeptRelation();

            // 상위 부서가 없는 경우, 이동하기 전, 최 상위 부서(company 클래스의 departments)에서만 제거하면 됨
        } else {
            departments.remove(subDept);
        }

        // 상위 부서 · 하위 부서 연결
        parentDept.makeRelation(subDept);
    }

    // 특정 부서를 최 상위 부서(*)로 이동하는 경우
    public void updateRelation(String sub) {
        Department department = getDept(sub)
                .orElseThrow(() -> new CustomException(String.format("%s 는 존재하지 않는 부서명 입니다.", sub)));

        department.removeDeptRelation();

        this.departments.add(department);
    }

    // 최상위 부서부터, 탐색하여 하위 부서를 모두 탐색하는 재귀 함수
    public Optional<Department> getDept(String name) {
        for (Department dept : departments) {
            if (dept.getName().equals(name)) {

                return Optional.of(dept);

            } else {
                Department subDept = dept.findSubDept(name);

                if (subDept != null) {
                    return Optional.of(subDept);
                }

            }
        }
        return Optional.empty();
    }

}

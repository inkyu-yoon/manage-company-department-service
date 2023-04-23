package com.service;

import java.util.HashSet;

public class ManageService {
    private Company company;
    void run() {
        company = new Company(new HashSet<>());
        System.out.println("회사 조직(부서) 인원수 파악 서비스 실행");
        boolean power = true;

        while (power) {
            System.out.println("\n원하는 기능 번호를 입력하세요.");
            System.out.println("1. 부서 인원 정보 입력 | 2. 부서 구성도 입력 | 3. 부서 별 인원 파악 | 4. 프로그램 종료 : ");


            int num;

            try {
                num = Integer.parseInt(Console.readLine());
            } catch (NumberFormatException e) {
                System.out.println("1~4 번호로 입력해주세요.");
                continue;
            }

            if (num == 1) {
                System.out.println("[부서명],[인원] 으로 부서 인원 정보를 입력해주세요");

                try {
                    saveDeptInfo();
                } catch (CustomException e) {
                    System.out.println(e.getMessage());
                }

            } else if (num == 2) {
                System.out.println("[상위 부서명]>[하위 부서명] 으로 부서 구성도를 입력해주세요");
                try {
                    setDeptRelation();
                } catch (CustomException e) {
                    System.out.println(e.getMessage());
                }

            } else if (num == 3) {
                DepartmentPrinter.printAllDeptInfo(company);

            } else if (num == 4) {

                System.out.println("프로그램을 종료합니다.");
                power = false;

            } else {
                System.out.println("존재하지 않는 기능입니다. 1~4번 중에 하나로 다시 입력해주세요.");
            }

        }
    }

    private void setDeptRelation() {
        String[] info = split(Console.readLine(), ">");
        company.setRelation(info);
    }

    private void saveDeptInfo() {
        String[] info = split(Console.readLine(), ",");
        company.saveDept(info);
    }

    public String[] split(String input, String delimiter) throws IllegalArgumentException {
        String[] splited = input.split(delimiter);
        if (splited.length != 2 || splited[0].trim().isEmpty() || splited[1].trim().isEmpty()) {
            throw new CustomException("입력이 잘못되었습니다. 형식대로 입력했는지 확인해주세요.");
        }
        return new String[]{splited[0].trim(), splited[1].trim()};
    }

}

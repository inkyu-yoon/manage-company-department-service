package com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ManageServiceTest {

    private ManageService manageService;

    @BeforeEach
    public void setUp() {
        manageService = new ManageService();
    }

    @Nested
    @DisplayName("split 테스트")
    class SplitMethodTest {

        @Test
        @DisplayName("성공 테스트")
        void success(){
            //given
            String input = "A,12";
            String delimiter = ",";

            //when
            String[] split = manageService.split(input, delimiter);

            //then
            assertThat(split[0]).isEqualTo("A");
            assertThat(split[1]).isEqualTo("12");
        }

        @Test
        @DisplayName("성공 테스트 (trim이 잘 되는지 확인)")
        void success2(){
            //given
            String input = "A,     12   ";
            String delimiter = ",";

            //when
            String[] split = manageService.split(input, delimiter);

            //then
            assertThat(split[0]).isEqualTo("A");
            assertThat(split[1]).isEqualTo("12");
        }

        @Test
        @DisplayName("실패 테스트 (구분자가 올바르게 주어지지 않는 경우)")
        void error1(){
            //given
            String input = "A,12";

            //when
            String delimiter = ">";

            //then
            CustomException customException = assertThrows(CustomException.class, () -> manageService.split(input, delimiter));
            assertThat(customException.getMessage()).isEqualTo("입력이 잘못되었습니다. 형식대로 입력했는지 확인해주세요.");
        }

        @Test
        @DisplayName("실패 테스트 (split한 결과물이 2개가 아닌 3개인 경우)")
        void error2(){
            //given
            String delimiter = ",";

            //when
            String input = "A,12,123";


            //then
            CustomException customException = assertThrows(CustomException.class, () -> manageService.split(input, delimiter));
            assertThat(customException.getMessage()).isEqualTo("입력이 잘못되었습니다. 형식대로 입력했는지 확인해주세요.");
        }

        @Test
        @DisplayName("실패 테스트 (구분자 뒷 문자열이 공백으로 주어지는 경우)")
        void error3(){
            //given
            String delimiter = ",";

            //when
            String input = "A, ";


            //then
            CustomException customException = assertThrows(CustomException.class, () -> manageService.split(input, delimiter));
            assertThat(customException.getMessage()).isEqualTo("입력이 잘못되었습니다. 형식대로 입력했는지 확인해주세요.");
        }

        @Test
        @DisplayName("실패 테스트 (구분자 앞 문자열이 공백으로 주어지는 경우)")
        void error4(){
            //given
            String delimiter = ",";

            //when
            String input = " ,1";


            //then
            CustomException customException = assertThrows(CustomException.class, () -> manageService.split(input, delimiter));
            assertThat(customException.getMessage()).isEqualTo("입력이 잘못되었습니다. 형식대로 입력했는지 확인해주세요.");
        }
    }
}
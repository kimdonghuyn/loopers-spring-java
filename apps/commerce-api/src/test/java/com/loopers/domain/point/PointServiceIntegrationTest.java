package com.loopers.domain.point;

import com.loopers.application.user.UserCriteria;
import com.loopers.application.user.UserFacade;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserService;
import com.loopers.support.enums.Gender;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointServiceIntegrationTest {
    /**
     * 🔗 통합 테스트
     * <p>
     * 포인트 조회
     * - [x]  해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.
     * - [x]  해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
     * <p>
     * 포인트 충전
     * - [x] 존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.
     */

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private PointService pointService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.")
    @Test
    void returnsPoints_whenGetPointsIsSuccessful() {
        // arrange
        UserCriteria.SignUp userCriteria = new UserCriteria.SignUp(
                "loopers123",
                "hyun",
                "loopers123@naver.com",
                "2002-10-10",
                Gender.M
        );

        userFacade.signUp(userCriteria);

        //act
        PointInfo pointInfo = pointService.get("loopers123");

        //assert
        assertAll(
                () -> assertThat(pointInfo.loginId().getLoginId()).isEqualTo("loopers123"),
                () -> assertThat(pointInfo.amount()).isEqualTo(100)
        );
    }

    @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
    @Test
    void returnsNotFound_whenUserDoesNotExist() {
        // arrange
        UserCommand.SignUp user = new UserCommand.SignUp(
                "loopers123",
                "hyun",
                "loopers@naver.com",
                "2002-10-10",
                Gender.F
        );
        userService.signUp(user);

        //act
        PointInfo pointInfo = pointService.get("roopers123");

        // assert
        assertThat(pointInfo).isNull();

    }

    @DisplayName("존재하지 않는 유저 ID 로 충전을 시도한 경우, `404 NOT FOUND`를 반환한다.")
    @Test
    void fail_whenChargePointForNonExistentUser() {
        // arrange
        UserCommand.SignUp user = new UserCommand.SignUp(
                "loopers123",
                "hyun",
                "loopers@naver.com",
                "2002-10-10",
                Gender.F
        );
        userService.signUp(user);

        // act
        CoreException exception = assertThrows(CoreException.class, () -> {
            pointService.charge(new PointCommand.Charge("roopers123", 100L));
        });

        // assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }

    @DisplayName("포인트 충전 시, 포인트가 정상적으로 증가한다.")
    @Test
    void chargePointSuccessfully() {
        // arrange
        userService.signUp(
                new UserCommand.SignUp(
                        "loopers123",
                        "hyun",
                        "loopers@naver.com",
                        "2002-10-10",
                        Gender.M
                ));

        pointService.initPoint(new PointCommand.Init("loopers123", 100L));

        // act
        pointService.charge(new PointCommand.Charge("loopers123", 100L));

        // assert
        PointInfo pointInfo = pointService.get("loopers123");
        assertAll(
                () -> assertThat(pointInfo).isNotNull(),
                () -> assertThat(pointInfo.loginId().getLoginId()).isEqualTo("loopers123"),
                () -> assertThat(pointInfo.amount()).isEqualTo(200L) // 초기 100 + 충전 100
        );
    }

    @DisplayName("포인트 사용 시, 포인트가 정상적으로 차감된다.")
    @Test
    void usePointSuccessfully() {
        // arrange
        userService.signUp(
                new UserCommand.SignUp(
                        "loopers123",
                        "hyun",
                        "loopers@naver.com",
                        "2002-10-10",
                        Gender.M
                ));

        pointService.initPoint(new PointCommand.Init("loopers123", 100L));

        // act
        pointService.use(new PointCommand.Use("loopers123", 50L));

        // assert
        PointInfo pointInfo = pointService.get("loopers123");
        assertAll(
                () -> assertThat(pointInfo).isNotNull(),
                () -> assertThat(pointInfo.loginId().getLoginId()).isEqualTo("loopers123"),
                () -> assertThat(pointInfo.amount()).isEqualTo(50L) // 초기 100 - 사용 50
        );
    }
}

package com.loopers.interfaces.api.user;

import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.Gender;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {

    private final UserService userService;

    public UserV1Controller(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    @Override
    public ApiResponse<UserV1Dto.UserResponse> signUp(@RequestBody @Valid UserV1Dto.SignUpRequest signUpRequest) {
        return ApiResponse.success(
                new UserV1Dto.UserResponse(
                        "loopers",
                        "hyun",
                        Gender.F,
                        "loopers@naver.com",
                        "2002-10-10"
                )
        );
    }

    @GetMapping("/me")
    @Override
    public ApiResponse<UserV1Dto.UserResponse> getUserInfo(@RequestHeader("X-USER-ID") String userId) {
        UserV1Dto.UserResponse response = userService.getUserInfo(userId);
        return ApiResponse.success(
                new UserV1Dto.UserResponse(
                        response.userId(),
                        response.name(),
                        response.gender(),
                        response.email(),
                        response.birth()
                )
        );
    }

}

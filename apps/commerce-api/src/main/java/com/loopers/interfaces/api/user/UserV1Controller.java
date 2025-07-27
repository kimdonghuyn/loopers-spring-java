package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {

    private final UserService userService;
    private final UserFacade userFacade;

    public UserV1Controller(UserService userService, UserFacade userFacade) {
        this.userService = userService;
        this.userFacade = userFacade;
    }

    @PostMapping("")
    @Override
    public ApiResponse<UserV1Dto.UserResponse> signUp(@RequestBody @Valid UserV1Dto.SignUpRequest signUpRequest) {
        final UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(
                userFacade.signUp(signUpRequest.toCriteria())
        );

        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    @Override
    public ApiResponse<UserV1Dto.UserResponse> getUserInfo(@RequestHeader("X-USER-ID") String userId) {

        final  UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(
                userFacade.getUserInfo(userId)
        );

        return ApiResponse.success(response);
    }

}

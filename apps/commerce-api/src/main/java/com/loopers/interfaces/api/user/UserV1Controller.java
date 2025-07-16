package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {

    @PostMapping("")
    @Override
    public ApiResponse<UserV1Dto.UserResponse> signUp(@RequestBody @Valid UserV1Dto.SignUpRequest signUpRequest) {
        return null;
    }
}

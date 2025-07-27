package com.loopers.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class LoginId {

    private String loginId;

    public LoginId() {}

    public LoginId(final String loginId) {
        UserValidator.validateUserId(loginId);
        this.loginId = loginId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginId)) return false;
        LoginId loginId1 = (LoginId) o;
        return loginId.equals(loginId1.loginId);
    }

    @Override
    public int hashCode() {
        return loginId.hashCode();
    }
}

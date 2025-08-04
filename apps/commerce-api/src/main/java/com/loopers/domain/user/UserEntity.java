package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import com.loopers.support.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {
    @Embedded
    private LoginId loginId;
    private String name;
    private Email email;
    private Birth birth;
    private Gender gender;

    public UserEntity(LoginId loginId, String name,Email email, Birth birth, Gender gender) {
        UserValidator.validateUserId(loginId.getLoginId());
        UserValidator.validateName(name);
        UserValidator.validateEmail(email.getEmail());
        UserValidator.validateBirth(birth.getBirth());
        UserValidator.validateGender(gender);

        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.gender = gender;
    }
}

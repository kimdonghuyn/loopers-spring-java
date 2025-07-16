package com.loopers.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class UserEntity {
    @Id
    private String userId;
    private String name;
    private String email;
    private String birth;

    public UserEntity(String userId, String name, String email, String birth) {
        UserValidator.validateUserId(userId);
        UserValidator.validateEmail(email);
        UserValidator.validateBirth(birth);

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }
}

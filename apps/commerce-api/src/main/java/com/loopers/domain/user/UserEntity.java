package com.loopers.domain.user;

import com.loopers.support.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private LoginId loginId;
    private String name;
    private Email email;
    private Birth birth;
    private Gender gender;

    public UserEntity(LoginId loginId, String name,Email email, Birth birth, Gender gender) {
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.gender = gender;
    }
}

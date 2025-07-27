package com.loopers.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Email {
    private String email;

    public Email() {}

    public Email(final String email) {
        UserValidator.validateEmail(email);
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email1 = (Email) o;
        return email.equals(email1.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}

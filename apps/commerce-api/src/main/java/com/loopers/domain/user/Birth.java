package com.loopers.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Birth {

    private String birth;

    public Birth() {}

    public Birth(final String birth) {
        UserValidator.validateBirth(birth);
        this.birth = birth;
    }

    public String getBirth() {
        return birth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Birth)) return false;
        Birth birth1 = (Birth) o;
        return birth.equals(birth1.birth);
    }

    @Override
    public int hashCode() {
        return birth.hashCode();
    }
}

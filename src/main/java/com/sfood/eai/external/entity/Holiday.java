package com.sfood.eai.external.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Holiday {

    private LocalDate date;
    private String description;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Holiday holiday = (Holiday) o;
        return Objects.equals(date, holiday.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }
}

package com.example.kitastestukas.Utilities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtilities {
    private static final LocalDate EPOCH = LocalDate.now();
    public static LocalDate fromMonthsSinceEpoch(long months) {
        return EPOCH.plusMonths(months);
    }
}

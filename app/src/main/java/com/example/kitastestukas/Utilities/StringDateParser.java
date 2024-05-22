package com.example.kitastestukas.Utilities;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StringDateParser {
    public static LocalDate parseStringToDate(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
        try {
            return LocalDate.parse(str, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + str);
            return null;
        }
    }
}

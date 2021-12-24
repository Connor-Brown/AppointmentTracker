package com.mutzy.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Constants {

    // web page mappings
    public static final String APPOINTMENTS_PAGE = "appointments";
    public static final String ERROR_PAGE = "error";

    // database text field sizes
    public static final int MAX_APPOINTMENT_DESCRIPTION_LENGTH = 1024;
    public static final int MAX_PERSON_NAME_LENGTH = 255;
    public static final int MAX_PERSON_AFFILIATION_LENGTH = 255;
    public static final int MAX_LOCATION_NAME_LENGTH = 255;
    public static final int MAX_LOCATION_DESCRIPTION_LENGTH = 1024;

    // regex
    public static final String STRING_INPUT_REGEX = "^[a-zA-Z0-9.,?!\\-\\_ ]+$";

    // date time formats
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

}

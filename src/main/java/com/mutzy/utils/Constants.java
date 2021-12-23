package com.mutzy.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Constants {

    public static final int MAX_APPOINTMENT_DESCRIPTION_LENGTH = 1024;
    public static final int MAX_PERSON_NAME_LENGTH = 255;
    public static final int MAX_PERSON_AFFILIATION_LENGTH = 255;
    public static final int MAX_LOCATION_NAME_LENGTH = 255;
    public static final int MAX_LOCATION_DESCRIPTION_LENGTH = 1024;

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

}

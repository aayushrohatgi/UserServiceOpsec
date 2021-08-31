package com.opsec.userservice;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *  Widely used constants across test files
 */
public interface TestConstants {
    public static final String DOB = "07-05-1994";
    public static final String TITLE = "Mr";
    public static final String LAST_NAME = "Rohatgi";
    public static final String FIRST_NAME = "Aayush";
    public static final String USER_ID = "userId";
    public static final String DOB_WRONG = "1994-05-07";
    public static final Date DATE = new GregorianCalendar(1994, Calendar.MAY, 7).getTime();
}

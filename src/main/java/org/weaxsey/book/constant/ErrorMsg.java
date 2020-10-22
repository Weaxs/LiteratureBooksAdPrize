package org.weaxsey.book.constant;

/**
 * @author Weaxs
 */
public class ErrorMsg {

    public static final String BOOKER_YEAR_SMALL_MSG = "The first Booker pize began in 1969.";
    public static final String NOBEL_YEAR_SMALL_MSG = "The first Nobel pize began in 1901.";
    public static final String NOT_AWARDED_MSG = " pize hasn't awarded.";
    public static final String NOT_AWARDED_THE = "The ";
    public static final String BLANK = " ";

    public static String hasNotAwarded(long year, String prizes) {
        return NOT_AWARDED_THE + year + BLANK + prizes + NOT_AWARDED_MSG;
    }

}

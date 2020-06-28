package org.weaxsey.book.prizes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public interface IPrizesService {

    static SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");

    public void getWinner(Calendar calendar);

}

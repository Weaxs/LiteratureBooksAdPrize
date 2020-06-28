package org.weaxsey.book.prizes;

import org.weaxsey.book.domain.BookMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public interface IPrizesService {

    static SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");

    public List<BookMessage> getWinner(Calendar calendar);

}

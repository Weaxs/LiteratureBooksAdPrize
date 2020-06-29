package org.weaxsey.book.prizes.api;

import org.weaxsey.book.domain.BookMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public interface IPrizesService {

    public List<BookMessage> getWinner(Calendar calendar);

}

package org.weaxsey.book.prizes.api;

import org.weaxsey.book.domain.BookMessage;

import java.util.Calendar;
import java.util.List;

public interface IPrizesService {

    List<BookMessage> getWinner(Calendar calendar);

}

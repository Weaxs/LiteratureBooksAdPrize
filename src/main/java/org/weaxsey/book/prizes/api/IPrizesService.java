package org.weaxsey.book.prizes.api;

import org.weaxsey.book.domain.BookMessage;

import java.util.Calendar;
import java.util.List;

/**
 * prize service
 *
 * @author Weaxs
 */
public interface IPrizesService {

    /**
     * get prize winner
     * @param calendar year param
     * @return winner message
     */
    List<BookMessage> getWinner(Calendar calendar);

}

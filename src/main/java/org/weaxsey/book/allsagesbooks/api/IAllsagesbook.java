package org.weaxsey.book.allsagesbooks.api;

import com.alibaba.fastjson.JSONObject;
import org.weaxsey.book.domain.BookMessage;

import java.util.Map;

/**
 *
 * @author Weaxs
 */
public interface IAllsagesbook {

    /**
     * search book in allsagesbook by Request
     * @param book search param
     * @return result
     */
    JSONObject getBookMessageByRequest(BookMessage book);

    /**
     * search book in allsagesbook by HttpClient
     * @param book search param
     * @return result
     */
    JSONObject getBookMessageByHttpClient(BookMessage book);

    /**
     * get allsages book rank monthly
     * @return rank
     */
    Map<Double, String> getRank();

}

package org.weaxsey.book.allsagesbooks.api;

import com.alibaba.fastjson.JSONObject;
import org.weaxsey.book.domain.BookMessage;

import java.util.Map;

public interface IAllsagesbook {

    JSONObject getBookMessageByRequest(BookMessage book);

    JSONObject getBookMessageByHttpClient(BookMessage book);

    Map<Double, String> getRank();

}

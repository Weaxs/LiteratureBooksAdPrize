package org.weaxsey.book.allsagesBooks.api;

import com.alibaba.fastjson.JSONObject;
import org.weaxsey.book.domain.BookMessage;

public interface IAllsagesbook {

    JSONObject getBookMessageByRequest(BookMessage book);

    JSONObject getBookMessageByHttpClient(BookMessage book);

    JSONObject getRank();

}

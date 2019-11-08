package org.weaxsey.allsagesbooks.api;

import com.alibaba.fastjson.JSONObject;
import org.weaxsey.domain.BookMessage;

public interface IAllsagesbook {

    JSONObject getBookMessageByRequest(BookMessage book);

    JSONObject getBookMessageByHttpClient(BookMessage book);

}

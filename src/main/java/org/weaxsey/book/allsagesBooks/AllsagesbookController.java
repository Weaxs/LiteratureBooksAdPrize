package org.weaxsey.book.allsagesBooks;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.weaxsey.book.allsagesBooks.api.IAllsagesbook;
import org.weaxsey.book.domain.BookMessage;

@RestController
@RequestMapping("/allsagesbook")
public class AllsagesbookController {

    @Autowired
    private IAllsagesbook allsagesbook;

    @RequestMapping(value = "/getBooks", method = RequestMethod.POST)
    public JSONObject getBookMessage(BookMessage book) {
        return allsagesbook.getBookMessageByRequest(book);
    }

}

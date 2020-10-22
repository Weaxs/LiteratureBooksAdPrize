package org.weaxsey.book.allsagesbooks;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.weaxsey.book.allsagesbooks.api.IAllsagesbook;
import org.weaxsey.book.domain.BookMessage;

/**
 * allsages book controller
 *
 * @author Weaxs
 */
@RestController
@RequestMapping("/allsagesbook")
public class AllsagesbookController {

    @Autowired
    private IAllsagesbook allsagesbook;

    @PostMapping(value = "/getBooks")
    public JSONObject getBookMessage(BookMessage book) {
        return allsagesbook.getBookMessageByRequest(book);
    }

}

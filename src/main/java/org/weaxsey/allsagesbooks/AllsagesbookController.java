package org.weaxsey.allsagesbooks;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.weaxsey.allsagesbooks.api.IAllsagesbook;
import org.weaxsey.domain.BookMessage;

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

package org.weaxsey;

import com.alibaba.fastjson.JSONObject;
import org.weaxsey.allsagesbooks.Allsagesbook;
import org.weaxsey.domain.BookMessage;

public class Main {

    public static void main(String[] args) {

        Allsagesbook allsagesbook = new Allsagesbook();
        BookMessage book = new BookMessage();
        book.setBookName("月落荒寺");
//        book.setBookName("pride and prejudice");
        JSONObject re = allsagesbook.getBookMessage(book);
        System.out.println(re.toJSONString());

    }

}

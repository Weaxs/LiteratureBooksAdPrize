package org.weaxsey;

import com.alibaba.fastjson.JSONObject;

public class Main {

    public static void main(String[] args) {

        Allsagesbook allsagesbook = new Allsagesbook();
        BookMessage book = new BookMessage();
//        book.setBookName("傲慢与偏见");
        book.setBookName("pride and prejudice");
        JSONObject re = allsagesbook.getBookMessage(book);
        System.out.println(re.toJSONString());

    }

}

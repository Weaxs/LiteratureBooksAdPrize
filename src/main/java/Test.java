import allsagesbook.Allsagesbook;
import allsagesbook.BookMessage;
import com.alibaba.fastjson.JSONObject;

public class Test {

    public static void main(String[] args) {

        Allsagesbook allsagesbook = new Allsagesbook();
        BookMessage book = new BookMessage();
        book.setBookName("傲慢与偏见");
        JSONObject re = allsagesbook.getBookMessage(book);
        System.out.println(re.toJSONString());


    }

}

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.weaxsey.QuickStart;
import org.weaxsey.book.allsagesBooks.api.IAllsagesbook;
import org.weaxsey.book.domain.BookMessage;
import org.weaxsey.redis.RedisClient;

import java.util.Map;

@SpringBootTest(classes = QuickStart.class)
@RunWith(SpringRunner.class)
public class AllsagesbookTest {

    private static final Logger logger = LoggerFactory.getLogger(AllsagesbookTest.class);

    @Autowired
    private IAllsagesbook allsagesbook;
    @Autowired
    private RedisClient<String> redisClient;

    @Test
    public void getBookTest() {
        BookMessage book = new BookMessage();
        book.setBookName("傲慢与偏见");
//        book.setBookName("月落荒寺");
//        book.setBookName("pride and prejudice");
        JSONObject re = allsagesbook.getBookMessageByRequest(book);
        logger.info(re.toJSONString());
    }

    @Test
    public void getRank() {
        Map<Double, String> redisReturn = allsagesbook.getRank();
        logger.info("");
    }
}

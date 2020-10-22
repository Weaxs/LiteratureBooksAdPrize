import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.weaxsey.QuickStart;
import org.weaxsey.book.domain.BookMessage;
import org.weaxsey.book.prizes.api.IPrizesService;
import org.weaxsey.redis.RedisClient;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

@SpringBootTest(classes = QuickStart.class)
@RunWith(SpringRunner.class)
public class PrizesTest {

    private static final Logger logger = LoggerFactory.getLogger(PrizesTest.class);

    @Resource(name = "theBookerPizes")
    private IPrizesService theBookerPizes;
    @Resource(name = "theNobelPizes")
    private IPrizesService theNobelPizes;
    @Autowired
    private RedisClient<String> redisClient;

    @Test
    public void prizesTest() {
//        redisClient.del("theBookerPizes");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,1,1);
//        List<BookMessage> ans = theBookerPizes.getWinner(calendar);
        List<BookMessage> ans = theNobelPizes.getWinner(calendar);
        logger.info(JSON.toJSONString(ans));

    }
}

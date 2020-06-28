import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.weaxsey.QuickStart;
import org.weaxsey.book.prizes.IPrizesService;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest(classes = QuickStart.class)
@RunWith(SpringRunner.class)
public class PrizesTest {

    private static final Logger logger = LoggerFactory.getLogger(PrizesTest.class);

    @Autowired
    private IPrizesService prizesService;

    @Test
    public void prizesTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018,1,1);
        JSONArray arr = prizesService.getWinner(calendar);
        logger.info(arr.toJSONString());

    }
}

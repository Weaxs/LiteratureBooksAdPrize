import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.weaxsey.QuickStart;
import org.weaxsey.traslation.api.ITranslateService;

@SpringBootTest(classes = QuickStart.class)
@RunWith(SpringRunner.class)
public class TranslateTest {

    private static final Logger logger = LoggerFactory.getLogger(TranslateTest.class);

    @Autowired
    private ITranslateService translateService;

    @Test
    public void test() {
        String msg = translateService.translate("傲慢与偏见");
        logger.info("翻译出的信息为：" + msg);
    }

}

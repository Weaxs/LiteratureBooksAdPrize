package org.weaxsey.traslation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.weaxsey.traslation.api.IMultiTranslateService;
import org.weaxsey.traslation.api.ITranslateService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.weaxsey.config.UserDefThreadPoolExecutor.poolExecutor;

@Service
public class MultiTranslateServiceImpl implements IMultiTranslateService {

    private static final Logger logger = LoggerFactory.getLogger(MultiTranslateServiceImpl.class);

//    @Autowired
//    @Qualifier("baidu")
    @Resource(name = "baidu")
    private ITranslateService baiduService;
    @Resource(name = "youdao")
    private ITranslateService youdaoService;
    @Resource(name = "google")
    private ITranslateService googleService;

    @Override
    public String translate4One(String message) {
        String ans = null;
        List<Callable<String>> callables = multiTranslateCallables(message);
        try {
            //invokeAny方法会对线程进行提交
            ans = poolExecutor.invokeAny(callables);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ans;
    }

//    @Override
//    public String[] translate4All(String message) {
//        List<Callable<String>> callables = multiTranslateCallables(message);
//        List<Future<String>> ans = new ArrayList<>();
//        try {
//            ans = poolExecutor.invokeAll(callables);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private List<Callable<String>> multiTranslateCallables(String message) {
        Callable<String> baiduCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return baiduService.translate(message);
            }
        };
        Callable<String> googleCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return googleService.translate(message);
            }
        };
        Callable<String> youdaoCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return youdaoService.translate(message);
            }
        };
        List<Callable<String>> callables = new ArrayList<>();
        callables.add(baiduCallable);
//        callables.add(google);callables.add(youdao);

        return callables;
    }



}

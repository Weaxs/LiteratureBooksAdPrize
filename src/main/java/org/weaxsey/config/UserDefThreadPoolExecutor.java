package org.weaxsey.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UserDefThreadPoolExecutor {

    public final static ThreadPoolExecutor poolExecutor =
            new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() ,
                    Runtime.getRuntime().availableProcessors() * 2, 120L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(10000));;

}

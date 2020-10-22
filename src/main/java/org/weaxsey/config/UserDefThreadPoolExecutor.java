package org.weaxsey.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.defaultThreadFactory;

/**
 * thread pool
 *
 * @author Weaxs
 */
public class UserDefThreadPoolExecutor {

    public final static ThreadPoolExecutor POOL_EXECUTOR =
            new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() ,
                    Runtime.getRuntime().availableProcessors() * 2, 120L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(10000), defaultThreadFactory());;

}

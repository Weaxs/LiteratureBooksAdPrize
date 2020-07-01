package org.weaxsey.book.prizes.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.weaxsey.redis.RedisClient;
import org.weaxsey.remotecall.api.IRemoteCallService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class AbstractPrizesServiceImpl implements IPrizesService {

    protected static SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");

    protected static String bookerUrl = "https://thebookerprizes.com/fiction/";

    protected static String nobelUrl = "https://www.nobelprize.org/prizes/literature/";

    protected static int BOOKER_PRIZES_START_YEAR = 1969;
    protected static int Nobel_PRIZES_START_YEAR = 1901;

    @Autowired
    protected IRemoteCallService remoteCallService;
    @Autowired
    protected RedisClient<String> redisClient;

    protected Boolean isThisYear(String year) {
        return year.equals(yearDateFormat.format(Calendar.getInstance().getTime()));
    }

}

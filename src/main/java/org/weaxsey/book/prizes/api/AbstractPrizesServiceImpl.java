package org.weaxsey.book.prizes.api;

import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.weaxsey.redis.RedisClient;
import org.weaxsey.remotecall.api.IRemoteCallService;

import java.util.Calendar;

import static org.weaxsey.book.constant.CommonConstant.YEAR_DATE_FORMAT;

/**
 *
 *
 * @author Weaxs
 */
public abstract class AbstractPrizesServiceImpl implements IPrizesService {

    @Autowired
    protected IRemoteCallService remoteCallService;
    @Autowired
    protected RedisClient<String> redisClient;

    protected Boolean isThisYear(String year) {
        return year.equals(DateUtils.formatDate(Calendar.getInstance().getTime(), YEAR_DATE_FORMAT));
    }

}

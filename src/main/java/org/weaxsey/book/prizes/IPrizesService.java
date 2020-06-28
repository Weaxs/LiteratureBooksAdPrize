package org.weaxsey.book.prizes;

import com.alibaba.fastjson.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public interface IPrizesService {

    static SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");

    public JSONArray getWinner(Calendar calendar);

}

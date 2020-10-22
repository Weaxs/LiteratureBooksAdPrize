package org.weaxsey.traslation;

import org.springframework.stereotype.Service;
import org.weaxsey.traslation.api.ITranslateService;

/**
 * YouDao Translate
 *
 * @author Weaxs
 */
@Service("youdao")
public class YoudaoTranslateServiceImpl implements ITranslateService {

    private static String youdaoTranslateUrl = "https://openapi.youdao.com/api";

    @Override
    public String translate(String message) {
        return null;
    }

}

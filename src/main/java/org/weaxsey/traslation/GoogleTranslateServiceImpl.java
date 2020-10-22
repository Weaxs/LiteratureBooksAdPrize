package org.weaxsey.traslation;

import org.springframework.stereotype.Service;
import org.weaxsey.traslation.api.ITranslateService;

/**
 * Google Translate
 *
 * @author Weaxs
 */
@Service("google")
public class GoogleTranslateServiceImpl implements ITranslateService {
    @Override
    public String translate(String message) {
        return null;
    }
}

package org.weaxsey.traslation;

import org.springframework.stereotype.Service;
import org.weaxsey.traslation.api.ITranslateService;

@Service("google")
public class GoogleTranslateServiceImpl implements ITranslateService {
    @Override
    public String translate(String message) {
        return null;
    }
}

package org.weaxsey.traslation.api;

import org.weaxsey.traslation.model.TranslateLanguage;

/**
 * translate service
 *
 * @author Weaxs
 */
public interface ITranslateService {

    /**
     * translate, return the fastest result
     * @param message text
     * @return translate result
     */
    String translate(String message);

    /**
     * translate, return the fastest result
     * @param message text
     * @return translate result
     */
    String translate(String message, TranslateLanguage from, TranslateLanguage to);

}

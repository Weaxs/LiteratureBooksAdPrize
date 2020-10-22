package org.weaxsey.traslation.api;

/**
 * translate service
 *
 * @author Weaxs
 */
public interface IMultiTranslateService {

    /**
     * translate, return the fastest result
     * @param message text
     * @return translate result
     */
    String translate(String message);

}

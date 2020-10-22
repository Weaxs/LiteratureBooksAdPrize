package org.weaxsey.traslation.api;

/**
 * translate
 *
 * @author Weaxs
 */
public interface ITranslateService {

    /**
     * specific translate request
     * @param message text
     * @return translate result
     */
    String translate(String message);

}

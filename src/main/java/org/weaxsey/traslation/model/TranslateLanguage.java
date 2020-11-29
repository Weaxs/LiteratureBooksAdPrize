package org.weaxsey.traslation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Weaxs
 */
public enum  TranslateLanguage {

    /**
     * auto
     */
    auto("auto"),

    /**
     * en
     */
    en("en"),

    /**
     * jp
     */
    jp("jp"),

    /**
     * zh-cn
     */
    zh_cn("zh-cn");

    TranslateLanguage(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

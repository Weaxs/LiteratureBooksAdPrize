package org.weaxsey.traslation.model;

/**
 * @author Weaxs
 */
public class TranslateRequest {

    private String text;
    private TranslateType type;
    private TranslateLanguage from;
    private TranslateLanguage to;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TranslateType getType() {
        return type;
    }

    public void setType(TranslateType type) {
        this.type = type;
    }

    public TranslateLanguage getFrom() {
        return from;
    }

    public void setFrom(TranslateLanguage from) {
        this.from = from;
    }

    public TranslateLanguage getTo() {
        return to;
    }

    public void setTo(TranslateLanguage to) {
        this.to = to;
    }
}

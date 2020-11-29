package org.weaxsey.traslation.model;

/**
 * @author Weaxs
 */
public class TranslateResponse {

    private int status;
    private String msg;
    private TranslateMsg result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TranslateMsg getResult() {
        return result;
    }

    public void setResult(TranslateMsg result) {
        this.result = result;
    }

    public static class TranslateMsg {
        private String text;
        private String result;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

}

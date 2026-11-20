package com.sqx.common.eum;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/5/6 11:02
 */
public enum StringEnum {
    CARD_STYLE_DEFAULT("default","默认样式"),
    ;

    private String code;
    private String msg;

    StringEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

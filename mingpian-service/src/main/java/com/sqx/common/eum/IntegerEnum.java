package com.sqx.common.eum;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/5/6 11:00
 */
public enum IntegerEnum {
    CARD_STATUS_N(0,"不展示"),
    CARD_STATUS_Y(1,"展示"),
    CARE_AUDIT_ING(0,"待审核"),
    CARE_AUDIT_PASS(1,"审核通过"),
    CARE_AUDIT_FAILED(2,"审核未通过"),
    //成员状态 0申请中 2加入 3退出
    COM_STAFF_STATUSE_ING(0,"申请中"),
    COM_STAFF_STATUSE_ED(1,"已加入"),
    COM_STAFF_STATUSE_OUT(2,"退出"),
    //成员来源 0自主申请 1公司邀请 2公司录入
    STAFF_SOURCE_APPLY(0,"申请"),
    STAFF_SOURCE_COM_INVITE(1,"公司邀请"),
    STAFF_SOURCE_COM_INSERT(2,"公司录入"),
    ;

    private Integer code;
    private String msg;

    IntegerEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}


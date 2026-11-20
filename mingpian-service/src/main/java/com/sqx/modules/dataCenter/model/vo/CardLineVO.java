package com.sqx.modules.dataCenter.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author chengming
 * @since 2026/3/6
 */
@Data
public class CardLineVO {

    /**
     * 日期
     */
    private List<String> xDate;

    /**
     * 个人名片新增数量
     */
    private List<Integer> uSeries;

    /**
     * 企业名片新增数量
     */
    private List<Integer> cSeries;

    @Data
    public static class CardLine {
        private String dayStr;
        private Integer cardType;
        private Integer cnt;
    }
}

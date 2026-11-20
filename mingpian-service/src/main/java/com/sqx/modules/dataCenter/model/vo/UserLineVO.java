package com.sqx.modules.dataCenter.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author chengming
 * @since 2026/3/6
 */
@Data
public class UserLineVO {

    /**
     * 日期
     */
    private List<String> xDate;

    /**
     * 用户新增数量
     */
    private List<Integer> series;

    @Data
    public static class UserLine {
        private String dayStr;
        private Integer cnt;
    }
}

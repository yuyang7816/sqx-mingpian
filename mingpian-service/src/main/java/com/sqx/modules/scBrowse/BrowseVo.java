package com.sqx.modules.scBrowse;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BrowseVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime update_time;
    //标题
    private String title;

    //采购id
    private Long demandId;

    //产品照片
    private String procureImg;

    //采购方公司名称
    private String procureRealName;

    //采购方联系电话
    private String procurePhone;

    //距离
    private Double distance;

    //是否已购买 0否 1是
    private Integer isBuy;


}

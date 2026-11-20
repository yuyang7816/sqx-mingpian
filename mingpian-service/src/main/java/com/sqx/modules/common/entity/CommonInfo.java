package com.sqx.modules.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通用配置管理
 */
@Data
@TableName("common_info")
public class CommonInfo implements Serializable {

    @TableId(type = IdType.INPUT)
    private long id;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    private Integer type; //1表示客服二维码 2表示公众号二维码 3表示全局佣金是否开启 4注册客服渠道id配置 5、佣金规则 6、

    private String value;

    private String max;

    private String min;

    private String conditionFrom;

}


package com.sqx.modules.cardList.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户名片分组表(UserCardGroups)实体类
 *
 * @author makejava
 * @since 2024-05-07 17:06:30
 */
@Data
public class UserCardGroups implements Serializable {
    private static final long serialVersionUID = 140629528448946431L;
/**
     *  
     */
    @TableId(type = IdType.INPUT)
    private String id;
/**
     * 用户id
     */
    private Long userId;
/**
     * 分组名称
     */
    private String groupsName;
/**
     * 备注
     */
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String sortId;

    /**
     * 是否默认 1=默认 2=不默认
     */
    private Integer isDefault;

}


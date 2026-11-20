package com.sqx.modules.businessCard.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


/**
 * 访问记录表(VisitCardRecord)实体类
 *
 * @author makejava
 * @since 2024-05-07 10:06:15
 */
@Data
public class VisitCardRecord implements Serializable {
    private static final long serialVersionUID = 750108959580453627L;
/**
     * id
     */
    @TableId(type = IdType.INPUT)
    private String id;
/**
     * 名片id
     */
    private Long cardId;
/**
     * 用户id
     */
    private Long userId;
/**
     * 用户名称
     */
    private String userName;
    /*访问来源*/
    private String visitSource;
/**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime visitTime;
/**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}


package com.sqx.modules.invite.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 邀请记录表(InviteRecord)实体类
 *
 * @author makejava
 * @since 2024-05-13 18:04:08
 */
@Data
public class InviteRecord implements Serializable {
    private static final long serialVersionUID = -68026509946813091L;

    @TableId(type = IdType.INPUT)
    private String id;
/**
     * 邀请人id
     */
    private Long inviterId;
    /*邀请人名片id*/
    private Long inviterCardId;
/**
     * 被邀请人id
     */
    private Long inviteeId;
/**
     * 被邀请人名片id
     */
    private Long inviteeCardId;
/**
     * 被邀请人头像
     */
    private String inviteePortrait;
/**
     * 被邀请人名称
     */
    private String inviteeName;
/**
     * 邀请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inviteTime;
/**
     * 邀请标题
     */
    private String inviteTitle;
/**
     * 邀请描述
     */
    private String inviteContent;
/**
     * 邀请类型 0注册 1加入企业
     */
    private Integer inviteType;
/**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
/**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
/**
     * 备注
     */
    private String remark;

}


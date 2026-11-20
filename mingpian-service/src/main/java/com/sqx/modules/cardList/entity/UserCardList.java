package com.sqx.modules.cardList.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户名片夹(UserCardList)实体类
 *
 * @author makejava
 * @since 2024-05-07 17:06:02
 */
@Data
public class UserCardList implements Serializable {
    private static final long serialVersionUID = 935812806296642118L;
/**
     * id
     */
    @TableId(type = IdType.INPUT)
    private String id;
/**
     * 用户id
     */
    private Long userId;
/**
     * 名片id
     */
    private Long cardId;
/**
     * 分组id
     */
    private String groupId;
/**
     * 名片备注
     */
    private String cardRemark;
    /*备注拼音全拼*/
    private String cardSpell;
    /*名片头像*/
    private String cardAvatar;
/**
     * 真实姓名
     */
    private String realName;
/**
     * 职务名称
     */
    private String cardPosition;
/**
     * 公司名称
     */
    private String cardCompany;
/**
     * 收藏/添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectTime;
/**
     * 联系次数
     */
    private Integer contactCount;
/**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
/**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private Integer cardType;

}


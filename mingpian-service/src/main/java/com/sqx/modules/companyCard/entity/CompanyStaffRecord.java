package com.sqx.modules.companyCard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公司员工动态信息(CompanyStaffRecord)实体类
 *
 * @author makejava
 * @since 2024-05-09 13:56:06
 */
@Data
public class CompanyStaffRecord implements Serializable {
    private static final long serialVersionUID = -66170371166309352L;

    @TableId(type = IdType.UUID)
    private String id;
/**
     * 企业名片id
     */
    private Long cardId;
/**
     * 企业成员id
     */
    private Long staffUserId;
/**
     * 企业成员名片id
     */
    private Long staffCardId;
/**
     * 申请加入原因
     */
    private String applyContent;
/**
     * 成员状态  0申请中 1审核通过 2拒绝 3退出
     */
    private Integer staffSattus;
/**
     * 成员来源 0自主申请 1公司邀请 2公司录入
     */
    private Integer staffSource;
/**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;
/**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinTime;
/**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
/**
     * 备注
     */
    private String remark;
    /*审核备注*/
    private String auditContent;

}


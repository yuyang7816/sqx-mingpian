package com.sqx.modules.companyCardTemplate.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 公司名片模板(CompanyTemplate)实体类
 *
 * @author makejava
 * @since 2024-05-09 11:32:35
 */
@Data
public class CompanyTemplate implements Serializable {
    private static final long serialVersionUID = 531679461169653396L;

    @TableId(type = IdType.INPUT)
    private String id;
/**
     * 公司名片id
     */
    private Integer companyCardId;
/**
     * 模板名称
     */
    private String templateName;
/**
     * 背景图片
     */
    private String backImg;
/**
     * 公司logo图片
     */
    private String companyLogo;
/**
     * 用户名称
     */
    private String userName;
/**
     * 公司名称
     */
    private String companyName;
/**
     * 职务名称
     */
    private String companyuPisition;
/**
     * 手机号码
     */
    private String phone;
/**
     * 邮箱
     */
    private String email;
/**
     * 公司地址
     */
    private String companyAddress;
/**
     * 公司简介
     */
    private String companyDesc;
/**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
/**
     * 备注
     */
    private String remark;

}


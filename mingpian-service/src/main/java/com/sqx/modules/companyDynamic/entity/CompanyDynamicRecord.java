package com.sqx.modules.companyDynamic.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 企业动态表(CompanyDynamicRecord)实体类
 *
 * @author makejava
 * @since 2024-05-09 17:14:51
 */
@Data
public class CompanyDynamicRecord implements Serializable {
    private static final long serialVersionUID = -29069634033241855L;
/**
     * id
     */
    @TableId(type = IdType.INPUT)
    private String id;
/**
     * 企业名片id
     */
    private Long companyCardId;
/**
     * 员工名片id
     */
    private Long staffCardId;
/**
     * 标题
     */
    private String title;
/**
     * 内容
     */
    private String content;
/**
     * 动态类型 1加入 2退出
     */
    private Integer dynamicType;
/**
     * 创建时间
     */
    private LocalDateTime createTime;
/**
     * 修改时间
     */
    private LocalDateTime updateTime;
/**
     * 备注
     */
    private String remark;

}


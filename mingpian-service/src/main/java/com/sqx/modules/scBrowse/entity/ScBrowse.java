package com.sqx.modules.scBrowse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sc_browse")
public class ScBrowse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 浏览记录id
     */
    @TableId(type = IdType.AUTO)
    private Long browseId;
    /**
     * 访问人id
     */
    private Long userId;
    /**
     * 需求id
     */
    private Long demandId;


    /**
     * 更新时间
     */
    private Date updateTime;

}

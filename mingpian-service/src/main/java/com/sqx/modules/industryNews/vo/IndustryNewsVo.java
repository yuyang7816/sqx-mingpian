package com.sqx.modules.industryNews.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.sqx.modules.industryNewsComment.entity.IndustryNewsComment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IndustryNewsVo extends IndustryNews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 搜索关键词
     */
    @ApiModelProperty("搜索关键词")
    private String keyWord;
    /**
     * 是否已点赞 0否 1是
     */
    @ApiModelProperty("是否已点赞 0否 1是")
    private Integer isLike;
    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer likeCount;
    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer commentCount;
    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String cardAvatar;
    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    private String realName;
    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    private String companyName;
    /**
     * 主营产品
     */
    @ApiModelProperty("主营产品")
    private String firstProduct;
    /**
     * 主营产品图
     */
    @ApiModelProperty("主营产品图")
    private String firstProductImg;
    /**
     * 公司logo
     */
    @ApiModelProperty("公司logo")
    private String companyLogo;
    /**
     * 名片id
     */
    @ApiModelProperty("名片id")
    private Long cardId;


    /**
     * 评论列表
     */
    @TableField(exist = false)
    @ApiModelProperty("评论列表")
    private List<IndustryNewsComment> commentList;
}

package com.sqx.modules.industryNews.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.DateUtils;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserCertificationService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.industry.service.IndustryService;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.sqx.modules.industryNews.dao.IndustryNewsDao;
import com.sqx.modules.industryNews.service.IndustryNewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.industryNews.vo.IndustryNewsVo;
import com.sqx.modules.industryNewsComment.entity.IndustryNewsComment;
import com.sqx.modules.industryNewsComment.service.IIndustryNewsCommentService;
import com.sqx.modules.industryNewsLike.entity.IndustryNewsLike;
import com.sqx.modules.industryNewsLike.service.IIndustryNewsLikeService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.sys.entity.SysUserEntity;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.vipDetails.entity.VipDetails;
import com.sqx.modules.vipDetails.service.VipDetailsService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 行业资讯 服务实现类
 * @author wuChang
 * @since 2024-02-26
 */
@Service
public class IndustryNewsServiceImp extends ServiceImpl<IndustryNewsDao, IndustryNews> implements IndustryNewsService {
    @Autowired
    private UserService userService;
    @Autowired
    private VipDetailsService vipDetailsService;
    @Autowired
    private IIndustryNewsLikeService likeService;
    @Autowired
    private IIndustryNewsCommentService commentService;
    @Autowired
    private UserCertificationService certificationService;
    @Autowired
    private IIndustryNewsCommentService iIndustryNewsCommentService;
    @Autowired
    private IndustryService industryService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MessageService messageService;

    @Override
    public Result saveIndustryNews(IndustryNews industryNews) {
        UserEntity userEntity = userService.getById(industryNews.getUserId());
        UserCertification certification = certificationService.getOne(new QueryWrapper<UserCertification>().eq("user_id", userEntity.getUserId()));
        if (certification == null) {
            return Result.error("请先实名认证");
        }
        if (certification.getStatus() != 1) {
            return Result.error("请等待实名认证通过");
        }
        if (certification.getUserType() == 2) {
            industryNews.setEnterpriseName(certification.getEnterpriseName());
        }
        if (userEntity.getIsVip() != 1) {
            return Result.error("请先开通会员");
        }
        industryNews.setReleaseType(1);
        VipDetails vipDetails = vipDetailsService.getById(userEntity.getVipId());
        if (vipDetails == null) {
            return Result.error("无法检查到当前会员权限,请联系管理员");
        }
        return null;
    }



    @Override
    public IPage<IndustryNewsVo> getIndustryNewsList(Integer page, Integer limit, IndustryNewsVo industryNewsVo) {
        Page<IndustryNewsVo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getIndustryNewsList(pages, industryNewsVo);
    }

    @Override
    public Result adminSaveIndustryNews(IndustryNews industryNews) {
        SysUserEntity sysUserEntity = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        industryNews.setSysUserId(sysUserEntity.getUserId());
        industryNews.setReleaseType(2);
        industryNews.setSysUserName(sysUserEntity.getUsername());
        if (industryNews.getNewsId() != null) {
            return Result.upStatus(baseMapper.updateById(industryNews));
        } else {
            industryNews.setStatus(1);
            industryNews.setCreateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.insert(industryNews));
        }
    }


    /**
     * 上架或下架行业资讯 （用户id和资讯id不可同时都传值）
     * @param userId 用户id 传用户id将当前用户全部行业下架或上架
     * @param newsId 资讯id 传资讯id值将当前数据下架或上架
     * @param type 1上架 3会员到期已下架
     */
    @Override
    public void downOrUp(Long userId, Long newsId, Integer type) {
        QueryWrapper<IndustryNews> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (newsId != null) {
            wrapper.eq("news_id", newsId);
        }
        List<IndustryNews> industryNewsList = baseMapper.selectList(wrapper);
        for (IndustryNews industryNews : industryNewsList) {
            if (type == 1) {
                industryNews.setStatus(1);
            } else {
                industryNews.setStatus(3);
            }
            baseMapper.updateById(industryNews);
        }
    }

    @Override
    public IndustryNews getIndustryNewsById(Long loginUserId, Long newsId) {
        IndustryNewsVo industryNewsVo = baseMapper.getIndustryNewsById(loginUserId, newsId);
        if (industryNewsVo != null) {
            industryNewsVo.setCommentList(iIndustryNewsCommentService.getIndustryNewsCommentList(newsId));
        }
        return industryNewsVo;
    }

    @Override
    public Result deleteIndustryNewsById(Long userId, Long newsId) {
        IndustryNews industryNews = baseMapper.selectById(newsId);
        if (industryNews.getUserId() != null && userId != null) {
            if (!industryNews.getUserId().equals(userId)) {
                return Result.error("非法操作");
            }
        }
        //删除所有点赞
        likeService.remove(new QueryWrapper<IndustryNewsLike>().eq("news_id", newsId));
        //删除所有评论
        commentService.remove(new QueryWrapper<IndustryNewsComment>().eq("news_id", newsId));
        return Result.upStatus(baseMapper.deleteById(newsId));


    }

    @Override
    public Result examineNews(Long newsId, Integer status, String examineRemark) {
        IndustryNews industryNews = baseMapper.selectById(newsId);
        UserEntity userEntity = userService.getById(industryNews.getUserId());
        if (userEntity == null) {
            return Result.error("审核的用户不存在");
        }
        industryNews.setExamineRemark(examineRemark);
        if (status == 1) {
            industryNews.setStatus(1);
            industryNews.setIsEnable(1);
        } else {
            industryNews.setStatus(2);
            industryNews.setIsEnable(0);
        }
        if (StringUtils.isNotBlank(userEntity.getOpenId())) {
            List<String> msgList = new ArrayList<>();
            msgList.add("行业资讯审核通知");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (status == 1) {
                msgList.add("审核已通过");
            } else {
                msgList.add("审核未通过");
            }
            String str = "";
            if (StringUtils.isNotBlank(examineRemark)) {
                if (examineRemark.length() > 20) {
                    str = examineRemark.substring(0, 16) + "...";
                } else {
                    str = examineRemark;
                }
            } else {
                str = "点击查看原因";
            }

            CommonInfo one = commonInfoService.findOne(292);
            msgList.add(str);
            msgList.add(fmt.format(LocalDateTime.now()));
            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
        }
        String content = "";
        MessageInfo messageInfo = new MessageInfo();
        if (status == 1) {
            content = "您发布的行业资讯审核已通过";

        } else {
            content = "您发布的行业资讯审核未通过,请在资讯详情页面查看原因";
        }
        messageInfo.setContent(content);
        messageInfo.setTitle("审核通知");
        messageInfo.setState(String.valueOf(6));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);

        if (StringUtils.isNotEmpty(userEntity.getClientid())) {
            userService.pushToSingle("审核通知", content, userEntity.getClientid());
        }
        return Result.upStatus(baseMapper.updateById(industryNews));
    }

    @Override
    public IPage<IndustryNews> getMyLikeIndustryList(Integer page, Integer limit, Long userId) {
        Page<IndustryNews> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getMyLikeIndustryList(pages, userId);

    }

    @Override
    public Result upOrDownNews(Long newsId, String downRemark) {
        IndustryNews industryNews = baseMapper.selectById(newsId);
        if (industryNews == null) {
            return Result.error("当前资讯不存在");
        }
        if (industryNews.getIsEnable() == 0) {
            industryNews.setIsEnable(1);
        } else {
            industryNews.setIsEnable(0);
        }
        industryNews.setDownRemark(downRemark);
        return Result.upStatus(baseMapper.updateById(industryNews));

    }

}

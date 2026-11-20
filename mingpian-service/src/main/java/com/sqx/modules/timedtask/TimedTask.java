package com.sqx.modules.timedtask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.businessCard.service.BusinessCardService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.industryNews.entity.IndustryNews;
import com.sqx.modules.industryNews.service.IndustryNewsService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class TimedTask {
    @Autowired
    private UserService userService;

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private IndustryNewsService industryNewsService;

    /**
     * 用户VIP是否到期
     */
/*    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void checkUserTime() {
        List<UserEntity> list = userService.list(new QueryWrapper<UserEntity>().eq("is_vip", 1));
        for (UserEntity userEntity : list) {
            if (LocalDateTime.now().isAfter(userEntity.getVipEndTime())) {
                userEntity.setIsVip(0);
                userService.updateById(userEntity);
                industryNewsService.downOrUp(userEntity.getUserId(), null, 3);
                businessCardService.downOrUp(userEntity.getUserId(), null, 3);
            }
        }
    }*/


    /**
     * 到期提前提醒
     */
/*    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void advanceUserTime() {
        List<UserEntity> list = userService.list(new QueryWrapper<UserEntity>().eq("is_vip", 1));
        CommonInfo advance = commonInfoService.findOne(295);
        for (UserEntity userEntity : list) {
            Duration userHours = Duration.between(LocalDateTime.now(), userEntity.getVipEndTime());
            if (userHours.toDays() == Integer.parseInt(advance.getValue())) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                List<String> msgList = new ArrayList<>();
                msgList.add("VIP会员");
                msgList.add(fmt.format(userEntity.getVipEndTime()));
                msgList.add("为了您的服务数据不中断,请及时续费");
                CommonInfo commonInfo = commonInfoService.findOne(294);
                if (StringUtils.isNotBlank(userEntity.getOpenId())) {
                    SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), commonInfo.getValue(), msgList, 3);
                }
                if (StringUtils.isNotEmpty(userEntity.getClientid())) {
                    userService.pushToSingle("VIP即将到期提醒", "为了您的服务数据不中断,请及时续费", userEntity.getClientid());
                }
            }
        }
    }*/


}

package com.sqx.modules.dataCenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqx.common.exception.SqxException;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.businessCard.dao.BusinessCardDao;
import com.sqx.modules.businessCard.dao.VisitCardRecordDao;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.dataCenter.model.StatisticsBo;
import com.sqx.modules.dataCenter.model.vo.CardLineVO;
import com.sqx.modules.dataCenter.model.vo.CardStatisticsVO;
import com.sqx.modules.dataCenter.model.vo.CountCardVO;
import com.sqx.modules.dataCenter.model.vo.UserLineVO;
import com.sqx.modules.dataCenter.service.DataCenterService;
import com.sqx.modules.invite.dao.InviteDao;
import com.sqx.modules.invite.dao.InviteRecordDao;
import com.sqx.modules.invite.entity.InviteRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chengming
 * @since 2026/1/13
 */
@Slf4j
@Service
public class DataCenterServiceImpl implements DataCenterService {

    @Resource
    private VisitCardRecordDao visitCardRecordDao;
    @Resource
    private BusinessCardDao businessCardDao;
    @Resource
    private InviteRecordDao inviteRecordDao;
    @Resource
    private InviteDao inviteDao;
    @Resource
    private UserDao userDao;

    @Override
    public CountCardVO countCard(Long cardId) {
        BusinessCard businessCard = businessCardDao.selectById(cardId);
        if (Objects.isNull(businessCard)) {
            log.error("名片信息不存在");
            throw new SqxException("名片信息不存在");
        }

        Integer browseCount = visitCardRecordDao.countVisitor(cardId);

        Integer views = visitCardRecordDao.countViews(businessCard.getUserId());

        Integer invites = inviteRecordDao.selectCount(new LambdaQueryWrapper<InviteRecord>()
                .eq(InviteRecord::getInviterId, businessCard.getUserId())
        );

        CountCardVO vo = new CountCardVO();
        vo.setVisitor(browseCount);
        vo.setViews(views);
        vo.setInvites(invites);
        return vo;
    }

    @Override
    public UserLineVO userLine(String startTime, String endTime) {
        // 生成连续的日期列表
        List<String> dateList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(startTime, formatter);
        LocalDate endDate = LocalDate.parse(endTime, formatter);

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            dateList.add(current.format(formatter));
            current = current.plusDays(1);
        }

        List<UserLineVO.UserLine> userLines = userDao.queryUserLine(startDate, endDate);
        Map<String, Integer> userLineMap = userLines.stream()
                .collect(Collectors.toMap(
                        UserLineVO.UserLine::getDayStr,
                        UserLineVO.UserLine::getCnt
                ));

        List<Integer> seriesData = new ArrayList<>();
        for (String date : dateList) {
            seriesData.add(userLineMap.getOrDefault(date, 0));
        }

        UserLineVO vo = new UserLineVO();
        vo.setXDate(dateList);
        vo.setSeries(seriesData);
        return vo;
    }

    @Override
    public CardLineVO cardLine(String startTime, String endTime) {
        // 生成连续的日期列表
        List<String> dateList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(startTime, formatter);
        LocalDate endDate = LocalDate.parse(endTime, formatter);

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            dateList.add(current.format(formatter));
            current = current.plusDays(1);
        }

        List<CardLineVO.CardLine> cardLines = businessCardDao.queryCardLine(startDate, endDate);

        Map<String, Integer> userCardMap = cardLines.stream()
                .filter(d -> d.getCardType() == 1)
                .collect(Collectors.toMap(CardLineVO.CardLine::getDayStr, CardLineVO.CardLine::getCnt));

        Map<String, Integer> companyCardMap = cardLines.stream()
                .filter(d -> d.getCardType() == 2)
                .collect(Collectors.toMap(CardLineVO.CardLine::getDayStr, CardLineVO.CardLine::getCnt));

        List<Integer> cSeries = new ArrayList<>();
        List<Integer> uSeries = new ArrayList<>();

        for (String date : dateList) {
            uSeries.add(userCardMap.getOrDefault(date, 0));
            cSeries.add(companyCardMap.getOrDefault(date, 0));
        }

        CardLineVO vo = new CardLineVO();
        vo.setXDate(dateList);
        vo.setCSeries(cSeries);
        vo.setUSeries(uSeries);
        return vo;
    }

    @Override
    public CardStatisticsVO card(StatisticsBo statisticsBo) {
        statisticsBo.initFormattedDateRange();

        Integer total = businessCardDao.countCardNum(null, statisticsBo.getStartTime(), statisticsBo.getEndTime());

        Integer user = businessCardDao.countCardNum(1, statisticsBo.getStartTime(), statisticsBo.getEndTime());

        Integer company = businessCardDao.countCardNum(2, statisticsBo.getStartTime(), statisticsBo.getEndTime());

        CardStatisticsVO vo = new CardStatisticsVO();
        vo.setTotal(total);
        vo.setUser(user);
        vo.setCompany(company);
        return vo;
    }
}

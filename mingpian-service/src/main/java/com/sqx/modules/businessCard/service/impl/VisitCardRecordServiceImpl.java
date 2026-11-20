package com.sqx.modules.businessCard.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.businessCard.entity.VisitCardRecord;
import com.sqx.modules.businessCard.dao.VisitCardRecordDao;
import com.sqx.modules.businessCard.service.BusinessCardService;
import com.sqx.modules.businessCard.service.VisitCardRecordService;
import com.sqx.modules.utils.text.StringUtils;
import com.sqx.modules.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 访问记录表(VisitCardRecord)表服务实现类
 *
 * @author makejava
 * @since 2024-05-07 10:06:15
 */
@Service("visitCardRecordService")
public class VisitCardRecordServiceImpl extends ServiceImpl<VisitCardRecordDao, VisitCardRecord> implements VisitCardRecordService {
    @Resource
    private VisitCardRecordDao visitCardRecordDao;
    @Resource
    private BusinessCardService businessCardService;
    @Resource
    private UserService userService;


    @Override
    public Result queryByPage(Long userId,JSONObject params) {
        Integer pageSize = params.getInteger("pageSize");
        Integer pageNum = params.getInteger("pageNum");
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Map<String, Object>> maps = visitCardRecordDao.queryVisitRecord(userId);
        PageInfo<Map<String, Object>> mapPageInfo = new PageInfo<>(maps);
        return Result.success().put("data",mapPageInfo);
    }

    /**
     * 增加访问记录以及修改名片访问次数
     * @param userId
     * @param cardId
     * @return
     */
    @Override
    public Result visitCard(Long userId, JSONObject params) {
        Long cardId = params.getLong("cardId");
        UserEntity userEntity = userService.queryByUserId(userId);
        BusinessCard businessCard = businessCardService.getById(cardId);
        if (StringUtils.isNull(businessCard)){
            return Result.error("名片不存在,请确认参数是否正确");
        }
        Long userId1 = businessCard.getUserId();
        if (userId.equals(userId1)){
            return Result.success();
        }
        Integer browseCount = businessCard.getBrowseCount();
        if (ObjectUtil.isNull(browseCount)){
            businessCard.setBrowseCount(1);
        }else {
            browseCount++;
            businessCard.setBrowseCount(browseCount);
        }
        Integer popularityValue = businessCard.getPopularityValue();
        if (ObjectUtil.isNull(popularityValue)){
            businessCard.setPopularityValue(1);
        }else {
            popularityValue++;
            businessCard.setPopularityValue(popularityValue);
        }
        VisitCardRecord visitCardRecord = new VisitCardRecord();
        visitCardRecord.setId(IdUtils.fastSimpleUUID());
        visitCardRecord.setCardId(cardId);
        visitCardRecord.setUserId(userId);
        visitCardRecord.setUserName(userEntity.getUserName());
        visitCardRecord.setVisitTime(LocalDateTime.now());
        visitCardRecord.setCreateTime(LocalDateTime.now());
        visitCardRecord.setVisitSource(params.getString("source"));
        visitCardRecordDao.insert(visitCardRecord);
        businessCardService.updateById(businessCard);
        return Result.success();
    }

    @Override
    public Result visitUserData(Long userId) {
        Map<String, Object> resuleMap = new HashMap<>();
        //展示的名片
        BusinessCard businessCard = businessCardService.queryUserShowCard(userId,1);
        if (StringUtils.isNull(businessCard)){
            return Result.error("未设置展示名片！");
        }
        //总访问次数
        Integer browseCount = businessCard.getBrowseCount();
        resuleMap.put("totalVisitCount",browseCount);
        //分享次数
        Integer shareCount = businessCard.getShareCount();
        resuleMap.put("totalShareCount",shareCount);
        //今日访问次数
        Long cardId = businessCard.getCardId();
        LocalDate now = LocalDate.now();
        int i = visitCardRecordDao.queryTodayVisitData(cardId, now.toString());
        resuleMap.put("todayVisitCoune",i);
        //最近访客数据
        List<Map<String, Object>> maps = visitCardRecordDao.queryrecentlyVisitData(cardId,userId);
        resuleMap.put("recentlyList",maps);
        return Result.success().put("data",resuleMap);
    }

    @Override
    public Result visitMe(Long userId,JSONObject params) {
        Integer pageSize = params.getInteger("pageSize");
        Integer pageNum = params.getInteger("pageNum");
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }

        Long cardId = params.getLong("cardId");

        PageHelper.startPage(pageNum,pageSize);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> maps = visitCardRecordDao.queryVisitMe(userId, cardId);
        PageInfo<Map<String, Object>> mapPageInfo = new PageInfo<>(maps);
/*        Map<String, List<Map<String, Object>>> resultMap = maps.stream().collect(Collectors.groupingBy(
                map -> {
                    Date visitTime = (Date) map.get("visitTime");
                    String format = DateUtils.format(visitTime);
                    LocalDateTime parse = LocalDateTime.parse(format, formatter);
                    LocalDate localDate = parse.toLocalDate();
                    return localDate.toString();
                },
                Collectors.toList()
        ));*/

/*        List<Map<String, List<Map<String, Object>>>> visitTime1 = maps.stream().collect(Collectors.groupingBy(
                        map -> {
                            Date visitTime = (Date) map.get("visitTime");
                            String format = DateUtils.format(visitTime);
                            LocalDateTime parse = LocalDateTime.parse(format, formatter);
                            LocalDate localDate = parse.toLocalDate();
                            return localDate.toString();
                        }
                ))
                .entrySet().stream() // 获取Map的entrySet并转换为Stream
                .map(entry -> { // 将每个entry转换为一个新的Map，其中只包含一个键值对
                    Map<String, List<Map<String, Object>>> singleEntryMap = new LinkedHashMap<>();
                    singleEntryMap.put(entry.getKey(), entry.getValue()); // 键是日期字符串，值是数据列表
                    return singleEntryMap;
                })
                .collect(Collectors.toList());*/
        return Result.success().put("data",mapPageInfo);
    }

    @Override
    public Result myVisit(Long userId,JSONObject params) {
        Integer pageSize = params.getInteger("pageSize");
        Integer pageNum = params.getInteger("pageNum");
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Map<String, Object>> maps = visitCardRecordDao.queryMyVisit(userId);
        PageInfo<Map<String, Object>> mapPageInfo = new PageInfo<>(maps);
        return Result.success().put("data",mapPageInfo);
    }
}

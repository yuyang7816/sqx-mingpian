package com.sqx.modules.dataCenter.service;

import com.sqx.modules.dataCenter.model.StatisticsBo;
import com.sqx.modules.dataCenter.model.vo.CardLineVO;
import com.sqx.modules.dataCenter.model.vo.CardStatisticsVO;
import com.sqx.modules.dataCenter.model.vo.CountCardVO;
import com.sqx.modules.dataCenter.model.vo.UserLineVO;

/**
 * @author chengming
 * @since 2026/1/13
 */
public interface DataCenterService {

    CountCardVO countCard(Long cardId);

    UserLineVO userLine(String startTime, String endTime);

    CardLineVO cardLine(String startTime, String endTime);

    CardStatisticsVO card(StatisticsBo statisticsBo);
}

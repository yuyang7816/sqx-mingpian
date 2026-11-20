package com.sqx.modules.app.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserMoneyDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMoneyDetailsDao extends BaseMapper<UserMoneyDetails> {
    Double monthIncome(@Param("date") String date,@Param("userId") Long userId);
    Double selectMyProfit(Long userId);


}

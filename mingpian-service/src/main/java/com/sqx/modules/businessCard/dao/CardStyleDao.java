package com.sqx.modules.businessCard.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.businessCard.entity.CardStyle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 名片样式(CardStyle)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-06 17:17:18
 */
@Mapper
public interface CardStyleDao extends BaseMapper<CardStyle> {
}


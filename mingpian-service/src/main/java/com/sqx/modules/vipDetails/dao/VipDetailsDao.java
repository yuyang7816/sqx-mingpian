package com.sqx.modules.vipDetails.dao;

import com.sqx.modules.vipDetails.entity.VipDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会员配置 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2024-02-26
 */
@Mapper
public interface VipDetailsDao extends BaseMapper<VipDetails> {

    List<VipDetails> queryByPage(@Param("isEnable")Integer isEnable);

}

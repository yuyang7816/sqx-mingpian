package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.dataCenter.model.vo.UserLineVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {



    int queryInviterCount(@Param("inviterCode") String inviterCode);

    int queryUserCount(@Param("type") int type, @Param("date") String date);

    Double queryPayMoney(@Param("type") int type, @Param("date") String date);


    int userMessage(String date, Integer type);



    UserEntity selectUserByUserId(@Param("userId") Long userId,@Param("byUserId") Long byUserId);

    IPage<UserEntity> selectUserPage(@Param("pages") Page<UserEntity> pages, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userEntity") UserEntity userEntity);

    IPage<HashMap<String, Object>> getMyTeamUserList(@Param("pages") Page<HashMap<String, Object>> pages, @Param("invitationCode") String invitationCode, @Param("userId") Long userId);

    IPage<UserEntity> getLikeUserList(@Param("pages") Page<UserEntity> pages, @Param("newsId") Long newsId);

    int getStaffInviter(@Param("staffInvitationCode") String staffInvitationCode, @Param("dateType") Integer dateType, @Param("date") String date);

    List<UserLineVO.UserLine> queryUserLine(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Integer queryPlatformUserCount(@Param("type") int type, @Param("date") String date, @Param("platform") String platform);
}

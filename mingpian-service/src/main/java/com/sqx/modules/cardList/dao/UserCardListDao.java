package com.sqx.modules.cardList.dao;

import com.sqx.modules.cardList.entity.UserCardList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户名片夹(UserCardList)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-07 17:06:02
 */
 @Mapper
public interface UserCardListDao extends BaseMapper<UserCardList>{

  List<UserCardList> queryMyCollect(@Param("groupId")String groupId,
                                    @Param("sortType")Integer sortType,
                                    @Param("cardRemark")String cardRemark,
                                    @Param("cardPosition")String cardPosition,
                                    @Param("cardCompany")String cardCompany,
                                    @Param("initial")String initial,
                                    @Param("searchKey") String searchKey,
                                    @Param("userId") Long userId
  );

  int updateCardRemark(@Param("id")String id,@Param("cardRemark")String carRemark,@Param("upperCase")String upperCase);
  int updateRemark(@Param("id")String id,@Param("remark")String remark);

  Map<String ,Object> queryDetailsById(@Param("id")String id);

  void incrContactCount(@Param("userId") Long userId, @Param("cardId") Long cardId);
}


package com.sqx.modules.cardList.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.businessCard.service.BusinessCardService;
import com.sqx.modules.cardList.entity.UserCardList;
import com.sqx.modules.cardList.dao.UserCardListDao;
import com.sqx.modules.cardList.service.UserCardListService;
import com.sqx.modules.utils.ChineseCharacterUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.utils.text.StringUtils;
import com.sqx.modules.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户名片夹(UserCardList)表服务实现类
 *
 * @author makejava
 * @since 2024-05-07 17:06:02
 */
@Service("userCardListService")
public class UserCardListServiceImpl extends ServiceImpl<UserCardListDao, UserCardList> implements UserCardListService {
    @Resource
    private UserCardListDao userCardListDao;
    @Resource
    private BusinessCardService businessCardService;
    @Resource
    private UserService userService;


    /**
     * 收藏名片至名片夹
     * @param userId
     * @param cardId
     * @return
     */
    @Override
    public Result collectCard(Long userId, UserCardList userCardList) {
        BusinessCard businessCard = businessCardService.getById(userCardList.getCardId());
        if (StringUtils.isNull(businessCard)){
            return Result.error("名片不存在！");
        }
        userCardList.setId(IdUtils.fastSimpleUUID());
        userCardList.setUserId(userId);
        String cardRemark = userCardList.getCardRemark();
        if (StringUtils.isEmpty(cardRemark)){
            String realName = businessCard.getRealName();
            String upperCase = ChineseCharacterUtil.getUpperCase(realName, true);
            userCardList.setCardRemark(realName);
            userCardList.setCardSpell(upperCase);
        }else {
            String upperCase = ChineseCharacterUtil.getUpperCase(cardRemark,true);
            userCardList.setCardSpell(upperCase);
        }
        userCardList.setCardId(businessCard.getCardId());
        userCardList.setCardAvatar(businessCard.getCardAvatar());
        userCardList.setRealName(businessCard.getRealName());
        userCardList.setCardCompany(businessCard.getCompanyName());
        userCardList.setCardPosition(businessCard.getCompanyPosition());
        userCardList.setCollectTime(LocalDateTime.now());
        userCardList.setCreateTime(LocalDateTime.now());
        userCardListDao.insert(userCardList);
        return Result.success();
    }

    /**
     * 查询我的名片夹
     * @param userId
     * @param params
     * @return
     */
    @Override
    public Result queryMyCollect(Long userId, JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);

        String searchKey = params.getString("searchKey");

        List<UserCardList> userCardLists = userCardListDao.queryMyCollect(
                params.getString("groupId"),
                params.getInteger("sortType"),
                params.getString("cardRemark"),
                params.getString("cardPosition"),
                params.getString("cardCompany"),
                params.getString("initial"),
                searchKey,
                userId
        );

        for (UserCardList userCardList : userCardLists) {
            BusinessCard businessCard = businessCardService.getById(userCardList.getCardId());
            userCardList.setCardType(businessCard.getCardType());
        }

        PageInfo<UserCardList> userCardListPageInfo = new PageInfo<>(userCardLists);
        return Result.success().put("data",userCardListPageInfo);
    }

    @Override
    public Result updateCardRemark(JSONObject params) {
        String id = params.getString("id");
        String cardRemark = params.getString("cardRemark");
        if (StringUtils.isEmpty(id)){
            return Result.error("id不可以为空！");
        }
        if (StringUtils.isEmpty(cardRemark)){
            return Result.error("名称不可以为空！");
        }
        String upperCase = ChineseCharacterUtil.getUpperCase(cardRemark, true);
        int i = userCardListDao.updateCardRemark(id, cardRemark,upperCase);
        return Result.upStatus(i);
    }

    @Override
    public Result updateRemark(JSONObject params) {
        String id = params.getString("id");
        String remark = params.getString("remark");
        if (StringUtils.isEmpty(id)){
            return Result.error("id不可以为空！");
        }
        if (StringUtils.isEmpty(remark)){
            return Result.error("备注不可以为空！");
        }
        int i = userCardListDao.updateRemark(id, remark);
        return Result.upStatus(i);
    }

    @Override
    public Result queryDetailsById(String id) {
        Map<String, Object> stringObjectMap = userCardListDao.queryDetailsById(id);
        return Result.success().put("data",stringObjectMap);
    }

    @Override
    public void shareCard(Long cardId, HttpServletResponse response) {
        BusinessCard businessCard = businessCardService.getById(cardId);
        Integer shareCount = businessCard.getShareCount();
        shareCount++;
        businessCard.setShareCount(shareCount);
        businessCardService.updateById(businessCard);
        Long userId = businessCard.getUserId();
        UserEntity userEntity = userService.queryByUserId(userId);
        SenInfoCheckUtil.getCardPoster(userEntity.getInvitationCode(), String.valueOf(cardId), response);
    }

    @Override
    public Result moveToGroups(JSONObject params) {
        //分组id
        String groupId = params.getString("groupId");
        //名片夹id
        String cardListId = params.getString("cardListId");
        //根据id查询用户名片夹信息
        UserCardList userCardList = userCardListDao.selectById(cardListId);
        if (ObjectUtil.isNull(userCardList)){
            return Result.error("未查询到名片夹信息");
        }
        userCardList.setGroupId(groupId);
        int i = userCardListDao.updateById(userCardList);
        return Result.upStatus(i);
    }

    @Override
    public Result removeById(String id) {
        int i = userCardListDao.deleteById(id);
        return Result.upStatus(i);
    }

    @Override
    public Boolean isCollectCard(Long userId, Long cardId) {
        int count = this.count(new LambdaQueryWrapper<UserCardList>()
                .eq(UserCardList::getUserId, userId)
                .eq(UserCardList::getCardId, cardId)
        );

        return count > 0;
    }

    @Override
    public void incrContactCount(Long userId, Long cardId) {
        this.baseMapper.incrContactCount(userId, cardId);
    }
}

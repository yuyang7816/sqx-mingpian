package com.sqx.modules.vipDetails.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.businessCard.service.BusinessCardService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.industryNews.service.IndustryNewsService;
import com.sqx.modules.invite.dao.InviteDao;
import com.sqx.modules.vipDetails.dao.VipDetailsDao;
import com.sqx.modules.vipDetails.entity.VipDetails;
import com.sqx.modules.vipDetails.service.VipDetailsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员配置 服务实现类
 * @author wuChang
 * @since 2024-02-26
 */
@Service
public class VipDetailsServiceImp extends ServiceImpl<VipDetailsDao, VipDetails> implements VipDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService moneyDetailsService;
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private IndustryNewsService industryNewsService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private InviteDao inviteDao;

    @Override
    public Result saveVipDetails(VipDetails vipDetails) {
        if (vipDetails.getId() != null) {
            vipDetails.setUpdateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.updateById(vipDetails));
        } else {
            vipDetails.setCreateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.insert(vipDetails));
        }
    }

    @Override
    public Result getVipDetailsList(JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        if (ObjectUtils.isEmpty(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        if (ObjectUtils.isEmpty(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);
        List<VipDetails> vipDetailsList = baseMapper.queryByPage(params.getInteger("isEnable"));
        PageInfo<VipDetails> vipDetailsPageInfo = new PageInfo<>(vipDetailsList);
        return Result.success().put("data",vipDetailsPageInfo);
    }

    @Override
    public Result buyVipCallBack(Long userId, BigDecimal money, Integer classify, Long parentId, String staffInviterCode) {
        UserEntity userEntity = userService.getById(userId);
        if (userEntity == null) {
            return Result.error("用户不存在");
        }
        userEntity.setVipId(parentId);
        userEntity.setIsVip(1);
        userService.updateById(userEntity);

        VipDetails vipDetails = baseMapper.selectById(parentId);
        //增加用户会员时间
        this.addVipTime(userId, vipDetails.getVipMonthCount());
        //上架所有的名片和行业资讯
        industryNewsService.downOrUp(userEntity.getUserId(), null, 1);
        businessCardService.downOrUp(userEntity.getUserId(), null, 1);

        UserMoneyDetails moneyDetails = new UserMoneyDetails();
        moneyDetails.setTitle("购买会员《" + vipDetails.getVipName() + "》成功");
        moneyDetails.setMoney(vipDetails.getMoney());
        moneyDetails.setUserId(userId);
        moneyDetails.setClassify(1);
        moneyDetails.setType(2);
        moneyDetails.setState(2);
        moneyDetails.setMoneyType(2);
        moneyDetails.setContent("购买会员《" + vipDetails.getVipName() + "》花费:" + vipDetails.getMoney() + "元");
        moneyDetails.setCreateTime(LocalDateTime.now());
        moneyDetailsService.save(moneyDetails);

        //如果有邀请码
        // if (StringUtils.isNotBlank(userEntity.getStaffInviterCode())) {
        //     UserEntity inviterUser = userService.getOne(new LambdaQueryWrapper<UserEntity>()
        //             .eq(UserEntity::getStaffInvitationCode, userEntity.getStaffInviterCode())
        //     );
        //     if (Objects.nonNull(inviterUser)) {
        //         // 分销比例
        //         String rate = commonInfoService.findOne(316).getValue();
        //
        //         // 分销金额
        //         BigDecimal inviterMoney = vipDetails.getMoney().multiply(new BigDecimal(rate));
        //
        //         inviteDao.incrInviterMoney(inviterUser.getUserId(), inviterMoney);
        //     }
        // }
        return Result.success();
    }

    @Override
    public Result giveUserVip(Long userId, Long vipId, Integer count) {
        //如果用户不是会员,则赠送的会员即是用户的会员,如果用户是会员,则只延长到期时间。
        UserEntity userEntity = userService.getById(userId);
        if (userEntity == null) {
            return Result.error("所选用户不存在");
        }
        VipDetails vipDetails;
        if (userEntity.getIsVip() == 0) {
            vipDetails = baseMapper.selectById(vipId);
            if (vipDetails == null) {
                return Result.error("所选VIP会员不存在");
            }
        } else {
            vipDetails = baseMapper.selectById(userEntity.getVipId());
        }

        if (userEntity.getVipEndTime() != null) {
            if (LocalDateTime.now().isAfter(userEntity.getVipEndTime())) {
                userEntity.setVipId(vipId);
                userEntity.setVipEndTime(LocalDateTime.now().plusMonths((long) vipDetails.getVipMonthCount() * count));
                if(userEntity.getVipCardNum()==null || userEntity.getVipCardNum()==0){
                    userEntity.setVipCardNum(Integer.parseInt(commonInfoService.findOne(431).getValue()));
                }
            } else {
                userEntity.setVipEndTime(userEntity.getVipEndTime().plusMonths((long) vipDetails.getVipMonthCount() * count));
            }
        } else {
            userEntity.setVipId(vipId);
            userEntity.setVipEndTime(LocalDateTime.now().plusMonths((long) vipDetails.getVipMonthCount() * count));
            if(userEntity.getVipCardNum()==null || userEntity.getVipCardNum()==0){
                userEntity.setVipCardNum(Integer.parseInt(commonInfoService.findOne(431).getValue()));
            }
        }

        userEntity.setIsVip(1);

        return userService.updateById(userEntity) ? Result.success() : Result.error();
    }

    @Override
    public Result cancelUserVip(Long userId) {
        UserEntity userEntity = userService.getById(userId);
        userEntity.setIsVip(0);
        userEntity.setVipEndTime(LocalDateTime.now());
        return userService.updateById(userEntity) ? Result.success() : Result.error();
    }

    /**
     * 增加会员到期时间
     * @param userId 用户id
     * @param month 增加月份
     */
    public void addVipTime(Long userId, Integer month) {
        UserEntity userEntity = userService.getById(userId);

        if (userEntity.getVipEndTime() != null) {
            if (LocalDateTime.now().isAfter(userEntity.getVipEndTime())) {
                userEntity.setVipEndTime(LocalDateTime.now().plusMonths(month));
                if(userEntity.getVipCardNum()==null || userEntity.getVipCardNum()==0){
                    userEntity.setVipCardNum(Integer.parseInt(commonInfoService.findOne(431).getValue()));
                }
            } else {
                userEntity.setVipEndTime(userEntity.getVipEndTime().plusMonths(month));
            }
        } else {
            userEntity.setVipEndTime(LocalDateTime.now().plusMonths(month));
            if(userEntity.getVipCardNum()==null || userEntity.getVipCardNum()==0){
                userEntity.setVipCardNum(Integer.parseInt(commonInfoService.findOne(431).getValue()));
            }
        }

        userService.updateById(userEntity);
    }


    @Scheduled(cron = "0 0 0 1 * ?")
    public void closeAndAddCardNum() {
        userService.update(null, Wrappers.<UserEntity>lambdaUpdate()
                .set(UserEntity::getVipCardNum,0));
        List<UserEntity> userEntitieList = userService.list(new QueryWrapper<UserEntity>().eq("is_vip", 1));
        for (UserEntity userEntity:userEntitieList){
            userEntity.setVipCardNum(Integer.parseInt(commonInfoService.findOne(431).getValue()));
            userService.updateById(userEntity);
        }
    }



}

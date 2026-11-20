package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.response.HomeMessageResponse;
import com.sqx.modules.app.response.UserMessageResponse;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.businessCard.dao.BusinessCardDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayDetailsService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/30
 */
@RestController
@Api(value = "用户管理", tags = {"用户管理"})
@RequestMapping(value = "/user")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private BusinessCardDao businessCardDao;



    @RequestMapping(value = "/selectUserByInvitationCode", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserByInvitationCode(String invitationCode) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByInvitationCode(invitationCode);
        Long userId = userEntity.getUserId();
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        Double money = 0.0;
        if (userMoney != null) {
            money = userMoney.getMoney().doubleValue();
        }
        //查询用户钱包
        Double m = money;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = userMoneyDetailsService.monthIncome(date, userId);
        map.put("userEntity", userEntity);
        map.put("money", m);
        map.put("consume", consume);
        map.put("income", income);
        return Result.success().put("data", map);
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@ApiParam("用户id") @PathVariable Long userId) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByUserId(userId);
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        Double money = 0.0;
        if (userMoney != null) {
            money = userMoney.getMoney().doubleValue();
        }
        //查询用户钱包
        Double m = money;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = userMoneyDetailsService.monthIncome(date, userId);

        map.put("userEntity", userEntity);
        map.put("money", m);
        map.put("consume", consume);
        map.put("income", income);
       /* map.put("takeOrdersCount", takeOrdersCount);
        map.put("myOrdersCount", myOrdersCount);*/
        return Result.success().put("data", map);
    }

    @RequestMapping(value = "/addCannotMoney/{userId}/{money}/{type}", method = RequestMethod.POST)
    @ApiOperation("修改金额")
    @ResponseBody
    public Result addCannotMoney(@PathVariable("userId") Long userId, @PathVariable("money") Double money, @PathVariable("type") Integer type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (type == 1) {
            userMoneyService.updateMoney(1, userId, money);
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统充值：" + money);
            userMoneyDetails.setTitle("系统充值金额");
            userMoneyDetails.setType(1);
            userMoneyDetails.setMoneyType(2);
            userMoneyDetails.setClassify(0);
            userMoneyDetails.setCreateTime(LocalDateTime.now());
            userMoneyDetailsService.save(userMoneyDetails);
        } else {
            userMoneyService.updateMoney(2, userId, money);
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统减少：" + money);
            userMoneyDetails.setTitle("系统减少金额");
            userMoneyDetails.setType(2);
            userMoneyDetails.setMoneyType(2);
            userMoneyDetails.setCreateTime(LocalDateTime.now());
            userMoneyDetailsService.save(userMoneyDetails);
            PayDetails payDetails = new PayDetails();
            payDetails.setState(1);
            payDetails.setCreateTime(DateUtils.format(new Date()));
            payDetails.setOrderId("系统减少金额");
            payDetails.setUserId(userId);
            payDetails.setMoney(money);
            payDetails.setClassify(type);
            payDetailsService.save(payDetails);
        }
        return Result.success();
    }


    @RequestMapping(value = "/selectUserList", method = RequestMethod.GET)
    @ApiOperation("查询所有用户列表")
    @ResponseBody
    public Result selectUserList(Integer page, Integer limit, String startTime, String endTime, UserEntity userEntity) {
        return Result.success().put("data", userService.selectUserPage(page, limit, startTime, endTime, userEntity));
    }


    @RequestMapping(value = "/deleteUserByUserId/{userId}", method = RequestMethod.POST)
    @ApiOperation("删除用户")
    @ResponseBody
    public Result deleteUserByUserId(@PathVariable("userId") Long userId) {
        userService.removeById(userId);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserByUserId", method = RequestMethod.POST)
    @ApiOperation("修改用户")
    @ResponseBody
    public Result updateUserByUserId(@RequestBody UserEntity userEntity) {
        UserEntity userEntity1 = userService.selectUserById(userEntity.getUserId());
        if (StringUtils.isNotBlank(userEntity.getPhone())) {
            if (!userEntity1.getPhone().equals(userEntity.getPhone())) {
                int phone = userService.count(new QueryWrapper<UserEntity>().eq("phone", userEntity.getPhone()));
                if (phone > 0) {
                    return Result.error("当前手机号已被注册！");
                }
            }
        }
        userService.updateById(userEntity);
        return Result.success();
    }





    @RequestMapping(value = "/updateUserStatusByUserId", method = RequestMethod.GET)
    @ApiOperation("禁用或启用用户")
    @ResponseBody
    public Result updateUserByUserId(Long userId) {
        UserEntity byId = userService.getById(userId);
        if (byId.getStatus().equals(1)) {
            byId.setStatus(2);
        } else {
            byId.setStatus(1);
        }
        userService.updateById(byId);
        return Result.success();
    }


    /**
     * 获取openid
     *
     * @param code 微信code
     * @return openid
     */
    @GetMapping("/openId/{code:.+}/{userId}")
    @ApiOperation("根据code获取openid")
    public Result getOpenid(@PathVariable("code") String code, @PathVariable("userId") Long userId) {
        return userService.getOpenId(code, userId);
    }

    /**
     * 信息分析
     *
     * @return
     */
    @GetMapping("/homeMessage")
    @ApiOperation("信息分析")
    public Result homeMessage() {
        HomeMessageResponse homeMessageResponse = new HomeMessageResponse();
        //   0查总   1查天  2查月  3查年
        //设置总用户人数
        homeMessageResponse.setTotalUsers(userService.queryUserCount(0, null));
        //设置今日新增
        homeMessageResponse.setNewToday(userService.queryUserCount(1, null));
        //设置本月新增
        homeMessageResponse.setNewMonth(userService.queryUserCount(2, null));
        //设置本年新增
        homeMessageResponse.setNewYear(userService.queryUserCount(3, null));

        //设置总收入
        homeMessageResponse.setTotalRevenue(userService.queryPayMoney(0));
        //设置今日收入
        homeMessageResponse.setTodayRevenue(userService.queryPayMoney(1));
        //设置本月收入
        homeMessageResponse.setMonthRevenue(userService.queryPayMoney(2));
        //设置本年收入
        homeMessageResponse.setYearRevenue(userService.queryPayMoney(3));

        // // 总名片
        // homeMessageResponse.setCardCount(businessCardDao.countCardNum(null, null, null));
        //
        // // 企业名片
        // homeMessageResponse.setCompanyCardCount(businessCardDao.countCardNum(2));
        //
        // // 个人名片
        // homeMessageResponse.setCompanyCardCount(businessCardDao.countCardNum(1));

        return Result.success().put("data", homeMessageResponse);
    }



    /**
     * 用户分析
     */
    @GetMapping("/userMessage")
    @ApiOperation("用户分析")
    public Result userMessage(String date, int type) {
        UserMessageResponse userMessageResponse = new UserMessageResponse();
        userMessageResponse.setTotalNumber(userService.queryUserCount(type, date));
        userMessageResponse.setVipUserNumber(userService.userMessage(date, type));
        userMessageResponse.setUserNumber(userService.queryUserCount(type, date) - userService.userMessage(date, type));

        userMessageResponse.setH5UserNumber(userService.queryPlatformUserCount(type, date, "H5"));
        userMessageResponse.setMaUserNumber(userService.queryPlatformUserCount(type, date, "小程序"));
        userMessageResponse.setAppUserNumber(userService.queryPlatformUserCount(type, date, "APP"));

        return Result.success().put("data", userMessageResponse);
    }

    /**
     * 用户分析
     */
    @GetMapping("/payMessage")
    @ApiOperation("充值统计")
    public Result payMessage(String date, Integer type) {
        return Result.success().put("data", payDetailsService.payMessage(date, type));
    }

    @ApiOperation("修改推广员")
    @GetMapping("updatePromoter")
    public Result updatePromoter(Long userId, Integer promoter) {
        userService.updatePromoter(userId, promoter);
        return Result.success();
    }
}
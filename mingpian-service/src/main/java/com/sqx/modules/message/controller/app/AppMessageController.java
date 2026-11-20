package com.sqx.modules.message.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.businessCard.service.BusinessCardService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.utils.text.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fang
 * @date 2020/7/13
 */
@Slf4j
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private CommonInfoService commonInfoService;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);


    @Login
    @GetMapping("/getUserMessageInfoList")
    @ApiOperation("消息列表")
    public Result getUserMessageInfoList(Integer page, Integer limit, @RequestAttribute("userId") Long userId, MessageInfo messageInfo) {
        messageInfo.setUserId(userId.toString());
        messageService.updateSendState(userId);
        return Result.success().put("data", messageService.selectMessageList(page, limit, messageInfo));
    }

    @Login
    @GetMapping("/getUserMessageInfoListLimit1")
    @ApiOperation("查看最新消息")
    public Result getUserMessageInfoListLimit1(@RequestAttribute("userId") Long userId, MessageInfo messageInfo) {
        messageInfo.setUserId(userId.toString());
        return Result.success().put("data", messageService.getUserMessageInfoListLimit1(messageInfo));
    }

    @Login
    @PostMapping("/insertMessage")
    @ApiOperation("添加投诉")
    public Result insertMessage(@RequestAttribute("userId") Long userId, @RequestBody MessageInfo messageInfo) {
        if ("8".equals(messageInfo.getState())){
            UserEntity userEntity = userService.getById(userId);
            messageInfo.setTitle(userEntity.getPhone());
            messageInfo.setContent("注销");
            int count = messageService.count(new QueryWrapper<MessageInfo>().eq("user_id", userId).eq("state", 8));
            if (count>0){
                return Result.error("注销申请已提交,请勿重复提交");
            }
        }
        messageInfo.setUserId(String.valueOf(userId));
        messageInfo.setIsSee("2");
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/insertMessageBody")
    @ApiOperation("添加名片申请")
    public Result insertMessageBody(@RequestAttribute("userId") Long userId, @RequestBody MessageInfo messageInfo) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            UserEntity user1 = userService.getById(userId);
            if(user1.getVipCardNum()==null || user1.getVipCardNum()<=0){
                return Result.error("名片申请次数不足！");
            }
            BusinessCard businessCard = businessCardService.getById(messageInfo.getPlatformId());
            messageInfo.setUserId(String.valueOf(businessCard.getUserId()));
            MessageInfo messageInfo1 = messageService.getOne(new QueryWrapper<MessageInfo>()
                    .eq("platform_id", messageInfo.getPlatformId()).eq("user_id",messageInfo.getUserId())
                    .eq("by_user_id", userId).eq("type", 1).eq("state", 6));
            UserEntity byUser = null;
            if(messageInfo1!=null){
                messageInfo1.setStatus(1);
                messageInfo1.setIsSee("0");
                messageInfo1.setCreateAt(DateUtils.format(new Date()));
                messageService.updateById(messageInfo1);
                byUser = userService.getById(messageInfo1.getByUserId());
            }else{
                messageInfo.setByUserId(String.valueOf(userId));
                messageInfo.setIsSee("0");
                messageInfo.setState("6");
                messageInfo.setType("1");
                messageInfo.setStatus(1);
                messageInfo.setCreateAt(DateUtils.format(new Date()));
                UserEntity user2 = userService.getById(messageInfo.getUserId());
                messageInfo.setUserName(user2.getUserName());
                byUser = userService.getById(messageInfo.getByUserId());
                messageInfo.setByUserName(byUser.getUserName());
                messageService.save(messageInfo);

            }
            user1.setVipCardNum(user1.getVipCardNum()-1);
            userService.updateById(user1);

            List<String> msgList=new ArrayList<>();
            msgList.add("申请通知");
            String content = byUser.getUserName()+"发起交换名片申请";
            if(content.length()>20){
                content=content.substring(0,15)+"...";
            }
            msgList.add(content);
            msgList.add("点击查看详情");
            msgList.add(messageInfo.getCreateAt());
            UserEntity user = userService.getById(messageInfo.getUserId());
            if(StringUtils.isNotEmpty(user.getOpenId())){
                String value = commonInfoService.findOne(240).getValue();
                SenInfoCheckUtil.sendMsg(user.getOpenId(),value,msgList,2);
            }
            return Result.success();
        }catch (Exception e){
            log.error("发起申请报错："+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Login
    @PostMapping("/updateMessage")
    @ApiOperation("修改")
    public Result updateMessage(@RequestBody MessageInfo messageInfo) {
        messageService.update(messageInfo);
        MessageInfo messageInfo1 = messageService.getById(messageInfo.getId());
        if("6".equals(messageInfo1.getState()) && "1".equals(messageInfo1.getType())){
            MessageInfo messageInfo2=new MessageInfo();
            messageInfo2.setTitle("申请通知");
            UserEntity user = userService.getById(messageInfo1.getUserId());
            if (messageInfo.getStatus()==2) {
                messageInfo2.setContent(user.getUserName()+"同意了您的交换名片");
            }else{
                messageInfo2.setContent(user.getUserName()+"拒绝了您的交换名片");
            }
            messageInfo2.setUserId(messageInfo1.getByUserId());
            messageInfo2.setState("6");
            messageInfo2.setIsSee("0");
            messageInfo2.setCreateAt(DateUtils.format(new Date()));
            messageService.save(messageInfo2);

            UserEntity byUser = userService.getById(messageInfo1.getByUserId());
            List<String> msgList=new ArrayList<>();
            msgList.add("申请通知");
            String content = messageInfo2.getContent();
            if(content.length()>20){
                content=content.substring(0,15)+"...";
            }
            msgList.add(content);
            msgList.add("点击查看详情");
            msgList.add(messageInfo2.getCreateAt());
            if(StringUtils.isNotEmpty(byUser.getOpenId())){
                String value = commonInfoService.findOne(240).getValue();
                SenInfoCheckUtil.sendMsg(byUser.getOpenId(),value,msgList,2);
            }


        }

        return Result.success();
    }

    @Login
    @PostMapping("/deleteMessage")
    @ApiOperation("删除")
    public Result deleteMessage(Long id) {
        messageService.delete(id);
        return Result.success();
    }

    @Login
    @GetMapping("/getMessageSign")
    @ApiOperation("获取未读消息数量")
    public Result getMessageSign(@RequestAttribute("userId") Long userId) {
        return Result.success().put("data", messageService.getMessageSign(userId));
    }


}
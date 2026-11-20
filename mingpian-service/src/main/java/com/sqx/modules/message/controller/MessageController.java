package com.sqx.modules.message.controller;

import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.businessCard.service.BusinessCardService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.utils.text.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private CommonInfoService commonInfoService;

    @PostMapping("/auditMessage")
    @ApiOperation("审核投诉")
    public Result auditMessage(Long messageId,Integer status,String auditContent){
        return messageService.auditMessage(messageId,status,auditContent);
    }


    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(Integer page, Integer limit, MessageInfo messageInfo){

        return Result.success().put("data",messageService.selectMessageList(page,limit,messageInfo));
    }

    @RequestMapping(value = "/selectMessageByType", method = RequestMethod.GET)
    @ApiOperation("获取消息 type1为公告2位用户反馈 3为投诉服务 4为投诉店铺 5为用户消息 6客服消息")
    @ResponseBody
    public Result selectMessageByType(Integer page, Integer limit,MessageInfo messageInfo){
        return Result.success().put("data",messageService.selectMessageList(page,limit,messageInfo));
    }

    @RequestMapping(value = "/selectMessageDetails", method = RequestMethod.GET)
    @ApiOperation("获取消息详细信息")
    @ResponseBody
    public Result selectMessageDetails(Long id){
        return Result.success().put("data",messageService.selectMessageById(id));
    }

    @RequestMapping(value = "/updateMessage", method = RequestMethod.POST)
    @ApiOperation("修改消息")
    @ResponseBody
    public Result updateMessage(@RequestBody MessageInfo messageInfo){
        return Result.success().put("data",messageService.update(messageInfo));
    }

    @PostMapping("/updateMessageBody")
    @ApiOperation("修改")
    public Result updateMessageBody(@RequestBody MessageInfo messageInfo) {
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

    @RequestMapping(value = "/deleteMessageById", method = RequestMethod.POST)
    @ApiOperation("删除消息")
    @ResponseBody
    public Result deleteMessageById(Long id){
        return Result.success().put("data",messageService.delete(id));
    }

    @RequestMapping(value = "/insertMessage", method = RequestMethod.POST)
    @ApiOperation("添加消息")
    @ResponseBody
    public Result insertMessage(MessageInfo messageInfo){
        return Result.success().put("data",messageService.saveBody(messageInfo));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台公告详情")
    @ResponseBody
    public Result getMessage(@PathVariable Long id) {
        return Result.success().put("data",messageService.selectMessageById(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("管理平台和用户端通用接口添加公告")
    @ResponseBody
    public Result addMessage(@RequestBody MessageInfo messageInfo) {
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("管理平台修改公告接口")
    @ResponseBody
    public Result uUpdate(@RequestBody MessageInfo messageInfo) {
        messageService.update(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台删除公告接口")
    public Result deleteMessage(@PathVariable Long id) {
        messageService.delete(id);
        return Result.success();
    }


}
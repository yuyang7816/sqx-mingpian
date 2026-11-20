package com.sqx.modules.businessCard.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.businessCard.entity.CardStyle;
import com.sqx.modules.businessCard.service.CardStyleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 名片样式(CardStyle)表控制层
 *
 * @author makejava
 * @since 2024-05-06 17:17:18
 */
@RestController
@RequestMapping("/cardStyle")
public class CardStyleController {
    /**
     * 服务对象
     */
    @Resource
    private CardStyleService cardStyleService;

    @ApiOperation("分页查询名片样式信息")
    @PostMapping("/page")
   public Result queryByPage(@RequestBody JSONObject params){
       Result result = cardStyleService.queryByPage(params);
       return result;
   }

   @ApiOperation("新增名片样式")
   @PostMapping("/save")
   public Result saveStyle(@RequestBody CardStyle cardStyle){
       Result result = cardStyleService.saveInfo(cardStyle);
       return result;
   }

    @ApiOperation("修改名片样式")
    @PostMapping("/edit")
    public Result editStyle(@RequestBody CardStyle cardStyle){
        Result result = cardStyleService.editInfo(cardStyle);
        return result;
    }

    @ApiOperation("根据id查询样式")
    @GetMapping("/queryById/{id}")
    public Result queryById(@PathVariable String id){
        Result result = cardStyleService.queryById(id);
        return result;
    }

    @ApiOperation("根据id删除")
    @GetMapping("/del/{id}")
    public Result delById(@PathVariable String id){
        Result result = cardStyleService.delById(id);
        return result;
    }



}


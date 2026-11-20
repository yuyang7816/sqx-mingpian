package com.sqx.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.sys.entity.SysDictEntity;
import com.sqx.modules.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 数据字典
 *
 */
@RestController
@RequestMapping("sys/dict")
public class SysDictController {
    @Autowired
    private SysDictService sysDictService;


    @RequestMapping("/selectDictList")
    public Result selectDictList(String type){
        SysDictEntity sysDictEntity = sysDictService.getOne(new QueryWrapper<SysDictEntity>().eq("type", type));
        List<SysDictEntity> sysDictEntityList = sysDictService.list(new QueryWrapper<SysDictEntity>().eq( "parent_id", sysDictEntity.getId()).orderByDesc("order_num"));
        return Result.success().put("data", sysDictEntityList);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(Integer page,Integer limit,String name,String parentId,String type){
        PageUtils pages = sysDictService.queryPage(page, limit, name, parentId, type);

        return Result.success().put("page", pages);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id){
        SysDictEntity dict = sysDictService.getById(id);

        return Result.success().put("dict", dict);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody SysDictEntity dict){

        sysDictService.save(dict);

        return Result.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody SysDictEntity dict){
        sysDictService.updateById(dict);

        return Result.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
        sysDictService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}

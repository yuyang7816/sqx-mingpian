package com.sqx.modules.sys.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.sys.entity.SysDictEntity;
import com.sqx.modules.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典
 *
 */
@RestController
@RequestMapping("/app/dict/")
public class AppDictController {
    @Autowired
    private SysDictService sysDictService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(String type){
        List<SysDictEntity> sysDictEntityList = new ArrayList<>();
        SysDictEntity sysDictEntity = sysDictService.getOne(new QueryWrapper<SysDictEntity>().eq("type", type));
        if (sysDictEntity!=null){
            sysDictEntityList  = sysDictService.list(new QueryWrapper<SysDictEntity>().eq( "parent_id", sysDictEntity.getId()).orderByDesc("order_num"));
        }
        return Result.success().put("data", sysDictEntityList);
    }


}

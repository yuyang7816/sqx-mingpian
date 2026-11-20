package com.sqx.modules.industry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.industry.entity.Industry;
import com.sqx.modules.industry.dao.IndustryDao;
import com.sqx.modules.industry.service.IndustryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.sqx.common.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 行业分类 服务实现类
 * @author wuChang
 * @since 2024-02-26
 */
@Service
public class IndustryServiceImp extends ServiceImpl<IndustryDao, Industry> implements IndustryService {


    @Override
    public Result saveIndustry(Industry industry) {
        if (industry.getParentId() != null && industry.getParentId() != 0) {
            Industry parentIndustry = baseMapper.selectById(industry.getParentId());
            if (parentIndustry == null) {
                return Result.error("上级不存在");
            }
        }
        if (industry.getIndustryId() != null) {
            return Result.upStatus(baseMapper.updateById(industry));
        } else {
            industry.setCreateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.insert(industry));
        }
    }

    @Override
    public IPage<Industry> getIndustryList(Integer page, Integer limit, Industry industry) {
        Page<Industry> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(industry).orderByAsc("sort"));
    }

    @Override
    public Result deleteIndustryById(Long industryId) {
        Industry industry = baseMapper.selectById(industryId);

        Integer count = baseMapper.selectCount(new QueryWrapper<Industry>().eq("parent_id", industry.getIndustryId()));
        if (count > 0) {
            return Result.error("请先删除下级行业");
        }
        return Result.upStatus(baseMapper.deleteById(industryId));

    }

    @Override
    public List<Industry> getIndustryTreeList() {
        List<Industry> industryList = baseMapper.selectList(new QueryWrapper<Industry>().eq("is_enable", 1));
        return getIndustryTree(industryList);
    }


    private List<Industry> getIndustryTree(List<Industry> records) {
        return records.stream()
                .filter(e -> e.getParentId() == 0)
                .peek((menu) -> menu.setChildrenList(getChildrenList(menu, records)))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

    //递归查找所有菜单的子菜单
    private List<Industry> getChildrenList(Industry root, List<Industry> all) {
        return all.stream().filter(categoryEntity -> categoryEntity.getParentId().equals(root.getIndustryId())).peek(categoryEntity -> {
            //1、找到子菜单(递归)
            categoryEntity.setChildrenList(getChildrenList(categoryEntity, all));
        }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(Collectors.toList());
    }

}

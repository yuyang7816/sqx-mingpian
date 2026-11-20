package com.sqx.modules.companyCardTemplate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.companyCardTemplate.entity.CompanyTemplate;
import com.sqx.modules.companyCardTemplate.dao.CompanyTemplateDao;
import com.sqx.modules.companyCardTemplate.service.CompanyTemplateService;
import com.sqx.modules.utils.text.StringUtils;
import com.sqx.modules.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 公司名片模板(CompanyTemplate)表服务实现类
 *
 * @author makejava
 * @since 2024-05-09 11:32:35
 */
@Service("companyTemplateService")
public class CompanyTemplateServiceImpl extends ServiceImpl<CompanyTemplateDao, CompanyTemplate> implements CompanyTemplateService {
    @Resource
    private CompanyTemplateDao companyTemplateDao;


    @Override
    public Result saveCompanyTemp(CompanyTemplate companyTemplate) {
        companyTemplate.setId(IdUtils.fastSimpleUUID());
        companyTemplate.setCreateTime(LocalDateTime.now());
        companyTemplateDao.insert(companyTemplate);
        return Result.success();
    }

    @Override
    public Result queryTemplate(JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);

        Long cardId = params.getLong("cardId");
        String userName = params.getString("userName");
        String name = params.getString("name");
        String phone = params.getString("phone");
        String email = params.getString("email");

        List<CompanyTemplate> companyTemplates = companyTemplateDao
                .selectList(new QueryWrapper<CompanyTemplate>()
                        .eq(Objects.nonNull(cardId), "company_card_id", cardId)
                        .like(StringUtils.isNotBlank(name), "template_name", name)
                        .like(StringUtils.isNotBlank(userName), "user_name", userName)
                        .like(StringUtils.isNotBlank(phone), "phone", phone)
                        .like(StringUtils.isNotBlank(email), "email", email)
                );
        PageInfo<CompanyTemplate> companyTemplatePageInfo = new PageInfo<>(companyTemplates);
        return Result.success().put("data",companyTemplatePageInfo);
    }

    @Override
    public Result updateTemplate(CompanyTemplate companyTemplate) {
        this.updateById(companyTemplate);
        return Result.success();
    }

    @Override
    public Result info(Long companyCardId) {
        CompanyTemplate companyTemplate = this.getOne(new LambdaQueryWrapper<CompanyTemplate>()
                .eq(CompanyTemplate::getCompanyCardId, companyCardId)
        );

        return Result.success().put("data",companyTemplate);
    }

    @Override
    public Result saveTemplate(CompanyTemplate companyTemplate) {
        this.save(companyTemplate);
        return Result.success();
    }
}

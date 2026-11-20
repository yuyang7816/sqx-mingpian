package com.sqx.modules.companyCard.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.eum.IntegerEnum;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.Result;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.businessCard.service.BusinessCardService;
import com.sqx.modules.companyCard.dao.CompanyStaffRecordDao;
import com.sqx.modules.companyCard.domain.vo.CompanyStaffRecordVo;
import com.sqx.modules.companyCard.domain.vo.StaffDataVo;
import com.sqx.modules.companyCard.entity.CompanyStaffRecord;
import com.sqx.modules.companyCard.service.CompanyStaffRecordService;
import com.sqx.modules.companyDynamic.service.CompanyDynamicRecordService;
import com.sqx.modules.utils.text.StringUtils;
import com.sqx.modules.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 公司员工动态信息(CompanyStaffRecord)表服务实现类
 *
 * @author makejava
 * @since 2024-05-09 13:56:21
 */
@Service("companyStaffRecordService")
public class CompanyStaffRecordServiceImpl extends ServiceImpl<CompanyStaffRecordDao, CompanyStaffRecord> implements CompanyStaffRecordService {
    @Resource
    private CompanyStaffRecordDao companyStaffRecordDao;
    @Resource
    private BusinessCardService businessCardService;
    @Resource
    private CompanyDynamicRecordService companyDynamicRecordService;


    @Override
    public Result applyJoin(Long usreId, JSONObject params) {
        BusinessCard businessCard = businessCardService.queryUserShowCard(usreId,1);
        if (StringUtils.isNull(businessCard)){
            return Result.error("请先创建自己名片在来申请！");
        }

        Long cardId = params.getLong("comCardId");
        String string = params.getString("applyContent");

        // 校验是否已加入企业
        Integer count = companyStaffRecordDao.selectCount(new LambdaQueryWrapper<CompanyStaffRecord>()
                .eq(CompanyStaffRecord::getCardId, cardId)
                .eq(CompanyStaffRecord::getStaffCardId, businessCard.getCardId())
        );
        if (0 < count){
            throw new SqxException("已加入该企业");
        }

        //企业名片id
        CompanyStaffRecord companyStaffRecord = new CompanyStaffRecord();
        companyStaffRecord.setId(IdUtils.fastSimpleUUID());
        companyStaffRecord.setCardId(cardId);
        companyStaffRecord.setStaffUserId(usreId);
        companyStaffRecord.setStaffCardId(businessCard.getCardId());
        companyStaffRecord.setStaffSource(0);
        companyStaffRecord.setApplyContent(string);
        companyStaffRecord.setStaffSattus(1);
        companyStaffRecord.setApplyTime(LocalDateTime.now());
        companyStaffRecord.setCreateTime(LocalDateTime.now());
        companyStaffRecordDao.insert(companyStaffRecord);
        return Result.success();
    }

    @Override
    public Result queryApplyRecord(JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Map<String, Object>> applyList = companyStaffRecordDao.queryApplyList(params.getLong("cardId"));
        PageInfo<Map<String, Object>> companyStaffRecordPageInfo = new PageInfo<>(applyList);
        return Result.success().put("data",companyStaffRecordPageInfo);
    }

    /**
     * 审核申请
     * @param params
     * @return
     */
    @Override
    public Result auditApply(JSONObject params) {
        String string = params.getString("recordId");
        Integer status = params.getInteger("status");
        String auditContent = params.getString("content");
        if (StringUtils.isEmpty(string)){
            return Result.error("记录id不可以为空！");
        }
        CompanyStaffRecord companyStaffRecord = companyStaffRecordDao.selectById(string);
        if (StringUtils.isNull(companyStaffRecord)){
            return Result.error("申请记录不存在！");
        }
        companyStaffRecord.setStaffSattus(status);
        companyStaffRecord.setAuditContent(auditContent);
        companyStaffRecord.setJoinTime(LocalDateTime.now());
        companyStaffRecordDao.updateById(companyStaffRecord);
        if (status.equals(1)){
            //审核通过
            //企业名片id
            Long cardId = companyStaffRecord.getCardId();
            //用户名片id
            Long staffCardId = companyStaffRecord.getStaffCardId();
            BusinessCard comCardInfo = businessCardService.getById(cardId);
            BusinessCard staffCardInfo = businessCardService.getById(staffCardId);
            staffCardInfo.setParentId(cardId);
            staffCardInfo.setCompanyName(comCardInfo.getCompanyName());
            staffCardInfo.setCompanyAmbient(comCardInfo.getCompanyAmbient());
            staffCardInfo.setCompanyAddress(comCardInfo.getCompanyAddress());
            staffCardInfo.setCompanyFlairs(comCardInfo.getCompanyFlairs());
            staffCardInfo.setCompanyBlurb(comCardInfo.getCompanyBlurb());
            staffCardInfo.setCompanyArea(comCardInfo.getCompanyArea());
            staffCardInfo.setCompanyCity(comCardInfo.getCompanyCity());
            staffCardInfo.setCompanyDistrict(comCardInfo.getCompanyDistrict());
            staffCardInfo.setCompanyLogo(comCardInfo.getCompanyLogo());
            staffCardInfo.setCompanyProvince(comCardInfo.getCompanyProvince());
            businessCardService.updateById(staffCardInfo);
            //增加企业员工动态记录
            companyDynamicRecordService.saveRecord(cardId,staffCardId,1,null,null);
        }
        return Result.success();
    }

    @Override
    public StaffDataVo staffData(Long cardId) {
        LambdaQueryWrapper<CompanyStaffRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(CompanyStaffRecord::getCardId, cardId)
                .eq(CompanyStaffRecord::getStaffSattus, IntegerEnum.COM_STAFF_STATUSE_ED.getCode());

        Integer count = companyStaffRecordDao.selectCount(queryWrapper);

        StaffDataVo vo = new StaffDataVo();
        vo.setTotalNum(Long.valueOf(count));
        return vo;
    }

    @Override
    public Result pageInfo(Integer pageNum, Integer pageSize, Long cardId, Integer status) {
        if (Objects.isNull(pageNum) || 0 == pageNum){
            pageNum = 1;
        }

        if (Objects.isNull(pageSize) || 0 == pageSize){
            pageSize = 10;
        }
        Page<CompanyStaffRecord> initPage = new Page<>(pageNum, pageSize);

        IPage<CompanyStaffRecordVo> voIPage = this.baseMapper.pageInfo(initPage, cardId, status);

        return Result.success().put("data", voIPage);
    }

    @Override
    public Integer applyCount(Long cardId) {
        return this.count(new LambdaQueryWrapper<CompanyStaffRecord>()
                .eq(CompanyStaffRecord::getCardId, cardId)
                .eq(CompanyStaffRecord::getStaffSattus, 0)
        );
    }
}

package com.sqx.modules.businessCard.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.eum.IntegerEnum;
import com.sqx.common.eum.StringEnum;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.QRCodeUtil;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.businessCard.dao.BusinessCardDao;
import com.sqx.modules.businessCard.entity.BusinessCard;
import com.sqx.modules.businessCard.entity.CardStyle;
import com.sqx.modules.businessCard.entity.VisitCardRecord;
import com.sqx.modules.businessCard.service.BusinessCardService;
import com.sqx.modules.businessCard.service.CardStyleService;
import com.sqx.modules.businessCard.service.VisitCardRecordService;
import com.sqx.modules.businessCard.vo.CityVo;
import com.sqx.modules.cardList.entity.UserCardList;
import com.sqx.modules.cardList.service.UserCardListService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.companyCard.dao.CompanyStaffRecordDao;
import com.sqx.modules.companyCard.entity.CompanyStaffRecord;
import com.sqx.modules.companyCard.service.CompanyStaffRecordService;
import com.sqx.modules.companyCardTemplate.entity.CompanyTemplate;
import com.sqx.modules.companyCardTemplate.service.CompanyTemplateService;
import com.sqx.modules.industry.entity.Industry;
import com.sqx.modules.industry.service.IndustryService;
import com.sqx.modules.invite.entity.InviteRecord;
import com.sqx.modules.invite.service.InviteRecordService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.search.dao.SearchDao;
import com.sqx.modules.search.entity.Search;
import com.sqx.modules.utils.BaiDuUtils;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.utils.text.StringUtils;
import com.sqx.modules.utils.uuid.IdUtils;
import com.sqx.modules.verify.db.entity.VerifyRecord;
import com.sqx.modules.verify.db.service.VerifyRecordDbService;
import com.sqx.modules.vipDetails.service.VipDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * 名片管理 服务实现类
 * @author wuChang
 * @since 2024-02-26
 */
@Service
@Slf4j
public class BusinessCardServiceImp extends ServiceImpl<BusinessCardDao, BusinessCard> implements BusinessCardService {
    @Autowired
    private UserService userService;
    @Autowired
    private VipDetailsService vipDetailsService;
    @Autowired
    private IndustryService industryService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MessageService messageService;
    @Resource
    private VisitCardRecordService visitCardRecordService;
    @Resource
    private CompanyStaffRecordService companyStaffRecordService;
    @Resource
    private CardStyleService cardStyleService;
    @Resource
    private InviteRecordService inviteRecordService;
    @Resource
    private SearchDao searchDao;
    @Resource
    private CompanyTemplateService companyTemplateService;
    @Resource
    private VerifyRecordDbService verifyRecordDbService;
    @Autowired
    private UserCardListService userCardListService;
    @Resource
    private CompanyStaffRecordDao companyStaffRecordDao;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public Result saveBusinessCard(BusinessCard businessCard) {
        UserEntity userEntity = userService.getById(businessCard.getUserId());
        if (StringUtils.isNull(userEntity)) {
            return Result.error("用户信息不存在，请联系管理员！");
        }
        String cardAvatar = businessCard.getCardAvatar();
        if (StringUtils.isEmpty(cardAvatar)){
            businessCard.setCardAvatar(userEntity.getAvatar());
        }
        String cardStyleCode = businessCard.getCardStyleCode();
        if (StringUtils.isEmpty(cardStyleCode)){
            //设置默认样式
            businessCard.setCardStyleCode(StringEnum.CARD_STYLE_DEFAULT.getCode());
        }

        //写入行业分类
        Industry firstIndustry = industryService.getById(businessCard.getIndustryFirstId());
        Industry secondIndustry = industryService.getById(businessCard.getIndustrySecondId());
        if (firstIndustry != null && secondIndustry != null) {
            businessCard.setIndustryFirstName(firstIndustry.getIndustryName());
            businessCard.setIndustrySecondName(secondIndustry.getIndustryName());
        } else {
            return Result.error("一级或二级行业分类不存在");
        }
            if (businessCard.getCardId() != null) {
                int update = baseMapper.updateById(businessCard);
                return Result.upStatus(update);
            } else {
                businessCard.setCreateTime(LocalDateTime.now());
                int insert = baseMapper.insert(businessCard);
                return Result.upStatus(insert);
            }

    }

    @Override
    public Result getBusinessCardList(JSONObject params) {
        Integer pageSize = params.getInteger("pageSize");
        Integer pageNum = params.getInteger("pageNum");
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);
        List<BusinessCard> businessCardList = baseMapper.getBusinessCardList(params.getString("userName"),
                params.getString("companyName"),
                params.getInteger("cardStatus"),
                params.getInteger("userType"),
                params.getString("realName"),
                params.getString("industryFirstName"),
                params.getInteger("auditStatus"));
        PageInfo<BusinessCard> businessCardPageInfo = new PageInfo<>(businessCardList);
        return Result.success().put("data",businessCardPageInfo);
    }

    @Override
    public Result deleteBusinessCardById(Long cardId) {
        return Result.upStatus(baseMapper.deleteById(cardId));

    }

    @Override
    public Result getBusinessCardById(JSONObject params,Long userId) {
        Long cardId= params.getLong("cardId");
        String string = params.getString("source");
        BusinessCard businessCard = baseMapper.selectById(cardId);
        if (StringUtils.isNotNull(businessCard)) {
            Long userId1 = businessCard.getUserId();
            if (!userId.equals(userId1)){
                //访问用户的信息
                UserEntity byId = userService.getById(userId);
                VisitCardRecord visitCardRecord = new VisitCardRecord();
                visitCardRecord.setId(IdUtils.fastSimpleUUID());
                visitCardRecord.setCardId(cardId);
                visitCardRecord.setUserId(userId);
                visitCardRecord.setUserName(byId.getUserName());
                visitCardRecord.setVisitTime(LocalDateTime.now());
                visitCardRecord.setCreateTime(LocalDateTime.now());
                visitCardRecord.setVisitSource(string);
                //流量次数增加
                businessCard.setBrowseCount(businessCard.getBrowseCount() + 1);
                baseMapper.updateById(businessCard);
                //增加访问记录
                visitCardRecordService.save(visitCardRecord);
                return Result.success().put("data",businessCard);
            }
            return Result.success().put("data",businessCard);
        }else {
            return Result.error("未查询到名片信息，请刷新重试！");
        }
    }

    /**
     * 上架或下架名片
     * @param userId
     * @param type
     */
    @Override
    public Result downOrUp(Long userId, Long carId, Integer type) {
        QueryWrapper<BusinessCard> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (carId != null) {
            wrapper.eq("car_id", carId);
        }
        BusinessCard businessCard = baseMapper.selectOne(wrapper);
        if (businessCard != null) {
            if (type == 1) {
                businessCard.setCardStatus(1);
            } else {
                businessCard.setCardStatus(3);
            }
            return Result.upStatus(baseMapper.updateById(businessCard));
        } else {
            return Result.error("当前用户没有发布名片");
        }

    }

    @Override
    public Result examineCard(JSONObject params) {
        Long cardId = params.getLong("cardId");
        String examineRemark = params.getString("examineRemark");
        Integer status = params.getInteger("status");
        BusinessCard businessCard = baseMapper.selectById(cardId);
        UserEntity userEntity = userService.getById(businessCard.getUserId());
        businessCard.setExamineRemark(examineRemark);
        if (status == 1) {
            businessCard.setAuditStatus(1);

            // 保存企业名片模版
            saveCompanyCardTemplate(businessCard);
        } else {
            businessCard.setAuditStatus(2);
        }
        if (StringUtils.isNotBlank(userEntity.getOpenId())) {
            List<String> msgList = new ArrayList<>();
            msgList.add("名片审核通知");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (status == 1) {
                msgList.add("审核已通过");
            } else {
                msgList.add("审核未通过");
            }
            String str = "";
            if (StringUtils.isNotBlank(examineRemark)) {
                if (examineRemark.length() > 20) {
                    str = examineRemark.substring(0, 16) + "...";
                } else {
                    str = examineRemark;
                }
            } else {
                str = "点击查看详情";
            }

            CommonInfo one = commonInfoService.findOne(292);
            msgList.add(str);
            msgList.add(fmt.format(LocalDateTime.now()));
            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
        }
        String content = "";
        MessageInfo messageInfo = new MessageInfo();
        if (status == 1) {
            content = "您发布的名片审核已通过";

        } else {
            content = "您发布的名片审核未通过,请在名片详情页面查看原因";
        }
        messageInfo.setContent(content);
        messageInfo.setTitle("名片审核通知");
        messageInfo.setState(String.valueOf(6));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);

        if (StringUtils.isNotEmpty(userEntity.getClientid())) {
            userService.pushToSingle("发布名片审核通知", content, userEntity.getClientid());
        }

        // 如果用户只有一个名片, 默认显示
        int count = this.count(new LambdaQueryWrapper<BusinessCard>()
                .eq(BusinessCard::getUserId, businessCard.getUserId())
                .eq(BusinessCard::getAuditStatus, 1)
                .eq(BusinessCard::getCardStatus, 1)
        );
        if (0 == count) {
            businessCard.setCardStatus(1);
        }

        return Result.upStatus(baseMapper.updateById(businessCard));
    }

    // 保存企业模版
    private void saveCompanyCardTemplate(BusinessCard businessCard) {
        CompanyTemplate companyTemplate = new CompanyTemplate();
        companyTemplate.setId(UUID.randomUUID().toString().replace("-", ""));
        companyTemplate.setCompanyCardId(Math.toIntExact(businessCard.getCardId()));
        companyTemplate.setCompanyName("默认企业名片模版");
        companyTemplate.setBackImg(businessCard.getCardImgBack());
        companyTemplate.setCompanyLogo(businessCard.getCompanyLogo());
        companyTemplate.setUserName(businessCard.getRealName());
        companyTemplate.setCompanyName(businessCard.getCompanyName());
        companyTemplate.setCompanyuPisition(businessCard.getCompanyPosition());
        companyTemplate.setPhone(businessCard.getCardPhone());
        companyTemplate.setEmail(businessCard.getEmail());
        companyTemplate.setCompanyAddress(businessCard.getCompanyAddress());
        companyTemplate.setCompanyDesc(businessCard.getCompanyBlurb());
        companyTemplate.setCreateTime(LocalDateTime.now());

        companyTemplateService.save(companyTemplate);
    }

    @Override
    public Result getBusinessCard(Long userId) {
        List<BusinessCard> businessCardList = baseMapper.selectList(new QueryWrapper<BusinessCard>().eq("user_id", userId));
        return Result.success().put("data",businessCardList);
    }

    @Override
    public List<CityVo> getCity() {
        List<CityVo> provinceVoList = baseMapper.getProvinceList();
        for (CityVo cityVo : provinceVoList) {
            List<CityVo> cityList = baseMapper.getCityList(cityVo.getCity());
            cityVo.setCityVoList(cityList);
        }
        return provinceVoList;
    }

    @Override
    public Result getMyBusinessCardList(Long userId,JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        if (ObjectUtil.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        if (ObjectUtil.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);
        List<BusinessCard> businessCardList = baseMapper.queryBusinessCardList(userId, params.getInteger("cardType"));
        PageInfo<BusinessCard> businessCardPageInfo = new PageInfo<>(businessCardList);
        return Result.success().put("data",businessCardPageInfo);
    }

    @Override
    public Result saveBusinessCard(Long userId, BusinessCard businessCard) {
        // 自动审核文本
        if (autoCheck(businessCard.getWorkExperience() + "|"
                + businessCard.getEduExperience() + "|"
                + businessCard.getRealName())) {
            throw new SqxException("百度云审核不通过");
        }

        //用户信息
        UserEntity userEntity = userService.queryByUserId(userId);
        if (ObjectUtil.isNull(userEntity)){
            return Result.error("用户信息不存在，请联系管理员！");
        }
        if (ObjectUtil.isNull(businessCard.getCardType())){
            businessCard.setCardType(2);
        }
        businessCard.setUserId(userId);
        String cardAvatar = businessCard.getCardAvatar();
        if (StringUtils.isEmpty(cardAvatar)){
            businessCard.setCardAvatar(userEntity.getAvatar());
        }
        if (businessCard.getCardType().equals(2)){
            //企业用户 判断是否已经有名片
            // BusinessCard businessCard1 = baseMapper.queryCompanyShowCard(userId);
            // if (StringUtils.isNotNull(businessCard1)){
            //     return Result.error("已拥有企业名片，不允许多次创建！");
            // }

            CommonInfo commonInfo = commonInfoService.findOne(314);
            if (ObjectUtil.isNull(commonInfo)){
                log.error("系统配置不存在: {}", 314);
                throw new SqxException("系统配置不存在");
            }

            businessCard.setQuota(Integer.valueOf(commonInfo.getValue()));
            businessCard.setUsedQuota(0);
        }
        String cardStyleCode = businessCard.getCardStyleCode();
        if (StringUtils.isEmpty(cardStyleCode)){
            businessCard.setCardStyleCode(StringEnum.CARD_STYLE_DEFAULT.getCode());
        }
        //写入行业分类
        Industry firstIndustry = industryService.getById(businessCard.getIndustryFirstId());
        Industry secondIndustry = industryService.getById(businessCard.getIndustrySecondId());
        if (firstIndustry != null && secondIndustry != null) {
            businessCard.setIndustryFirstName(firstIndustry.getIndustryName());
            businessCard.setIndustrySecondName(secondIndustry.getIndustryName());
        } else {
            return Result.error("一级或二级行业分类不存在");
        }
        //获取到样式编码
        String styleCode = businessCard.getCardStyleCode();
        if (StringUtils.isEmpty(styleCode)){
            styleCode="default";
        }
        //判断样式是免费的还是 vip可以使用 然后根据用户是否是会员判断是否允许设置
        CardStyle cardStyle = cardStyleService.queryInfoByStyleCode(styleCode);
        if (ObjectUtil.isNull(cardStyle)){
            return Result.error("未查询到名片样式");
        }
        Integer isFree = cardStyle.getIsFree();
        if (isFree.equals(1)){
            Integer isVip = userEntity.getIsVip();
            if (isVip.equals(0)){
                return Result.error("非会员不可以使用会员名片样式！");
            }
        }

        if (businessCard.getCardId() != null) {
            int update = baseMapper.updateById(businessCard);
            return Result.upStatus(update);
        } else {
            //邀请加入的企业id
            Long invitationCardId = businessCard.getInvitationCardId();
            if (ObjectUtil.isNotNull(invitationCardId)){
                businessCard.setParentId(invitationCardId);
            }
            //用户邀请码
            String invitationCode = businessCard.getInvitationCode();
            businessCard.setCreateTime(LocalDateTime.now());

            businessCard.setCompanyFlair(JSONObject.toJSONString(businessCard.getCompanyFlairs()));
            // 个人名片不需要审核
            if (1 == businessCard.getCardType()) {
                businessCard.setAuditStatus(1);

                // 如果没有名片, 默认展示
                Integer count = this.baseMapper.selectCount(new LambdaQueryWrapper<BusinessCard>()
                        .eq(BusinessCard::getUserId, userId)
                );

                if (0 >= count){
                    businessCard.setCardStatus(1);
                }

            } else if (2 == businessCard.getCardType()) {
                businessCard.setAuditStatus(0);
            } else {
                throw new SqxException("不支持的名片类型");
            }
            int insert = baseMapper.insert(businessCard);
            saveInviteInfo(businessCard,invitationCode,invitationCardId,userEntity);
            return Result.upStatus(insert);
        }
    }

    private boolean autoCheck(String text) {
        return BaiDuUtils.auditBaiDuText(text);
    }

    /**
     * 保存邀请信息
     */
    public void saveInviteInfo(BusinessCard businessCard,String invitationCode,Long invitationCardId,UserEntity userEntity){
        if (StringUtils.isNotEmpty(invitationCode)){
            //邀请人信息
            UserEntity inviteUser = userService.queryByInvitationCode(invitationCode);
            InviteRecord inviteRecord = new InviteRecord();
            inviteRecord.setInviterId(inviteUser.getUserId());
            inviteRecord.setInviteeId(userEntity.getUserId());
            inviteRecord.setInviteeName(userEntity.getUserName());
            inviteRecord.setInviteePortrait(userEntity.getAvatar());
            inviteRecord.setInviteeCardId(businessCard.getCardId());
            inviteRecord.setInviterCardId(invitationCardId);
            inviteRecord.setInviteTitle("邀请加入企业");
            inviteRecord.setInviteType(1);
            inviteRecordService.saveInfo(inviteRecord);
        }
    }

    @Override
    public Result updateBusinessCard(BusinessCard businessCard) {
        // 自动审核文本
        if (autoCheck(businessCard.getWorkExperience() + "|"
                + businessCard.getEduExperience() + "|"
                + businessCard.getRealName())) {
            throw new SqxException("百度云审核不通过");
        }

        if (CollectionUtils.isNotEmpty(businessCard.getCompanyFlairs())){
            businessCard.setCompanyFlair(JSONObject.toJSONString(businessCard.getCompanyFlairs()));
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(businessCard.getCardStyleCode())){
            CardStyle cardStyle = cardStyleService.queryInfoByStyleCode(businessCard.getCardStyleCode());
            if (Objects.isNull(cardStyle)){
                throw new SqxException("名片样式代码不存在");
            }

            businessCard.setCardImg(cardStyle.getStyleImg());
            businessCard.setCardImgBack(cardStyle.getStyleImg());
        }

        if (2 == businessCard.getCardType()){
            businessCard.setAuditStatus(0);
        }

        int update = baseMapper.updateById(businessCard);
        return Result.upStatus(update);
    }

    @Override
    public Result queryAllList(Long userId, JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        if (ObjectUtil.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        if (ObjectUtil.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);
        Integer auditStatus = params.getInteger("auditStatus");
        Integer cardType = params.getInteger("cardType");
        List<BusinessCard> businessCardList = baseMapper.queryAllList(
                userId,
                auditStatus,
                cardType
        );
        PageInfo<BusinessCard> businessCardPageInfo = new PageInfo<>(businessCardList);
        return Result.success().put("data",businessCardPageInfo);
    }

    @Override
    public Result queryShowCard(Long userId) {
        if (StringUtils.isNull(userId)){
            return Result.error("请确认是否登录！");
        }

        BusinessCard businessCard = baseMapper.queryShowCard(userId, null);
        return Result.success().put("data",businessCard);
    }

    @Override
    public Result eidtShowCard(Long userId, Long cardId) {
        //获取到要修改的名片id
        if (StringUtils.isNull(cardId)){
            return Result.error("名片id不可以为空！");
        }
        //根据用户id查询现在展示的名片信息
        BusinessCard businessCard = baseMapper.queryShowCard(userId,null);
        if (StringUtils.isNotNull(businessCard)){
            //存在则修改为不展示
            businessCard.setCardStatus(IntegerEnum.CARD_STATUS_N.getCode());
            baseMapper.updateById(businessCard);
            Result result = showCard(cardId, userId);
            return result;
        }else {
            //不存在 则直接设置本次切换的为展示
            Result result = showCard(cardId, userId);
            return result;
        }
    }
    public Result showCard(Long cardId,Long userId){
        BusinessCard businessCard = baseMapper.selectOne(new QueryWrapper<BusinessCard>().eq("card_id", cardId).eq("user_id", userId));
        if (ObjectUtil.isNull(businessCard)){
            return Result.error("名片信息不存在！");
        }
        Integer auditStatus = businessCard.getAuditStatus();
        if (auditStatus.equals(IntegerEnum.CARE_AUDIT_PASS.getCode())){
            //s审核通过
            businessCard.setCardStatus(IntegerEnum.CARD_STATUS_Y.getCode());
            baseMapper.updateById(businessCard);
            return Result.success();
        }else if (auditStatus.equals(IntegerEnum.CARE_AUDIT_ING.getCode())){
            return Result.error("名片暂未审核通过不允许设置为展示名片！");
        }else {
            return Result.error("名片审核未通过不允许设置为展示名片！");
        }
    }


    @Override
    public Result addBrowseCount(Long cardId) {
        int i = baseMapper.updateBrowseCount(cardId);
        return Result.upStatus(i);
    }


    @Override
    public int addBrowseCounts(Long cardId) {
        int i = baseMapper.updateBrowseCount(cardId);
        return i;
    }

    /**
     * 根据用户id查询用户展示名片
     * @param userId
     * @return
     */
    @Override
    public Result queryCardByUserId(Long userId) {
        BusinessCard businessCard = baseMapper.queryShowCard(userId,null);
        return Result.success().put("data",businessCard);
    }

    @Override
    public BusinessCard queryUserShowCard(Long userId,Integer userType) {
        BusinessCard businessCard = baseMapper.queryShowCard(userId,userType);
        return businessCard;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveStaffCard(Long userId, Long cardId, Long companyCardId) {
        //查询登录用户的信息
        UserEntity userEntity = userService.getById(userId);
        // Integer userType = userEntity.getUserType();
        // //判断该用户是不是企业用户
        // if (!userType.equals(2)){
        //     return Result.error("不是企业用户不允许添加员工名片");
        // }

        int count = companyStaffRecordService.count(new LambdaQueryWrapper<CompanyStaffRecord>()
                .eq(CompanyStaffRecord::getStaffCardId, cardId)
                .eq(CompanyStaffRecord::getCardId, companyCardId)
        );
        if (0 < count){
            throw new SqxException("该名片已在企业中");
        }

        // 不是会员要使用额度
        if (0 == userEntity.getIsVip()) {
            BusinessCard companyBusinessCard = this.baseMapper.selectById(companyCardId);
            if (Objects.isNull(companyBusinessCard)){
                throw new SqxException("企业不存在");
            }

            if (companyBusinessCard.getUsedQuota() >= companyBusinessCard.getQuota()) {
                throw new SqxException("额度已用完");
            }

            // 额度-1
            this.baseMapper.decrQuota(companyCardId, 1);
        }

        //判断该企业用户是否拥有企业名片
        BusinessCard businessCard1 = baseMapper.queryCompanyShowCard(userId);
        if (StringUtils.isNull(businessCard1)){
            return Result.error("暂无企业名片信息，请创建后再来进行员工名片录入");
        }
        //员工用户id
        BusinessCard businessCard = this.baseMapper.selectById(cardId);
        if (Objects.isNull(businessCard)){
            throw new SqxException("名片信息不存在");
        }

        Long userId1 = businessCard.getUserId();
        UserEntity staffInfo = userService.getById(userId1);
        if (StringUtils.isNull(staffInfo)){
            return Result.error("员工账户信息不存在，请先注册！");
        }
        businessCard.setParentId(businessCard1.getCardId());
        businessCard.setCreateTime(LocalDateTime.now());
        baseMapper.updateById(businessCard);
        CompanyStaffRecord companyStaffRecord = new CompanyStaffRecord();
        companyStaffRecord.setId(IdUtils.fastSimpleUUID());
        companyStaffRecord.setStaffUserId(userId1);
        companyStaffRecord.setStaffCardId(businessCard.getCardId());
        companyStaffRecord.setCardId(businessCard1.getCardId());
        companyStaffRecord.setStaffSattus(IntegerEnum.COM_STAFF_STATUSE_ED.getCode());//已加入
        companyStaffRecord.setStaffSource(IntegerEnum.STAFF_SOURCE_COM_INSERT.getCode());//公司录入
        companyStaffRecord.setJoinTime(LocalDateTime.now());
        companyStaffRecord.setCreateTime(LocalDateTime.now());
        companyStaffRecordService.save(companyStaffRecord);
        return Result.success();
    }


    @Override
    public Result delStaff(Long userId, Long staffId) {
        //根据用户查询企业名片信息
        BusinessCard businessCard = baseMapper.queryCompanyShowCard(userId);
        Long cardId = businessCard.getCardId();
        BusinessCard businessCard1 = baseMapper.queryStaffCard(staffId, cardId);
        if (StringUtils.isNotNull(businessCard1)){
            businessCard1.setParentId(null);
            baseMapper.updateById(businessCard1);
            CompanyStaffRecord companyStaffRecord = new CompanyStaffRecord();
            companyStaffRecord.setId(IdUtils.fastSimpleUUID());
            companyStaffRecord.setStaffCardId(businessCard.getCardId());
            companyStaffRecord.setCardId(businessCard1.getCardId());
            companyStaffRecord.setStaffSattus(IntegerEnum.COM_STAFF_STATUSE_OUT.getCode());//已加入
            companyStaffRecord.setCreateTime(LocalDateTime.now());
            companyStaffRecordService.save(companyStaffRecord);
            return Result.success();
        }else {
            return Result.error("未查询到关联的员工信息！");
        }
    }


    /**
     * 根据企业名片id查询所有员工的名片
     * @return
     */
    @Override
    public Result queryStaff(JSONObject params) {
        Integer pageSize = params.getInteger("pageSize");
        Integer pageNum = params.getInteger("pageNum");
        Long cardId = params.getLong("cardId");
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        if (StringUtils.isNull(cardId)){
            return Result.error("企业名片id不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);
        List<BusinessCard> businessCardList = baseMapper.queryStaff(cardId);
        businessCardList = businessCardList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        PageInfo<BusinessCard> businessCardPageInfo = new PageInfo<>(businessCardList);
        return Result.success().put("data",businessCardPageInfo);
    }

    @Override
    public Result queryCompanyCard(Long userId) {
        List<BusinessCard> businessCardList = baseMapper.selectList(new QueryWrapper<BusinessCard>()
                .eq("user_id", userId)
                .eq("card_type", 2)
                .eq("audit_status",1)
                .eq("card_status",1));
        return Result.success().put("data",businessCardList);
    }

    @Override
    public Result cardHotList(JSONObject params) {
        Integer pageSize = params.getInteger("pageSize");
        Integer pageNum = params.getInteger("pageNum");
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        int i = pageNum - 1;
        BigDecimal bigDecimal = new BigDecimal(i);
        BigDecimal multiply = bigDecimal.multiply(BigDecimal.valueOf(pageSize));
        PageHelper.startPage(pageNum,pageSize);

        String industrySecondName = params.getString("industrySecondName");
        String companyProvince = params.getString("companyProvince");
        String companyCity = params.getString("companyCity");
        String companyDistrict = params.getString("companyDistrict");
        Long myUserId = params.getLong("userId");
        List<Map<String, Object>> maps = baseMapper.queryByPopularityValue(
                industrySecondName,
                companyProvince,
                companyCity,
                companyDistrict,
                multiply.intValue(),myUserId);

        for (Map<String, Object> map : maps) {
            Integer userId = (Integer) map.get("user_id");
            UserEntity user = userService.getById(userId);
            if (Objects.nonNull(user)){
                map.put("isVip", user.getIsVip());
            } else {
                map.put("isVip", 0);
            }
        }

        PageInfo<Map<String, Object>> mapPageInfo = new PageInfo<>(maps);
        return Result.success().put("data",mapPageInfo);
    }

    @Override
    public void getQrCode(Long cardId, HttpServletResponse response) {
        BusinessCard businessCard = baseMapper.selectById(cardId);
        try {
            Map<String, Object> map= new HashMap<>();
            map.put("cardId",businessCard.getCardId());
            map.put("realName",businessCard.getRealName());
            QRCodeUtil.createCodeToOutputStream(map.toString(),response.getOutputStream());
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Override
    public BusinessCard info(Long cardId, Long userId) {
        BusinessCard businessCard = baseMapper.selectById(cardId);
        if (Objects.isNull(businessCard)){
            throw new SqxException("名片不存在");
        }

        if (Objects.nonNull(userId) && !Objects.equals(businessCard.getUserId(), userId)){
            this.baseMapper.incrBrowseCount(cardId);
            this.baseMapper.incrPopularityValue(cardId);

            UserEntity user = userService.getById(userId);

            VisitCardRecord visitCardRecord = new VisitCardRecord();
            visitCardRecord.setId(IdUtils.fastSimpleUUID());
            visitCardRecord.setCardId(businessCard.getCardId());
            visitCardRecord.setUserId(userId);
            visitCardRecord.setUserName(user.getUserName());
            visitCardRecord.setVisitTime(LocalDateTime.now());
            visitCardRecord.setCreateTime(LocalDateTime.now());
            visitCardRecordService.save(visitCardRecord);

            businessCard.setUserCardListCount(userCardListService.count(new QueryWrapper<UserCardList>()
                    .eq("card_id",cardId).eq("user_id",userId)));
            MessageInfo messageInfo = messageService.getOne(new QueryWrapper<MessageInfo>()
                    .eq("platform_id", cardId).eq("by_user_id", userId).eq("type", 1).eq("state", 6));
            if(messageInfo!=null){
                businessCard.setMessageStatus(messageInfo.getStatus());
            }

            businessCard.setCompanyStaffCount(0);

            BusinessCard businessCard1 = queryUserShowCard(userId,1);
            if(businessCard1!=null){
                Integer count = companyStaffRecordDao.selectCount(new LambdaQueryWrapper<CompanyStaffRecord>()
                        .eq(CompanyStaffRecord::getCardId, cardId)
                        .eq(CompanyStaffRecord::getStaffCardId, businessCard1.getCardId())
                );
                businessCard.setCompanyStaffCount(count);
            }

        }

        int verifyFlag = 0;
        List<VerifyRecord> verifyRecordList = verifyRecordDbService.list(new LambdaQueryWrapper<VerifyRecord>()
                .eq(VerifyRecord::getUserId, userId)
                .eq(VerifyRecord::getName, businessCard.getCompanyName())
        );

        if (CollectionUtils.isNotEmpty(verifyRecordList)){
            verifyFlag = 1;
        }

        businessCard.setVerifyFlag(verifyFlag);

        return businessCard;
    }

    @Override
    public List<BusinessCard> queryByPhone(Long cardId, String phone) {
        return this.baseMapper.queryByPhone(cardId, phone);
    }

    @Override
    public IPage<BusinessCard> searchBusinessCard(String keyword, Long userId, Integer page, Integer limit) {
        if (null == page || 0 == page) {
            page = 1;
        }

        if (null == limit || 0 == limit) {
            limit = 10;
        }
        reentrantReadWriteLock.writeLock().lock();
        try {
            Search search = searchDao.selectOne(new QueryWrapper<Search>().eq("key_word", keyword).eq("user_id", userId));
            if(search!=null){
                search.setUpdateTime(new Date());
                searchDao.updateById(search);
            }else{
                search = new Search();
                search.setKeyWord(keyword);
                search.setUserId(userId);
                search.setUpdateTime(new Date());
                searchDao.insert(search);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }

        Page<BusinessCard> initPage = new Page<>(page, limit);
        return this.baseMapper.searchBusinessCard(initPage, keyword,userId);
    }

    @Override
    public Result delStaffCard(Long cardId, Long staffId) {
        BusinessCard businessCard1 = baseMapper.queryStaffCard(staffId, cardId);
        if (StringUtils.isNotNull(businessCard1)){
            businessCard1.setParentId(null);
            baseMapper.updateById(businessCard1);
            CompanyStaffRecord companyStaffRecord = new CompanyStaffRecord();
            companyStaffRecord.setId(IdUtils.fastSimpleUUID());
            companyStaffRecord.setStaffCardId(cardId);
            companyStaffRecord.setCardId(businessCard1.getCardId());
            companyStaffRecord.setStaffSattus(IntegerEnum.COM_STAFF_STATUSE_OUT.getCode());//已加入
            companyStaffRecord.setCreateTime(LocalDateTime.now());
            companyStaffRecordService.save(companyStaffRecord);
            return Result.success();
        }else {
            return Result.error("未查询到关联的员工信息！");
        }
    }

    @Override
    public void editStyle(Long cardId, String cardStyleCode) {
        CardStyle cardStyle = cardStyleService.queryInfoByStyleCode(cardStyleCode);
        if (Objects.isNull(cardStyle)){
            throw new SqxException("名片样式代码不存在");
        }

        this.update(new BusinessCard(), new LambdaUpdateWrapper<BusinessCard>()
                .eq(BusinessCard::getCardId, cardId)
                .set(BusinessCard::getCardStyleCode, cardStyleCode)
                .set(BusinessCard::getCardImg, cardStyle.getStyleImg())
                .set(BusinessCard::getCardImgBack, cardStyle.getStyleImg())
        );
    }

    @Override
    public Boolean userCardExists(Long userId) {
        int count = this.count(new LambdaQueryWrapper<BusinessCard>()
                .eq(BusinessCard::getUserId, userId)
        );

        return 0 < count;
    }
}

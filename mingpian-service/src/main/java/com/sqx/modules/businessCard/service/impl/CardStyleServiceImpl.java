package com.sqx.modules.businessCard.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.businessCard.dao.CardStyleDao;
import com.sqx.modules.businessCard.entity.CardStyle;
import com.sqx.modules.businessCard.service.CardStyleService;
import com.sqx.modules.utils.text.StringUtils;
import com.sqx.modules.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 名片样式(CardStyle)表服务实现类
 *
 * @author makejava
 * @since 2024-05-06 17:17:18
 */
@Service("cardStyleService")
public class CardStyleServiceImpl extends ServiceImpl<CardStyleDao, CardStyle> implements CardStyleService {
    @Resource
    private CardStyleDao cardStyleDao;

    @Override
    public Result queryByPage(JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }
        PageHelper.startPage(pageNum,pageSize);

        String styleName = params.getString("styleName");
        List<CardStyle> cardStyles = cardStyleDao.selectList(new QueryWrapper<CardStyle>()
                .like(StringUtils.isNotBlank(styleName), "style_name", styleName)
                .orderByDesc("create_time"));
        PageInfo<CardStyle> cardStylePageInfo = new PageInfo<>(cardStyles);
        return Result.success().put("data",cardStylePageInfo);
    }

    /**
     * 新增名片样式
     * @param cardStyle
     * @return
     */
    @Override
    public Result saveInfo(CardStyle cardStyle) {
        String styleCode = cardStyle.getStyleCode();
        if (StringUtils.isEmpty(styleCode)){
            return Result.error("样式编码不可以为空！");
        }
        CardStyle cardStyleInfo = cardStyleDao.selectOne(new QueryWrapper<CardStyle>().eq("style_code", styleCode));
        if (StringUtils.isNotNull(cardStyleInfo)){
            return Result.error("样式编码已存在，请更换！");
        }
        cardStyle.setId(IdUtils.fastSimpleUUID());
        cardStyle.setCreateTime(LocalDateTime.now());
        cardStyleDao.insert(cardStyle);
        return Result.success();
    }

    @Override
    public Result editInfo(CardStyle cardStyle) {
        String id = cardStyle.getId();
        String styleCode = cardStyle.getStyleCode();
        CardStyle cardStyleInfo = cardStyleDao.selectOne(new QueryWrapper<CardStyle>().eq("style_code", styleCode));
        if (StringUtils.equals(id,cardStyleInfo.getId())){
            cardStyleDao.updateById(cardStyle);
            return Result.success();
        }else {
            return Result.error("样式编码已存在");
        }
    }

    @Override
    public Result queryById(String id) {
        CardStyle cardStyle = cardStyleDao.selectById(id);
        return Result.success().put("data",cardStyle);
    }

    @Override
    public Result delById(String id) {
        int i = cardStyleDao.deleteById(id);
        return Result.upStatus(i);
    }

    @Override
    public Result appList(JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        if (StringUtils.isNull(pageNum)){
            return Result.error("pageNum不可以为空！");
        }
        if (StringUtils.isNull(pageSize)){
            return Result.error("pageSize不可以为空！");
        }

        String styleCode = params.getString("styleCode");

        PageHelper.startPage(pageNum,pageSize);
        List<CardStyle> cardStyles = cardStyleDao.selectList(new LambdaQueryWrapper<CardStyle>()
                .eq(StringUtils.isNotBlank(styleCode), CardStyle::getStyleCode, styleCode)
        );
        PageInfo<CardStyle> cardStylePageInfo = new PageInfo<>(cardStyles);
        return Result.success().put("data",cardStylePageInfo);
    }

    @Override
    public CardStyle queryInfoByStyleCode(String styleCode) {
        CardStyle cardStyle = cardStyleDao.selectOne(new QueryWrapper<CardStyle>().eq("style_code", styleCode));
        return cardStyle;
    }
}

package com.sqx.modules.scFollow.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.scFollow.entity.ScFollow;
import com.sqx.modules.scFollow.vo.ScFollowVo;

/**
 * @autorh liyuan
 * @description sc_follow
 * @date 2021-08-12
 */
public interface ScFollowService extends IService<ScFollow> {

    IPage<ScFollowVo> getMyFollowList(Integer page, Integer limit, Long userId, Double lng, Double lat);


    Result updateFollow(ScFollow follow);


}

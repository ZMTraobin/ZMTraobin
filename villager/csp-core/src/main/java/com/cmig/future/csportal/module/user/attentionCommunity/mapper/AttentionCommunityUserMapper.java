package com.cmig.future.csportal.module.user.attentionCommunity.mapper;

import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

/**
 * AttentionCommunityUserMapper
 *
 * @author bubu
 *
 * 2017-3-28
 */
public interface AttentionCommunityUserMapper extends Mapper<AttentionCommunityUser> {

    /**
     * 获取小区对象
     *
     * @param attentionCommunityUser
     * @return
     */
    AttentionCommunityUser getEntity(AttentionCommunityUser attentionCommunityUser);

    /**
     * 改变当前关注的小区
     *
     * @param attentionCommunityUser
     * @return
     */
     int attentionByTrue(AttentionCommunityUser attentionCommunityUser);

     int attentionByFalse(AttentionCommunityUser attentionCommunityUser);

    /**
     * 校验是否已关注该社区
     *
     * @param attentionCommunityUser
     * @return
     */
     int getAttentionCount(AttentionCommunityUser attentionCommunityUser);

    /**
     * 删除关注的社区
     *
     * @param attentionCommunityUser
     */
    void deleteByuser(AttentionCommunityUser attentionCommunityUser);

    /**
     * 获取下一条关注的小区
     *
     * @param attentionCommunityUser
     * @return
     */
    AttentionCommunityUser getNextData(AttentionCommunityUser attentionCommunityUser);

    /**
     * 查询列表数据
     * @param attentionCommunityUser
     * @return
     */
    List<AttentionCommunityUser> findList(AttentionCommunityUser attentionCommunityUser);
}
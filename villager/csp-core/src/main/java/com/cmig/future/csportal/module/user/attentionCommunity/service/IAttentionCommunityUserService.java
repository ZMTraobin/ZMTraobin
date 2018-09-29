package com.cmig.future.csportal.module.user.attentionCommunity.service;

import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

/**
 * IAttentionCommunityUserService
 *
 * @author bubu
 *
 * 2017-3-28
 */
public interface IAttentionCommunityUserService extends IBaseService<AttentionCommunityUser>, ProxySelf<IAttentionCommunityUserService>{

    /**
     *  保存关注社区
     * @param attentionCommunityUser
     */
    public void save(AttentionCommunityUser attentionCommunityUser);

    /**
     * 获取小区对象
     *
     * @param attentionCommunityUser
     * @return
     */
    public AttentionCommunityUser getEntity(AttentionCommunityUser attentionCommunityUser);

    /**
     * 更新关注小区
     *
     * @param attentionCommunityUser
     * @return
     */
    public int updateAttention(AttentionCommunityUser attentionCommunityUser);

    /**
     * 校验是否已关注该社区
     *
     * @param attentionCommunityUser
     * @return
     */
    public int getAttentionCount(AttentionCommunityUser attentionCommunityUser);

    /**
     * 删除关注的社区
     *
     * @param attentionCommunityUser
     * @return
     */
    public void delete(AttentionCommunityUser attentionCommunityUser);

    /**
     * 获取下一条关注的小区
     *
     * @param attentionCommunityUser
     * @return
     */
    public AttentionCommunityUser getNextData(AttentionCommunityUser attentionCommunityUser);

    /**
     * 查询列表数据
     * @param attentionCommunityUser
     * @return
     */
    public List<AttentionCommunityUser> findList(AttentionCommunityUser attentionCommunityUser);

    /**
     * 分页查询用户关注的小区
     * @param attentionCommunityUser
     * @return
     */
    public List<AttentionCommunityUser> getCommunityList(IRequest request, AttentionCommunityUser attentionCommunityUser, int pageNum, int pageSize);

}
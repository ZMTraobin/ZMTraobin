package com.cmig.future.csportal.module.operate.businessMsg.service;

import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IBusinessMsgService extends IBaseService<SysNotifyRecord>, ProxySelf<IBusinessMsgService>{


    /**
     * 根据选择的人员mobile推送push消息
     * @author bubu
     * @param category
     * @param content
     * @param mobiles
     */
    public void pushToAppUser(IRequest requestCtx, String category, String content, String mobiles);

    /**
     * 根据选择的人员mobiles推送SMS消息
     * @author bubu
     * @param category
     * @param content
     * @param mobiles
     */
    public void smsToAppUser(IRequest requestCtx, String category, String content, String mobiles);

    /**
     * 手动消息列表
     * @param requestCtx
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    public List<SysNotifyRecord> findListByMsg(IRequest requestCtx, SysNotifyRecord dto, int page, int pageSize);

    /**
     * 手动消息-发送人员列表
     * @param requestCtx
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    public List<AppUser> groupByTimeAndCateoryAndType(IRequest requestCtx, SysNotifyRecord dto, int page, int pageSize);

    /**
     * 手动消息-软删除
     * @param dto
     * @return
     */
    public int deleteByTimeAndCateoryAndType(SysNotifyRecord dto);

}
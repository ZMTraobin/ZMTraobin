package com.cmig.future.csportal.module.sys.notifyrecord.service;

import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface ISysNotifyRecordService extends IBaseService<SysNotifyRecord>, ProxySelf<ISysNotifyRecordService>{

    void save(SysNotifyRecord sysNotifyRecord);

    /**
     * 根据选择的项目id推送公告
     * @author bubu
     * @param articleId
     * @param content
     * @param selectIds
     */
    public void publishToCommunity(IRequest requestCtx, String articleId, String content, String selectIds);

    /**
     * 根据选择的人员id推送公告
     * @author bubu
     * @param articleId
     * @param content
     * @param selectUserIds
     */
    public void publishToUser(IRequest requestCtx, String articleId, String content, String selectUserIds);

    /**
     * 关联人员,项目表,分页查询
     * @param sysNotifyRecord
     * @return
     */
    public List<SysNotifyRecord> findList(IRequest requestContext, SysNotifyRecord sysNotifyRecord, int page, int pagesize);

    /**
     * 清空用户某个小区下的所有消息
     * @param sysNotifyRecord
     */
    public void clean(SysNotifyRecord sysNotifyRecord);

    /**
     * 删除消息
     * @param sysNotifyRecord
     */
    public void deleteSysNotifyRecord(SysNotifyRecord sysNotifyRecord);

    /**
     * 消息列表-分页
     * @param sysNotifyRecord
     * @return
     */
    public List<SysNotifyRecord> selectSysNotifyRecord(IRequest requestContext, SysNotifyRecord sysNotifyRecord, int page, int pagesize);

    /**
     * 消息列表-不分页
     * @param sysNotifyRecord
     * @return
     */
    public List<SysNotifyRecord> selectSysNotifyRecord(SysNotifyRecord sysNotifyRecord);
}
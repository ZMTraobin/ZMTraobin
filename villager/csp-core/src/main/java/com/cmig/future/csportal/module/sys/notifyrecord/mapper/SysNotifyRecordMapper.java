package com.cmig.future.csportal.module.sys.notifyrecord.mapper;

import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface SysNotifyRecordMapper extends Mapper<SysNotifyRecord>{

    /**
     * 关联人员,项目表,查询列表
     * @param sysNotifyRecord
     * @return
     */
    List<SysNotifyRecord> findList(SysNotifyRecord sysNotifyRecord);

    /**
     * 清空用户某个小区下的所有消息
     * @param sysNotifyRecord
     */
    void clean(SysNotifyRecord sysNotifyRecord);

    /**
     * 删除消息
     * @param sysNotifyRecord
     */
    int deleteSysNotifyRecord(SysNotifyRecord sysNotifyRecord);

    /**
     * 消息列表
     * @param sysNotifyRecord
     * @return
     */
    List<SysNotifyRecord> selectSysNotifyRecord(SysNotifyRecord sysNotifyRecord);


    /**
     * 手动消息列表列表
     * @param dto
     * @return
     */
    List<SysNotifyRecord> findListByMsg(SysNotifyRecord dto);

    /**
     * 手动消息-软删除
     * @param dto
     * @return
     */
    int deleteByTimeAndCateoryAndType(SysNotifyRecord dto);

}
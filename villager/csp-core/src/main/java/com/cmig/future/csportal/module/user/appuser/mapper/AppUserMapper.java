package com.cmig.future.csportal.module.user.appuser.mapper;

import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppUserMapper extends Mapper<AppUser>{

    AppUser getByMobile(String mobile);

    void updateUserMobileInfo(AppUser appUser);

    AppUser getBySourceSystemId(String openid);

    List<AppUser> findList(AppUser appUser);
    /**
     * 手动消息-发送人员列表
     * @param dto
     * @return
     */
    List<AppUser> groupByTimeAndCateoryAndType(SysNotifyRecord dto);

    void updateRelationFlag(@Param("id") String id, @Param("flag") String flag);
}
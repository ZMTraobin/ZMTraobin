package com.cmig.future.csportal.module.properties.authorManager.utils;

import com.cmig.future.csportal.module.properties.authorManager.dto.LjhSysUserCommunity;
import com.cmig.future.csportal.module.properties.authorManager.service.ILjhSysUserCommunityService;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by dell on 2017/4/12.
 */
public class SysUserCommunityUtil {

    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(SysUserCommunityUtil.class);
    private static ILjhSysUserCommunityService ljhSysUserCommunityService= SpringContextHolder.getBean("ljhSysUserCommunityServiceImpl");

    /**
     * 根据登录用户系统id查询该权限下的小区id数组
     * @param sysUserId
     * @return
     */
    public static String[] CommunityIdsArray(Long sysUserId){
        LjhSysUserCommunity ljhSysUserCommunity = new LjhSysUserCommunity();
        ljhSysUserCommunity.setSysUserId(sysUserId);
        List<LjhSysUserCommunity> dto = ljhSysUserCommunityService.findListByUserId(ljhSysUserCommunity);
        String communityIds = "";
        for(LjhSysUserCommunity sysUserCommunity : dto){
            communityIds += sysUserCommunity.getCommunityId()+",";
        }
        communityIds = communityIds.substring(0,communityIds.length()-1);
        String[] communityIdsArray = communityIds.split(",");
        logger.debug(JSONUtils.valueToString(communityIdsArray));
        return communityIdsArray;
    }
}

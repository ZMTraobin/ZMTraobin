package com.cmig.future.csportal.module.operate.businessMsg.service.impl;

import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.Jpush.JPushUtils;
import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.operate.businessMsg.service.IBusinessMsgService;
import com.cmig.future.csportal.module.sys.notifyrecord.constants.NotifyMessageTypeEnum;
import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.cmig.future.csportal.module.sys.notifyrecord.mapper.SysNotifyRecordMapper;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.mapper.AppUserMapper;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BusinessMsgServiceImpl extends BaseServiceImpl<SysNotifyRecord> implements IBusinessMsgService {
    private final Logger logger = LoggerFactory.getLogger(BusinessMsgServiceImpl.class);
    @Autowired
    private SysNotifyRecordMapper sysNotifyRecordMapper;

    @Autowired
    private AppUserMapper appUserMapper;

    /**
     * 根据选择的人员mobile推送push消息
     * @author bubu
     * @param category
     * @param content
     * @param mobiles
     */
    @Override
    public void pushToAppUser(IRequest requestCtx, String category, String content, String mobiles) {
        if(!StringUtils.isEmpty(content)&&!StringUtils.isEmpty(mobiles)){
            String[] appUserMobile = mobiles.split(",");
            Map map=new HashMap<>();
            Date currentTime = new Date();
            for(String mobile : appUserMobile){
                AppUser appUser = appUserMapper.getByMobile(mobile);
                SysNotifyRecord sysNotifyRecord = new SysNotifyRecord();
                sysNotifyRecord.setMessageType(NotifyMessageTypeEnum.PUSH.getCode());
                sysNotifyRecord.setContent(content);
                sysNotifyRecord.setContentExt("");
                sysNotifyRecord.setCategory(category);
                sysNotifyRecord.setAppUserId(appUser.getId());
                sysNotifyRecord.setReceiverInfo(mobile);
                sysNotifyRecord.setCreationDate(currentTime);
                sysNotifyRecordMapper.insertSelective(sysNotifyRecord);

                map.put(mobile,sysNotifyRecord.getId());
                JPushUtils.pushAlertWithMessage(sysNotifyRecord);
            }
        }
    }

    /**
     * 根据选择的人员mobiles推送SMS消息
     * @author bubu
     * @param category
     * @param content
     * @param mobiles
     */
    @Override
    public void smsToAppUser(IRequest requestCtx, String category, String content, String mobiles) {
        if(!StringUtils.isEmpty(content)&&!StringUtils.isEmpty(mobiles)){
            String[] appUserMobile = mobiles.split(",");
            Map map=new HashMap<>();
            String smsContent = StringEscapeUtils.escapeJava(content);
            Date currentTime = new Date();
            for(String mobile : appUserMobile){
                AppUser appUser = appUserMapper.getByMobile(mobile);
                SysNotifyRecord sysNotifyRecord = new SysNotifyRecord();
                sysNotifyRecord.setMessageType(NotifyMessageTypeEnum.SMS.getCode());
                sysNotifyRecord.setContent(content);
                sysNotifyRecord.setContentExt("");
                sysNotifyRecord.setCategory(category);
                sysNotifyRecord.setAppUserId(appUser.getId());
                sysNotifyRecord.setReceiverInfo(mobile);
                sysNotifyRecord.setCreationDate(currentTime);
                sysNotifyRecordMapper.insertSelective(sysNotifyRecord);
                map.put(mobile,sysNotifyRecord.getId());
                boolean result = SmsUtil.sendSyn(smsContent,mobile);
                if(result){
                    sysNotifyRecord.setStatus(Constants.NOTIFY_STATUS_SUCCESS);
                }else{
                    sysNotifyRecord.setStatus(Constants.NOTIFY_STATUS_FAIL);
                }
                sysNotifyRecordMapper.updateByPrimaryKeySelective(sysNotifyRecord);
            }
        }
    }

    /**
     * 手动消息列表
     * @param request
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<SysNotifyRecord> findListByMsg(IRequest request, SysNotifyRecord dto, int page, int pageSize){
        PageHelper.startPage(page, pageSize);
        return sysNotifyRecordMapper.findListByMsg(dto);
    }

    /**
     * 手动消息-发送人员列表
     * @param requestCtx
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<AppUser> groupByTimeAndCateoryAndType(IRequest requestCtx,SysNotifyRecord dto,int page,int pageSize){
        PageHelper.startPage(page, pageSize);
        return appUserMapper.groupByTimeAndCateoryAndType(dto);
    }

    /**
     * 手动消息-软删除
     * @param dto
     * @return
     */
    @Override
    public int deleteByTimeAndCateoryAndType(SysNotifyRecord dto){
        return sysNotifyRecordMapper.deleteByTimeAndCateoryAndType(dto);
    }
}
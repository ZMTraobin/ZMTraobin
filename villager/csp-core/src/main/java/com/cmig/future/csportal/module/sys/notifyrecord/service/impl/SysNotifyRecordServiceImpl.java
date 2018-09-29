package com.cmig.future.csportal.module.sys.notifyrecord.service.impl;

import com.cmig.future.csportal.common.utils.Jpush.JPushPmCloundUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserCommunity;
import com.cmig.future.csportal.module.properties.mgtuser.mapper.MgtUserCommunityMapper;
import com.cmig.future.csportal.module.sys.enumType.NotifyType;
import com.cmig.future.csportal.module.sys.notifyrecord.constants.NotifyMessageTypeEnum;
import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.cmig.future.csportal.module.sys.notifyrecord.mapper.SysNotifyRecordMapper;
import com.cmig.future.csportal.module.sys.notifyrecord.service.ISysNotifyRecordService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SysNotifyRecordServiceImpl extends BaseServiceImpl<SysNotifyRecord> implements ISysNotifyRecordService{
    private final Logger logger = LoggerFactory.getLogger(SysNotifyRecordServiceImpl.class);
    @Autowired
    private SysNotifyRecordMapper sysNotifyRecordMapper;

    @Autowired
    private MgtUserCommunityMapper mgtUserCommunityMapper;

    @Override
    public void save(SysNotifyRecord sysNotifyRecord) {
        if(!StringUtils.isEmpty(sysNotifyRecord.getId())){
            sysNotifyRecordMapper.updateByPrimaryKeySelective(sysNotifyRecord);
        }else{
            sysNotifyRecordMapper.insertSelective(sysNotifyRecord);
        }
    }

    /**
     * 关联人员,项目表,分页查询
     * @param sysNotifyRecord
     * @return
     */
    @Override
    public List<SysNotifyRecord> findList(IRequest request, SysNotifyRecord sysNotifyRecord, int page, int pagesize){
        PageHelper.startPage(page, pagesize);
        return sysNotifyRecordMapper.findList(sysNotifyRecord);
    }

    /**
     * 根据选择的项目id推送公告
     * @author bubu
     * @param articleId
     * @param content
     * @param selectIds
     */
    @Override
    public void publishToCommunity(IRequest request,String articleId,String content, String selectIds) {
        if(!StringUtils.isEmpty(content)&&!StringUtils.isEmpty(selectIds)){
            String[] communityIds=selectIds.split(",");
            MgtUserCommunity mgtUserCommunity=new MgtUserCommunity();
            mgtUserCommunity.setSelectIds(communityIds);
            List<MgtUserCommunity> list= mgtUserCommunityMapper.findPublishUserList(mgtUserCommunity);
            Map map=new HashMap<>();//去重
            for(MgtUserCommunity userCommunity:list) {
                if(map.containsKey(userCommunity.getMobile())) continue;
                SysNotifyRecord sysNotifyRecord = new SysNotifyRecord();
                sysNotifyRecord.setMessageType(NotifyMessageTypeEnum.PUSH.getCode());
                sysNotifyRecord.setCmsNotifyId(articleId);
                sysNotifyRecord.setContent(content);
                sysNotifyRecord.setContentExt("");
                sysNotifyRecord.setCategory(NotifyType.NOTIFY.name());
                sysNotifyRecord.setMgtUserId(userCommunity.getMgtUserId());
                sysNotifyRecord.setCommunityId(userCommunity.getCommunityId());
                sysNotifyRecord.setReceiverInfo(userCommunity.getMobile());
                sysNotifyRecordMapper.insertSelective(sysNotifyRecord);

                JSONObject jsonObject=new JSONObject();
                jsonObject.put("type", NotifyType.NOTIFY);
                jsonObject.put("id",sysNotifyRecord.getId());
                jsonObject.put("sourceSystemCommunityId",userCommunity.getSourceSystemCommunityId());
                sysNotifyRecord.setContentExt(jsonObject.toString());

                map.put(userCommunity.getMobile(),sysNotifyRecord.getId());
                JPushPmCloundUtils.pushAlertWithMessage(sysNotifyRecord);
            }
        }
    }

    /**
     * 根据选择的人员id推送公告
     * @author bubu
     * @param articleId
     * @param content
     * @param selectUserIds
     */
    @Override
    public void publishToUser(IRequest request,String articleId,String content, String selectUserIds) {
        if(!StringUtils.isEmpty(content)&&!StringUtils.isEmpty(selectUserIds)){
            String[] userIds=selectUserIds.split(",");
            MgtUserCommunity mgtUserCommunity=new MgtUserCommunity();
            mgtUserCommunity.setSelectUserIds(userIds);
            List<MgtUserCommunity> list= mgtUserCommunityMapper.findPublishUserList(mgtUserCommunity);
            logger.info(JSONUtils.valueToString(list));
            Map map=new HashMap<>();//去重
            for(MgtUserCommunity userCommunity:list) {
                if(map.containsKey(userCommunity.getMobile())) continue;
                SysNotifyRecord sysNotifyRecord = new SysNotifyRecord();
                sysNotifyRecord.setMessageType(NotifyMessageTypeEnum.PUSH.getCode());
                sysNotifyRecord.setCmsNotifyId(articleId);
                sysNotifyRecord.setContent(content);
                sysNotifyRecord.setContentExt("");
                sysNotifyRecord.setCategory(NotifyType.NOTIFY.name());
                sysNotifyRecord.setMgtUserId(userCommunity.getMgtUserId());
                sysNotifyRecord.setCommunityId(userCommunity.getCommunityId());
                sysNotifyRecord.setReceiverInfo(userCommunity.getMobile());
                sysNotifyRecordMapper.insertSelective(sysNotifyRecord);

                JSONObject jsonObject=new JSONObject();
                jsonObject.put("type",NotifyType.NOTIFY);
                jsonObject.put("id",sysNotifyRecord.getId());
                jsonObject.put("sourceSystemCommunityId",userCommunity.getSourceSystemCommunityId());
                sysNotifyRecord.setContentExt(jsonObject.toString());

                map.put(userCommunity.getMobile(),sysNotifyRecord.getId());
                JPushPmCloundUtils.pushAlertWithMessage(sysNotifyRecord);
            }
        }
    }

    /**
     * 清空用户某个小区下的所有消息
     * @param sysNotifyRecord
     */
    @Override
    public void clean(SysNotifyRecord sysNotifyRecord){
        sysNotifyRecordMapper.clean(sysNotifyRecord);
    }

    /**
     * 删除消息
     * @param sysNotifyRecord
     */
    @Override
    public void deleteSysNotifyRecord(SysNotifyRecord sysNotifyRecord){
        sysNotifyRecordMapper.deleteSysNotifyRecord(sysNotifyRecord);
    }

    /**
     * 消息列表-分页
     * @param sysNotifyRecord
     * @return
     */
    @Override
    public List<SysNotifyRecord> selectSysNotifyRecord(IRequest request, SysNotifyRecord sysNotifyRecord, int page, int pagesize){
        PageHelper.startPage(page, pagesize);
        return sysNotifyRecordMapper.selectSysNotifyRecord(sysNotifyRecord);
    }

    /**
     * 消息列表-不分页
     * @param sysNotifyRecord
     * @return
     */
    @Override
    public List<SysNotifyRecord> selectSysNotifyRecord(SysNotifyRecord sysNotifyRecord){
        return sysNotifyRecordMapper.selectSysNotifyRecord(sysNotifyRecord);
    }
}
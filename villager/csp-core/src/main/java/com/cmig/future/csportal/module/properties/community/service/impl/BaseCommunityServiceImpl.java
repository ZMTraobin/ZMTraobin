package com.cmig.future.csportal.module.properties.community.service.impl;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.pay.account.dto.AccountReceivable;
import com.cmig.future.csportal.module.pay.account.mapper.AccountReceivableMapper;
import com.cmig.future.csportal.module.properties.authorManager.dto.LjhSysUserCommunity;
import com.cmig.future.csportal.module.properties.authorManager.mapper.LjhSysUserCommunityMapper;
import com.cmig.future.csportal.module.properties.base.customer.CustomerInfoException;
import com.cmig.future.csportal.module.properties.community.constants.CommunityConstants;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.mapper.BaseCommunityMapper;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import com.cmig.future.csportal.module.properties.communityEvent.dto.LjhEventCommunity;
import com.cmig.future.csportal.module.properties.communityEvent.mapper.LjhEventCommunityMapper;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigCommunity;
import com.cmig.future.csportal.module.sys.appconfig.mapper.AppConfigCommunityMapper;
import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.cmig.future.csportal.module.sys.notifyrecord.mapper.SysNotifyRecordMapper;
import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.mapper.AttentionCommunityUserMapper;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * BaseCommunityServiceImpl
 *
 * @author bubu
 *
 * 2017-3-21
 */
@Service
@Transactional
public class BaseCommunityServiceImpl extends BaseServiceImpl<BaseCommunity> implements IBaseCommunityService {

    @Autowired
    private BaseCommunityMapper baseCommunityMapper;

	@Autowired
	private AppConfigCommunityMapper appConfigCommunityMapper;
	
	@Autowired
    private LjhEventCommunityMapper ljhEventCommunityMapper;
	
	@Autowired
    private AccountReceivableMapper accountReceivableMapper;
	
	@Autowired
    private AttentionCommunityUserMapper attentionCommunityUserMapper;
	
	@Autowired
    private LjhSysUserCommunityMapper ljhSysUserCommunityMapper;
	
	@Autowired
    private SysNotifyRecordMapper sysNotifyRecordMapper;

    @Override
    public void save(BaseCommunity baseCommunity) {
        if(!StringUtils.isEmpty(baseCommunity.getId())){
            baseCommunityMapper.updateByPrimaryKeySelective(baseCommunity);
        }else{
            baseCommunity.setId(IdGen.uuid());
            baseCommunityMapper.insertSelective(baseCommunity);
	        //处理小区功能配置信息
	        appConfigCommunity(baseCommunity);
        }
    }

    @Override
    public BaseCommunity getBySourceSystemId(BaseCommunity community) {
        return baseCommunityMapper.getBySourceSystemId(community);
    }

    @Override
    public BaseCommunity findByCommunityName(BaseCommunity community) {
        return baseCommunityMapper.findByCommunityName(community);
    }

    @Override
    public List<BaseCommunity> findCommunityListByName(BaseCommunity community) {
        return baseCommunityMapper.findCommunityListByName(community);
    }

    @Override
    public int delete(BaseCommunity community) {
        return baseCommunityMapper.deleteCommunity(community);
    }

    @Override
    public List<BaseCommunity> findUserCommunityList(BaseCommunity communityQuery) {
        return baseCommunityMapper.findUserCommunityList(communityQuery);
    }

    /**
     * 根据主键查找项目信息
     *
     * @param communityId
     * @return
     */
    @Override
    public BaseCommunity get(String communityId) {
        BaseCommunity baseCommunity = new BaseCommunity();
        baseCommunity.setId(communityId);
        return baseCommunityMapper.getById(baseCommunity);
    }


    /**
     * 根据小区名称模糊查询小区列表

     * @return
     */
    @Override
    public List<BaseCommunity> findCommunityByLikeCommunityName(IRequest request, BaseCommunity community, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return baseCommunityMapper.findCommunityByLikeCommunityName(community);
    }

    /**
     * 根据经纬度查询附近的小区
     * @return
     */
    @Override
    public BaseCommunity findCommunityByLngLat(BaseCommunity community){
        return baseCommunityMapper.findCommunityByLngLat(community);
    }

    /**
     * 分页查询小区--web端
     * @param community
     * @return
     */
    @Override
    public List<BaseCommunity> selectCommunity(IRequest request, BaseCommunity community, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        return baseCommunityMapper.selectCommunity(community);
    }

    /**
     * 分页查询小区--客户端
     * @param community
     * @return
     */
    @Override
    public List<BaseCommunity> findList(IRequest request, BaseCommunity community, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        return baseCommunityMapper.findList(community);
    }

    /**
     * 根据项目编码查询小区
     * @param community
     * @return
     */
    @Override
    public List<BaseCommunity> findListByCommunityCode(BaseCommunity community){
        return baseCommunityMapper.findListByCommunityCode(community);
    }

    /**
     * 根据项目编码查询小区
     * @param communityCode
     * @return
     */
    @Override
    public BaseCommunity findListByCommunityCode(String communityCode) {
        BaseCommunity baseCommunity=new BaseCommunity();
        baseCommunity.setCommunityCode(communityCode);
        List<BaseCommunity> list=findListByCommunityCode(baseCommunity);
        if(null!=list&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
	public void appConfigCommunity(BaseCommunity community) {
		//  community.setConfigFlag(flag);
		//查询该小区开通的功能
		AppConfigCommunity appConfigCommun = new AppConfigCommunity();
		appConfigCommun.setCommunityId(community.getId());
		List<AppConfigCommunity> ownerCommunitys = appConfigCommunityMapper.findAppCommunity(appConfigCommun);
		if (ownerCommunitys == null || ownerCommunitys.size() == 0) {
			AppConfigCommunity appConfigCommunity = new AppConfigCommunity();
			appConfigCommunity.setId(IdGen.uuid());
			appConfigCommunity.setCommunityId(community.getId());
			appConfigCommunity.setDelFlag("0");
			appConfigCommunity.setConfigFlag("owner");
			//AppConfigFunction function = new AppConfigFunction();
			// function.setFunctionFlag(flag);
			//function.setFunctionStatus(Constants.APP_CONFIG_STATUS_ON);
			appConfigCommunityMapper.insertSelective(appConfigCommunity);

			appConfigCommunity.setId(IdGen.uuid());
			appConfigCommunity.setCommunityId(community.getId());
			appConfigCommunity.setDelFlag("0");
			appConfigCommunity.setConfigFlag("propertyMC");
			appConfigCommunityMapper.insertSelective(appConfigCommunity);
            /*function.setFunctionType(Constants.APP_CONFIG_TYPE_INTERIOR);
			saveFunction(community,function);
			function.setFunctionType(Constants.APP_CONFIG_TYPE_EXTERIOR);
			saveFunction(community,function);*/
		}
	}
	
	@Override
    public void deleteCommunity(IRequest requestContext,String id) throws CustomerInfoException {
		BaseCommunity baseCommunity=baseCommunityMapper.selectByPrimaryKey(id);
		if(CommunityConstants.DEFAULT_COMMUNITY_CODE.equals(baseCommunity.getCommunityCode())){
			throw new CustomerInfoException(CustomerInfoException.MSG_ERROR_COMMUNITY_DEFAULT,CustomerInfoException.MSG_ERROR_COMMUNITY_DEFAULT,null);
		}

        //先判断小区有没有绑定
        List<BaseCommunity> list = baseCommunityMapper.isBind(id);
        if(list.isEmpty()){
            throw new CustomerInfoException(CustomerInfoException.MSG_ERROR_COMMUNITY_BIND,CustomerInfoException.MSG_ERROR_COMMUNITY_BIND,null);
        }

        BaseCommunity community = new BaseCommunity();
        community.setId(id);
        baseCommunityMapper.delete(community);
        //删除授权表 csp_ljh_event_community
        LjhEventCommunity event = new LjhEventCommunity();
        event.setCommunityId(id);
        ljhEventCommunityMapper.delete(event);
        //删除收款账户配置表 csp_account_receivable
        AccountReceivable receive = new AccountReceivable();
        receive.setCommunityId(id);
        accountReceivableMapper.delete(receive);
        //删除前端功能配置——小区功能配置表：csp_ljh_app_config_community
        AppConfigCommunity config = new AppConfigCommunity();
        config.setCommunityId(id);
        appConfigCommunityMapper.delete(config);
        //用户关注小区表 csp_ljh_attention_community_user
        AttentionCommunityUser user = new AttentionCommunityUser();
        user.setCommunityId(id);
        attentionCommunityUserMapper.delete(user);
        //用户授权 csp_ljh_sys_user_community
        LjhSysUserCommunity sysUser = new LjhSysUserCommunity();
        sysUser.setCommunityId(id);
        ljhSysUserCommunityMapper.delete(sysUser);
        //退款通知 csp_ljh_sys_notify_record
        SysNotifyRecord record = new SysNotifyRecord();
        record.setCommunityId(id);
        sysNotifyRecordMapper.delete(record);
    }
}
package com.cmig.future.csportal.api.open.message.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.Jpush.JPushPmCloundUtils;
import com.cmig.future.csportal.common.utils.Jpush.JPushUtils;
import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.enums.NotifyCategoryEnum;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import com.cmig.future.csportal.module.sys.notifyrecord.constants.NotifyMessageTypeEnum;
import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.cmig.future.csportal.module.sys.notifyrecord.service.ISysNotifyRecordService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping(value = "${commonPath}")
public class PushController extends BaseExtendController {

    @Autowired
    private ISysNotifyRecordService sysNotifyRecordService;
    @Autowired
    private IBaseCommunityService BaseCommunityService;
    @Autowired
    private IMgtUserService mgtUserService;
    @Autowired
    private IAppUserService appUserService;

	// 业主端push
	@RequestMapping(value = "/push/user", produces = { "application/json" }, method = RequestMethod.POST)
	public RetApp user(HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		String alert = getParam(request, "content", "");
		String mobiles = getParam(request, "mobile", "");
        String contentExt = getParam(request, "message", "");
        String category = getParam(request, "type", "");
        String sourceSystemCommunityId=getParam(request, "sourceSystemCommunityId", "");
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");

		try {

			if (StringUtils.isEmpty(mobiles)) {
				throw new DataWarnningException("手机号不能为空");
			}
			if (StringUtils.isEmpty(alert)) {
				throw new DataWarnningException("消息内容不能为空");
			}

            // 校验appid
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);

            BaseCommunity communityQuery = new BaseCommunity();
            if(!StringUtils.isEmpty(sourceSystemCommunityId)){
                communityQuery.setSourceSystem(openAppInfo.getAppName());
                communityQuery.setSourceSystemId(sourceSystemCommunityId);
                communityQuery = BaseCommunityService.getBySourceSystemId(communityQuery);
                if(null==communityQuery){
                    throw new DataWarnningException("该小区未同步");
                }
            }

            logger.debug("mobile:{} alert:{} message:{} type:{} sourceSystemCommunityId:{}",mobiles,alert,contentExt,category,sourceSystemCommunityId);

            for(String mobile:mobiles.split(";")){
                SysNotifyRecord sysNotifyRecord = new SysNotifyRecord();
                sysNotifyRecord.setMessageType(NotifyMessageTypeEnum.PUSH.getCode());
                if (!StringUtils.isEmpty(category)) {
                    sysNotifyRecord.setCategory(category);
                }
                sysNotifyRecord.setCmsNotifyId(null);
                sysNotifyRecord.setSubject(null);
                sysNotifyRecord.setContent(alert);
                sysNotifyRecord.setContentExt(URLDecoder.decode(contentExt,"UTF-8"));
                sysNotifyRecord.setReceiverInfo(mobile);
                sysNotifyRecord.setCommunityId(communityQuery.getId());
                sysNotifyRecord.setSourceSystem(openAppInfo.getAppName());
                AppUser appUser= appUserService.getByMobile(mobile);
                if(null!=appUser){
                    sysNotifyRecord.setAppUserId(appUser.getId());
                    sysNotifyRecordService.save(sysNotifyRecord);
                    JPushUtils.pushAlertWithMessage(sysNotifyRecord);
                }else{
                    sysNotifyRecord.setStatus(Constants.NOTIFY_STATUS_FAIL);
                    sysNotifyRecord.setRemark("未能匹配用户别名："+mobile);
                    sysNotifyRecordService.save(sysNotifyRecord);
                }
            }

			retApp.setStatus(OK);
			retApp.setMessage("发送成功");
		} catch (DataWarnningException e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		}catch (OAuth2Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage("发送失败");
		}

		return retApp;
	}

    // 物管云-物业端push
    @RequestMapping(value = "/push/propertyClound", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp propertyClound(HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        String alert = getParam(request, "content", "");
        String mobiles = getParam(request, "mobile", "");
        String contentExt = getParam(request, "message", "");
        String category = getParam(request, "type", "");
        String sourceSystemCommunityId=getParam(request, "sourceSystemCommunityId", "");
        final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
        try {

            if (StringUtils.isEmpty(mobiles)) {
                throw new DataWarnningException("手机号不能为空");
            }
            if (StringUtils.isEmpty(alert)) {
                throw new DataWarnningException("消息内容不能为空");
            }
            if (StringUtils.isEmpty(category)) {
                throw new DataWarnningException("消息分类不能为空");
            }
            if (StringUtils.isEmpty(sourceSystemCommunityId)) {
                throw new DataWarnningException("小区id不能为空");
            }

            if(!NotifyCategoryEnum.contains(category)){
                throw new DataWarnningException("消息分类参数错误");
            }

            // 校验appid
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);

            BaseCommunity communityQuery = new BaseCommunity();
            communityQuery.setSourceSystem(openAppInfo.getAppName());
            communityQuery.setSourceSystemId(sourceSystemCommunityId);
            communityQuery = BaseCommunityService.getBySourceSystemId(communityQuery);
            if(null==communityQuery){
                throw new DataWarnningException("该小区未同步");
            }

            logger.debug("mobile:{} alert:{} message:{} type:{} sourceSystemCommunityId:{}",mobiles,alert,contentExt,category,sourceSystemCommunityId);

            for(String mobile:mobiles.split(";")){
                SysNotifyRecord sysNotifyRecord = new SysNotifyRecord();
                sysNotifyRecord.setMessageType(NotifyMessageTypeEnum.PUSH.getCode());
                sysNotifyRecord.setCategory(category);
                sysNotifyRecord.setCmsNotifyId(null);
                sysNotifyRecord.setSubject(null);
                sysNotifyRecord.setContent(alert);
                contentExt = StringEscapeUtils.unescapeHtml4(contentExt);
                sysNotifyRecord.setContentExt(URLDecoder.decode(contentExt,"UTF-8"));
                sysNotifyRecord.setReceiverInfo(mobile);
                sysNotifyRecord.setCommunityId(communityQuery.getId());
                sysNotifyRecord.setSourceSystem(openAppInfo.getAppName());
                MgtUser mgtUser= mgtUserService.getUserByMobile(mobile);
                if(null!=mgtUser){
                    sysNotifyRecord.setMgtUserId(mgtUser.getId());
                    sysNotifyRecordService.save(sysNotifyRecord);
                    JPushPmCloundUtils.pushAlertWithMessage(sysNotifyRecord);
                }else{
                    sysNotifyRecord.setStatus(Constants.NOTIFY_STATUS_FAIL);
                    sysNotifyRecord.setRemark("未能匹配员工别名:"+mobile);
                    sysNotifyRecordService.save(sysNotifyRecord);
                }
            }
            retApp.setStatus(OK);
            retApp.setMessage("发送成功");
        }catch (DataWarnningException e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("发送失败");
        }
        return retApp;
    }

	// 短信公共接口
	@RequestMapping(value = "/sms/message", produces = { "application/json" }, method = RequestMethod.POST)
	@ResponseBody
	public RetApp sendSmsToAppUser(HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		String content = getParam(request, "content", "");
		String mobile = getParam(request, "mobile", "");
		String sign = getParam(request, "sign", "");
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		try {

			if (StringUtils.isEmpty(mobile)) {
				throw new DataWarnningException("手机号不能为空");
			}else if(mobile.split(";").length>50){
				throw new DataWarnningException("超过单次人数上限");
			}
			if (StringUtils.isEmpty(content)) {
				throw new DataWarnningException("消息内容不能为空");
			}
			if (StringUtils.isEmpty(sign)) {
				throw new DataWarnningException("签名信息不能为空");
			}

			// 校验appid
			OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);
			Map<String, String> map = new HashMap();
			map.put("appid", appId);
			map.put("mobile", mobile);
			map.put("content", content);
			if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
				throw new DataWarnningException("签名验证未通过");
			} else {
				//logger.debug("手机号:{} 短信内容:{}", mobile, content);
				SmsUtil.send(openAppInfo, content, mobile.split(";"));
			}
			retApp.setStatus(OK);
			retApp.setMessage("发送成功");
		}catch (DataWarnningException e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		}catch(OAuth2Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage("发送失败");
		}

		return retApp;
	}

}

package com.cmig.future.csportal.module.weixin.helper;

import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.properties.company.dto.LjhBaseCompany;
import com.cmig.future.csportal.module.properties.company.dto.LjhCorpAgent;
import com.cmig.future.csportal.module.properties.company.service.ILjhBaseCompanyService;
import com.cmig.future.csportal.module.properties.company.service.ILjhCorpAgentService;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import com.cmig.future.csportal.module.weixin.entry.Corp;
import com.cmig.future.csportal.module.weixin.mp.aes.AesException;
import com.cmig.future.csportal.module.weixin.mp.aes.WXBizMsgCrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 11:27 2017/11/29.
 * @Modified by zhangtao on 11:27 2017/11/29.
 */
public class WorkWxHelper {

	private static ILjhBaseCompanyService ljhBaseCompanyService= SpringContextHolder.getBean("ljhBaseCompanyServiceImpl");
	private static ILjhCorpAgentService ljhCorpAgentService= SpringContextHolder.getBean("ljhCorpAgentServiceImpl");

	private static final String companyCorpMapKey="csp.work.wx.company.corp.map:";

	private static Map<String,Object> errorMap=new HashMap<>();


	private static Map<String, Object> getCompanyCorpMap() {
		if(!JedisUtils.exists(companyCorpMapKey)) {
			reloadCorpInfo();
		}
		return (Map<String, Object>) JedisUtils.getObjectMap(companyCorpMapKey).get("companyCorpMap");
	}

	private static Map<String, Corp> getCorpMap() {
		if(!JedisUtils.exists(companyCorpMapKey)) {
			reloadCorpInfo();
		}
		return (Map<String, Corp>) JedisUtils.getObjectMap(companyCorpMapKey).get("corpMap");
	}

	private static Map<String, Corp.Application> getAgentNoToCorpMap() {
		if(!JedisUtils.exists(companyCorpMapKey)) {
			reloadCorpInfo();
		}
		return (Map<String, Corp.Application>) JedisUtils.getObjectMap(companyCorpMapKey).get("agentNoToCorpMap");
	}

	static{
		loadErrorMap();
	}

	/**
	 * 清除缓存
	 */
	public static void cleanCorpInfo() {
		JedisUtils.del(companyCorpMapKey);
	}

	private static void reloadCorpInfo() {
		 Map<String,Object> companyCorpMap=new HashMap<>();
		/**
		 * 所有的企业微信帐号信息
		 */
		 Map<String,Corp> corpMap=new HashMap<>();

		Map<String,Corp.Application> agentNoToCorpMap=new HashMap<>();

		List<LjhBaseCompany> list=ljhBaseCompanyService.findCorpList();
		for(LjhBaseCompany company:list){
			//企业微信信息
			Corp corp=new Corp();
			corp.setCorpid(company.getCorpId());
			corp.setAddressSecret(company.getAddressSecret());
			corp.setServerUrl(company.getServerUrl());
			companyCorpMap.put(company.getOpenAppId()+":"+company.getSourceSystemId(),company.getCorpId());
			//JedisUtils.setObjectMap(companyCorpMapKey,companyCorpMap,0);

			LjhCorpAgent ljhCorpAgent=new LjhCorpAgent();
			ljhCorpAgent.setCorpId(company.getCompanyId());
			List<LjhCorpAgent> agentList=ljhCorpAgentService.findAgent(ljhCorpAgent);
			if(null!=agentList&&agentList.size()>0){
				for(LjhCorpAgent agent:agentList){
					//自建应用信息
					Corp.Application application=new Corp.Application();
					application.setCorpid(company.getCorpId());
					application.setAgentNo(agent.getAgentNo());
					application.setAgentId(agent.getAgentId());
					application.setAgentSecret(agent.getAgentSecret());
					application.setToken(agent.getMessageToken());
					application.setTncodingAESKey(agent.getTncodingAesKey());
					agentNoToCorpMap.put(agent.getAgentNo(),application);

					Map<String,Corp.Application> corpAppList=new HashMap<>();
					corpAppList.put(agent.getAgentNo(),application);
					corp.setApplicationMap(corpAppList);
				}
			}
			corpMap.put(corp.getCorpid(),corp);
		}

		Map map=new HashMap();
		map.put("companyCorpMap",companyCorpMap);
		map.put("agentNoToCorpMap",agentNoToCorpMap);
		map.put("corpMap",corpMap);
		JedisUtils.setObjectMap(companyCorpMapKey,map,0);
	}

	public static Corp getCorp(String appKey,String sourceSystemId){
		Map<String,Object> companyCorpMap=getCompanyCorpMap();
		Map<String,Corp> corpMap=getCorpMap();
		String openAppId=OAuthUtils.getOpenAppInfo(appKey).getId();
		String key=openAppId+":"+sourceSystemId;
		if(null!=companyCorpMap&&corpMap!=null&&companyCorpMap.containsKey(key)){
			return corpMap.get(companyCorpMap.get(key));
		}
		return null;
	}

	public static Corp getCorp(String corpid){
		Map<String,Corp> corpMap=getCorpMap();
		if(null!=corpMap&&corpMap.containsKey(corpid)){
			return corpMap.get(corpid);
		}
		return null;
	}

	public static String getCorpsecret(String corpid){
		Corp crop=getCorp(corpid);
		if(null!=crop){
			return crop.getAddressSecret();
		}
		return null;
	}

	public static String getAgentSecret(String agentNo){
		Corp.Application application=getAgentByNo(agentNo);
		if(null!=application){
			return application.getAgentSecret();
		}
		return null;
	}

	public static WXBizMsgCrypt getWxBizMsgCrypt(String agentNo) throws AesException {
		Corp.Application application=getAgentByNo(agentNo);
		if(null!=application){
			return new WXBizMsgCrypt(application.getToken(), application.getTncodingAESKey(), application.getCorpid());
		}
		return null;
	}

	public static String getCorpIdByAgentNo(String agentNo){
		Corp.Application application=getAgentByNo(agentNo);
		if(null!=application){
			return application.getCorpid();
		}
		return null;
	}

	public static Corp.Application getAgentByNo(String agentNo){
		if(!StringUtils.isEmpty(agentNo)){
			Map<String,Corp.Application> agentIdToCorpMap= getAgentNoToCorpMap();
			if(null!=agentIdToCorpMap&&agentIdToCorpMap.containsKey(agentNo)){
				return agentIdToCorpMap.get(agentNo);
			}
		}
		return null;
	}

	public static Corp.Application getDefaultAgent(Corp corp){
		if(null!=corp&&null!=corp.getApplicationMap()){
			for(String key:corp.getApplicationMap().keySet()){
				if(null!=corp.getApplicationMap().get(key)) {
					return corp.getApplicationMap().get(key);
				}
			}
		}
		return null;
	}

	private static void loadErrorMap() {
		errorMap.put("-1","系统繁忙");
		errorMap.put("0","请求成功");
		errorMap.put("40001","不合法的secret参数");
		errorMap.put("40003","无效的UserID");
		errorMap.put("40004","不合法的媒体文件类型");
		errorMap.put("40005","不合法的type参数");
		errorMap.put("40006","不合法的文件大小");
		errorMap.put("40007","不合法的media_id参数");
		errorMap.put("40008","不合法的msgtype参数");
		errorMap.put("40009","上传图片大小不是有效值");
		errorMap.put("40011","上传视频大小不是有效值");
		errorMap.put("40013","不合法的CorpID");
		errorMap.put("40014","不合法的access_token");
		errorMap.put("40016","不合法的按钮个数");
		errorMap.put("40017","不合法的按钮类型");
		errorMap.put("40018","不合法的按钮名字长度");
		errorMap.put("40019","不合法的按钮KEY长度");
		errorMap.put("40020","不合法的按钮URL长度");
		errorMap.put("40022","不合法的子菜单级数");
		errorMap.put("40023","不合法的子菜单按钮个数");
		errorMap.put("40024","不合法的子菜单按钮类型");
		errorMap.put("40025","不合法的子菜单按钮名字长度");
		errorMap.put("40026","不合法的子菜单按钮KEY长度");
		errorMap.put("40027","不合法的子菜单按钮URL长度");
		errorMap.put("40029","不合法的oauth_code");
		errorMap.put("40031","不合法的UserID列表");
		errorMap.put("40032","不合法的UserID列表长度");
		errorMap.put("40033","不合法的请求字符");
		errorMap.put("40035","不合法的参数");
		errorMap.put("40050","chatid不存在");
		errorMap.put("40054","不合法的子菜单url域名");
		errorMap.put("40055","不合法的菜单url域名");
		errorMap.put("40056","不合法的agentid");
		errorMap.put("40057","不合法的callbackurl或者callbackurl验证失败");
		errorMap.put("40058","不合法的参数");
		errorMap.put("40059","不合法的上报地理位置标志位");
		errorMap.put("40063","参数为空");
		errorMap.put("40066","不合法的部门列表");
		errorMap.put("40068","不合法的标签ID");
		errorMap.put("40070","指定的标签范围结点全部无效");
		errorMap.put("40071","不合法的标签名字");
		errorMap.put("40072","不合法的标签名字长度");
		errorMap.put("40073","不合法的openid");
		errorMap.put("40074","news消息不支持保密消息类型");
		errorMap.put("40077","不合法的pre_auth_code参数");
		errorMap.put("40078","不合法的auth_code参数");
		errorMap.put("40080","不合法的suite_secret");
		errorMap.put("40082","不合法的suite_token");
		errorMap.put("40083","不合法的suite_id");
		errorMap.put("40084","不合法的permanent_code参数");
		errorMap.put("40085","不合法的的suite_ticket参数");
		errorMap.put("40086","不合法的第三方应用appid");
		errorMap.put("40088","jobid不存在");
		errorMap.put("40089","批量任务的结果已清理");
		errorMap.put("40092","导入文件存在不合法的内容");
		errorMap.put("40093","不合法的jsapi_ticket参数");
		errorMap.put("40094","不合法的URL");
		errorMap.put("41001","缺少access_token参数");
		errorMap.put("41002","缺少corpid参数");
		errorMap.put("41004","缺少secret参数");
		errorMap.put("41006","缺少media_id参数");
		errorMap.put("41008","缺少auth code参数");
		errorMap.put("41009","缺少userid参数");
		errorMap.put("41010","缺少url参数");
		errorMap.put("41011","缺少agentid参数");
		errorMap.put("41033","缺少 description 参数");
		errorMap.put("41016","缺少title参数");
		errorMap.put("41019","缺少 department 参数");
		errorMap.put("41017","缺少tagid参数");
		errorMap.put("41021","缺少suite_id参数");
		errorMap.put("41025","缺少permanent_code参数");
		errorMap.put("42001","access_token已过期");
		errorMap.put("42007","pre_auth_code已过期");
		errorMap.put("42009","suite_access_token已过期");
		errorMap.put("43004","指定的userid未绑定微信或未关注微信插件");
		errorMap.put("44001","多媒体文件为空");
		errorMap.put("44004","文本消息content参数为空");
		errorMap.put("45001","多媒体文件大小超过限制");
		errorMap.put("45002","消息内容大小超过限制");
		errorMap.put("45004","应用description参数长度不符合系统限制");
		errorMap.put("45007","语音播放时间超过限制");
		errorMap.put("45008","图文消息的文章数量不符合系统限制");
		errorMap.put("45009","接口调用超过限制");
		errorMap.put("45022","应用name参数长度不符合系统限制");
		errorMap.put("45024","帐号数量超过上限");
		errorMap.put("45026","触发删除用户数的保护");
		errorMap.put("45032","图文消息author参数长度超过限制");
		errorMap.put("46003","菜单未设置");
		errorMap.put("46004","指定的用户不存在");
		errorMap.put("48002","API接口无权限调用");
		errorMap.put("48003","不合法的suite_id");
		errorMap.put("48004","授权关系无效");
		errorMap.put("48005","API接口已废弃");
		errorMap.put("50001","redirect_url未登记可信域名");
		errorMap.put("50002","成员不在权限范围");
		errorMap.put("50003","应用已禁用");
		errorMap.put("60001","部门长度不符合限制");
		errorMap.put("60003","部门ID不存在");
		errorMap.put("60004","父部门不存在");
		errorMap.put("60005","部门下存在成员");
		errorMap.put("60006","部门下存在子部门");
		errorMap.put("60007","不允许删除根部门");
		errorMap.put("60008","部门已存在");
		errorMap.put("60009","部门名称含有非法字符");
		errorMap.put("60010","部门存在循环关系");
		errorMap.put("60011","指定的成员/部门/标签参数无权限");
		errorMap.put("60012","不允许删除默认应用");
		errorMap.put("60020","访问ip不在白名单之中");
		errorMap.put("60028","不允许修改第三方应用的主页 URL");
		errorMap.put("60102","UserID已存在");
		errorMap.put("60103","手机号码不合法");
		errorMap.put("60104","手机号码已存在");
		errorMap.put("60105","邮箱不合法");
		errorMap.put("60106","邮箱已存在");
		errorMap.put("60107","微信号不合法");
		errorMap.put("60110","用户所属部门数量超过限制");
		errorMap.put("60111","UserID不存在");
		errorMap.put("60112","成员name参数不合法");
		errorMap.put("60123","无效的部门id");
		errorMap.put("60124","无效的父部门id");
		errorMap.put("60125","非法部门名字");
		errorMap.put("60127","缺少department参数");
		errorMap.put("60129","成员手机和邮箱都为空");
		errorMap.put("72023","发票已被其他公众号锁定");
		errorMap.put("72024","发票状态错误");
		errorMap.put("72037","存在发票不属于该用户");
		errorMap.put("80001","可信域名不正确，或者无ICP备案");
		errorMap.put("81001","部门下的结点数超过限制（3W）");
		errorMap.put("81002","部门最多15层");
		errorMap.put("81011","无权限操作标签");
		errorMap.put("82001","指定的成员/部门/标签全部无效");
		errorMap.put("82002","不合法的PartyID列表长度");
		errorMap.put("82003","不合法的TagID列表长度");
		errorMap.put("84014","成员票据过期");
		errorMap.put("84015","成员票据无效");
		errorMap.put("84019","缺少templateid参数");
		errorMap.put("84020","templateid不存在");
		errorMap.put("84021","缺少register_code参数");
		errorMap.put("84022","无效的register_code参数");
		errorMap.put("84023","不允许调用设置通讯录同步完成接口");
		errorMap.put("84024","无注册信息");
		errorMap.put("85002","包含不合法的词语");
		errorMap.put("85004","每企业每个月设置的可信域名不可超过20个");
		errorMap.put("85005","可信域名未通过所有权校验");
		errorMap.put("86001","参数 chatid 不合法");
		errorMap.put("86003","参数 chatid 不存在");
		errorMap.put("86216","存在非法会话成员ID");
		errorMap.put("86217","会话发送者不在会话成员列表中");
		errorMap.put("86220","指定的会话参数不合法");
		errorMap.put("90002","缺少摇一摇周边ticket参数");
		errorMap.put("90003","摇一摇周边ticket参数不合法");
		errorMap.put("91040","获取ticket的类型无效");
		errorMap.put("301002","无权限操作指定的应用");
		errorMap.put("301005","不允许删除创建者");
		errorMap.put("301012","参数 position 不合法");
		errorMap.put("301013","参数 telephone 不合法");
		errorMap.put("301014","参数 english_name 不合法");
		errorMap.put("301015","参数 mediaid 不合法");
		errorMap.put("301016","上传语音文件不符合系统要求");
		errorMap.put("301017","上传语音文件仅支持AMR格式");
		errorMap.put("301021","参数 userid 无效");
		errorMap.put("301022","获取打卡数据失败");
		errorMap.put("301023","useridlist非法或超过限额");
		errorMap.put("301024","获取打卡记录时间间隔超限");
		errorMap.put("301036","不允许更新该用户的userid");
		errorMap.put("302004","组织架构不合法（1不是一棵树，2 多个一样的partyid，3 partyid空，4 partyid name 空，5 同一个父节点下有两个子节点 部门名字一样 可能是以上情况，请一一排查）");
		errorMap.put("2000002","CorpId参数无效");
	}

	public static String getErrorMsg(String errorCode){
		if(errorMap.containsKey(errorCode)){
			return errorMap.get(errorCode).toString();
		}
		return null;
	}
}

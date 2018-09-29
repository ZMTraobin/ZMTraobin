package com.cmig.future.csportal.module.weixin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.module.weixin.WorkWxException;
import com.cmig.future.csportal.module.weixin.entry.Corp;
import com.cmig.future.csportal.module.weixin.entry.User;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:23 2017/12/5.
 * @Modified by zhangtao on 16:23 2017/12/5.
 */
@Service
public class UserService extends BaseService {

	/**
	 * 创建成员 
	 * @param corpid
	 * @param user
	 * @throws Exception
	 */
	public void createUser(String corpid,User user) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/user/create?access_token="+access_token;
		String result=HttpUtil.post(url,JSONObject.toJSONString(user));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	/**
	 * 根据主键查询成员详情
	 * @param corpid
	 * @param userId
	 * @throws Exception
	 */
	public User findByUserId(String corpid,String userId) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/user/get?access_token="+access_token+"&userid="+userId;
		String result=HttpUtil.get(url);
		User user=JSONObject.parseObject(result,User.class);
		if(new Integer(SUCCESS_CODE).intValue()!=user.getErrcode()){
			throw new WorkWxException(new Integer(user.getErrcode()).toString());
		}
		return user;
	}

	/**
	 * 修改成员 
	 * @param corpid
	 * @param user
	 * @throws Exception
	 */
	public void updateUser(String corpid,User user) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/user/update?access_token="+access_token;
		String result=HttpUtil.post(url,JSONObject.toJSONString(user));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	/**
	 * 删除
	 * @param corpid
	 * @param userId
	 * @throws Exception
	 */
	public void deleteUserByUserId(String corpid,String userId) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/user/delete?access_token="+access_token+"&userid="+userId;
		String result=HttpUtil.get(url);
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	/**
	 * 批量删除
	 * @param corpid
	 * @param userIds
	 * @throws Exception
	 */
	public void batchDeleteUserByUserId(String corpid,List<String> userIds) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/user/batchdelete?access_token="+access_token;
		JSONObject param=new JSONObject();
		param.put("useridlist",userIds);
		String result=HttpUtil.post(url,JSONObject.toJSONString(param));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	/**
	 * 获取部门下的成员列表-简单信息
	 * @param corpid
	 * @param departmentId
	 * @param fetchChild
	 * @return
	 * @throws Exception
	 */
	public List<User> getUserSimplelistByDepartmentId(String corpid,String departmentId,String fetchChild) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/user/simplelist?access_token="+access_token+"&department_id="+departmentId+"&fetch_child="+fetchChild;
		String result=HttpUtil.get(url);
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}else{
			String userList=jsonObject.get("userlist").toString();
			List<User> list= JSONArray.parseArray(userList,User.class);
			return list;
		}
	}

	/**
	 * 获取部门下的成员列表-详细信息
	 * @param corpid
	 * @param departmentId
	 * @param fetchChild
	 * @return
	 * @throws Exception
	 */
	public List<User> getUserListByDepartmentId(String corpid,String departmentId,String fetchChild) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/user/list?access_token="+access_token+"&department_id="+departmentId+"&fetch_child="+fetchChild;
		String result=HttpUtil.get(url);
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}else{
			String userList=jsonObject.get("userlist").toString();
			List<User> list= JSONArray.parseArray(userList,User.class);
			return list;
		}
	}

	/**
	 * userid转openid
	 * @param userid
	 * @param agentNo
	 * @return
	 * @throws Exception
	 */
	public String useridToOpenid(String userid,String agentNo) throws Exception {
		Corp.Application agent=WorkWxHelper.getAgentByNo(agentNo);
		String access_token=getAccessToken(agent.getCorpid(), WorkWxHelper.getCorpsecret(agent.getCorpid()));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/user/convert_to_openid?access_token="+access_token;
		JSONObject param=new JSONObject();
		param.put("userid",userid);
		param.put("agentid",agent.getAgentId());
		String result=HttpUtil.post(url,JSONObject.toJSONString(param));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}else{
			String openid=jsonObject.get("openid").toString();
			String appid=jsonObject.get("appid")==null?"":jsonObject.get("appid").toString();
			return openid;
		}
	}

	/**
	 * openid转userid
	 * @param corpid
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public String openidToUserId(String corpid, String openid) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/user/convert_to_userid?access_token="+access_token;
		JSONObject param=new JSONObject();
		param.put("openid",openid);
		String result=HttpUtil.post(url,JSONObject.toJSONString(param));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}else{
			String userid=jsonObject.get("userid").toString();
			return userid;
		}
	}
}

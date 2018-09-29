package com.cmig.future.csportal.module.weixin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.module.weixin.WorkWxException;
import com.cmig.future.csportal.module.weixin.entry.Tag;
import com.cmig.future.csportal.module.weixin.entry.User;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:23 2017/12/5.
 * @Modified by zhangtao on 16:23 2017/12/5.
 */
@Service
public class TagService extends BaseService{

	private static final Logger logger= LoggerFactory.getLogger(TagService.class);

	/**
	 * 新建tag
	 * @param corpid
	 * @param tag
	 * @return
	 * @throws Exception
	 */
	public int create(String corpid, Tag tag) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/tag/create?access_token="+access_token;
		String result= HttpUtil.post(url,JSONObject.toJSONString(tag));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}else{
			String tagid=jsonObject.get("tagid").toString();
			return new Integer(tagid);
		}
	}

	/**
	 * 更新tag
	 * @param corpid
	 * @param tag
	 * @throws Exception
	 */
	public void update(String corpid, Tag tag) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/tag/update?access_token="+access_token;
		String result= HttpUtil.post(url,JSONObject.toJSONString(tag));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	/**
	 * 删除tag
	 * @param corpid
	 * @param tagid
	 * @throws Exception
	 */
	public void delete(String corpid, int tagid) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/tag/delete?access_token="+access_token+"&tagid="+tagid;
		String result= HttpUtil.get(url);
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	/**
	 * 获取标签成员
	 * @param corpid
	 * @param tagid
	 * @return
	 * @throws Exception
	 */
	public List<User> getUserList(String corpid, int tagid) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/tag/get?access_token="+access_token+"&tagid="+tagid;
		String result= HttpUtil.get(url);
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
	 * 增加标签成员
	 * @param corpid
	 * @param tagid
	 * @param userlist
	 * @param partyList
	 * @throws Exception
	 */
	public void addtagusers(String corpid,int tagid, List<String> userlist,List<Integer> partyList) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/tag/addtagusers?access_token="+access_token;
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("tagid",tagid);
		jsonObject.put("userlist",userlist);
		jsonObject.put("partylist",partyList);

		String result= HttpUtil.post(url,JSONObject.toJSONString(jsonObject));
		jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}else if(jsonObject.containsKey("invalidlist")){
			logger.error("invalidlist {} ",jsonObject.get("invalidlist").toString());
		}else if(jsonObject.containsKey("invalidparty")){
			logger.error("invalidparty {} ",jsonObject.get("invalidparty").toString());
		}
	}

	/**
	 * 删除标签成员
	 * @param corpid
	 * @param tagid
	 * @param userlist
	 * @param partyList
	 * @throws Exception
	 */
	public void deltagusers(String corpid,int tagid, List<String> userlist,List<Integer> partyList) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/tag/deltagusers?access_token="+access_token;
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("tagid",tagid);
		jsonObject.put("userlist",userlist);
		jsonObject.put("partylist",partyList);

		String result= HttpUtil.post(url,JSONObject.toJSONString(jsonObject));
		jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}else if(jsonObject.containsKey("invalidlist")){
			logger.error("invalidlist {} ",jsonObject.get("invalidlist").toString());
		}else if(jsonObject.containsKey("invalidparty")){
			logger.error("invalidparty {} ",jsonObject.get("invalidparty").toString());
		}
	}

	/**
	 * 获取标签列表
	 * @param corpid
	 * @return
	 * @throws Exception
	 */
	public List<Tag> getList(String corpid) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/tag/list?access_token="+access_token;
		String result= HttpUtil.get(url);
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}else{
			String taglist=jsonObject.get("taglist").toString();
			List<Tag> list= JSONArray.parseArray(taglist,Tag.class);
			return list;
		}
	}

}

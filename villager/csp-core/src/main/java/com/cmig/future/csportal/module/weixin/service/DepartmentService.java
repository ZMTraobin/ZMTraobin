package com.cmig.future.csportal.module.weixin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.module.weixin.WorkWxException;
import com.cmig.future.csportal.module.weixin.entry.Department;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:33 2017/12/6.
 * @Modified by zhangtao on 14:33 2017/12/6.
 */
@Service
public class DepartmentService extends BaseService {

	/**
	 * 新建部门
	 * @param corpid
	 * @param department
	 * @return
	 * @throws Exception
	 */
	public int create(String corpid, Department department) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/department/create?access_token="+access_token;
		String result= HttpUtil.post(url,JSONObject.toJSONString(department));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}else{
			String id=jsonObject.get("id").toString();
			return new Integer(id);
		}
	}

	/**
	 * 更新
	 * @param corpid
	 * @param department
	 * @throws Exception
	 */
	public void update(String corpid, Department department) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/department/update?access_token="+access_token;
		String result= HttpUtil.post(url,JSONObject.toJSONString(department));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	/**
	 * 删除
	 * @param corpid
	 * @param id
	 * @throws Exception
	 */
	public void delete(String corpid, int id) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/department/delete?access_token="+access_token+"&id="+id;
		String result= HttpUtil.get(url);
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	/**
	 * 获取部门列表
	 * @param corpid
	 * @param parentid
	 * @return
	 * @throws Exception
	 */
	public List<Department> getList(String corpid, int parentid) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url =WORK_WX_SERVER_URL+"/cgi-bin/department/list?access_token="+access_token+"&id="+parentid;
		String result= HttpUtil.get(url);
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}else{
			String department=jsonObject.get("department").toString();
			List<Department> list= JSONArray.parseArray(department,Department.class);
			return list;
		}
	}

}

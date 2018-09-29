package com.cmig.future.csportal.module.weixin.service;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.module.weixin.WorkWxException;
import com.cmig.future.csportal.module.weixin.entry.Media;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 15:39 2017/12/6.
 * @Modified by zhangtao on 15:39 2017/12/6.
 */
@Service
public class MediaService extends BaseService {

	public Media upload(String corpid, String type, File file) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url= WORK_WX_SERVER_URL+"/cgi-bin/media/upload?access_token="+access_token+"&type="+type;
		Map<String,String> headers=new HashMap<>();
		headers.put("Content-Type","multipart/form-data");
		headers.put("Content-Disposition","form-data; name=\"media\";filename=\""+file.getName()+"\"");
		String result=HttpUtil.post(url,null,headers);
		Media media= JSONObject.parseObject(result,Media.class);
		if(!SUCCESS_CODE.equals(media.getErrcode())){
			throw new WorkWxException(new Integer(media.getErrcode()).toString());
		}else{
			return media;
		}
	}

	public void get(String corpid,String mediaId) throws Exception {
		String access_token=getAccessToken(corpid, WorkWxHelper.getCorpsecret(corpid));
		String url= WORK_WX_SERVER_URL+"/cgi-bin/media/get?access_token="+access_token+"&media_id="+mediaId;
	}
}

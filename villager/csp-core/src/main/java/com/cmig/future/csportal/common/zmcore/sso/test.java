package com.cmig.future.csportal.common.zmcore.sso;

import com.cmig.future.csportal.common.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 13:09 2017/6/15.
 * @Modified by zhangtao on 13:09 2017/6/15.
 */
public class test {

	private static Logger log=LoggerFactory.getLogger(test.class);
	//创建一个可重用固定线程数的线程池
	static ExecutorService pool = Executors.newFixedThreadPool(5);

	public static void main(String[] args) {
		for(int i=1;i<=10000;i++){
			String sq=new Integer(i).toString();
			String mobile="177000";
			if(sq.length()==1){
				mobile=mobile+"0000"+i;
			}else if(sq.length()==2){
				mobile=mobile+"000"+i;
			}else if(sq.length()==3){
				mobile=mobile+"00"+i;
			}else if(sq.length()==4){
				mobile=mobile+"0"+i;
			}else{
				mobile=mobile+i;
			}
			final String mobileThis=mobile;
			Runnable r= new Runnable(){
				public void run(){
					try {
						//Thread.sleep(1000);
						String password="a123456";
						String registrationInvitationCode="ylcs";
						String result = HttpUtil.post("http://127.0.0.1:8383/hap/user/appuser/register?mobile=" + mobileThis + "&password=" + password + "&registrationInvitationCode" + registrationInvitationCode);
						com.alibaba.fastjson.JSONObject jsonObject= com.alibaba.fastjson.JSONObject.parseObject(result);
						if(!"1".equals(jsonObject.get("status"))){
							log.debug(mobileThis+"/结果:"+jsonObject.get("message"));
						}else{
							log.debug(mobileThis+"/结果:"+jsonObject.get("message"));
						}
					}catch (Exception e){
						log.debug(mobileThis+"/结果:"+e.getMessage());
					}
				}
			};
			pool.execute(r);
		}
	}
}

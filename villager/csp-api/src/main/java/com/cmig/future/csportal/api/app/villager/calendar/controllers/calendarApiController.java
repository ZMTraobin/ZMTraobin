/**
 * .
 */
package com.cmig.future.csportal.api.app.villager.calendar.controllers;

import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * app用户Controller
 *
 * @author su
 * @version 2018
 */
@RestController
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class calendarApiController extends BaseExtendController {
    /**
     * 获取黄历
     */
    @RequestMapping(value = "/villager/integral/queryCalendar", produces = {"application/json"}, method = RequestMethod.GET)
    public RetApp query(@RequestParam("day") String day, HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	
    	String rday=null;
    	JSONObject aaa=null;
		try {
            URL url = new URL("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query="+day+"&resource_id=6018&format=json");
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if(200 == urlConnection.getResponseCode()){
                //得到输入流
                InputStream is =urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while(-1 != (len = is.read(buffer))){
                    baos.write(buffer,0,len);
                    baos.flush();
                }
                rday = baos.toString();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
		try {
		JSONObject jsonReq = JSONObject.fromObject(rday);
		JSONArray data = JSONArray.fromObject(jsonReq.getString("data"));
		JSONObject almanac = JSONObject.fromObject(data.get(0));
		JSONArray data1 = JSONArray.fromObject(almanac.getString("almanac"));
		System.out.println(data1);
		String date=day.replace("-0", "-");
		for (int i = 0; i < data1.size(); i++) {
			JSONObject json =JSONObject.fromObject(data1.get(i));
			if(json.getString("date").equals(date)) {
				aaa = json;
			}
		}
		 }  catch (Exception e1) {
	            e1.printStackTrace();
	            Map map=new HashMap<>();
	            map.put("date",day);
	            map.put("avoid","");
	            map.put("suit","");
	            retApp.setData(map);
	            retApp.setStatus(OK);
	            retApp.setTotall((long)0);
	            retApp.setMessage("查询成功!");
	            return retApp;
	        }

        retApp.setData(aaa);
        retApp.setStatus(OK);
        retApp.setTotall((long)0);
        retApp.setMessage("查询成功!");
        return retApp;
    }
}
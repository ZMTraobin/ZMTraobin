package com.cmig.future.csportal.api.acm.controllers;

import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@ResponseBody
@RequestMapping(value = "common/")
public class AppCommonController extends BaseExtendController {

	private static final Logger logger= LoggerFactory.getLogger(AppCommonController.class);

	//创建一个可重用固定线程数的线程池
	static ExecutorService pool = Executors.newFixedThreadPool(5);

    @RequestMapping(value = "gate/open", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp getRsaPublicKey(HttpServletRequest request) {
        JSONObject jsonObject=new JSONObject();
	    Runnable r=new Runnable(){
		    @Override
		    public void run() {
			    long i=0;
			    while(true) {
				    logger.debug(new Long(i++).toString());
				    try {
					    Thread.sleep(1000);
				    } catch (InterruptedException e) {
					    e.printStackTrace();
				    }
			    }
		    }
	    };
	    pool.execute(r);

	    return RetAppUtil.success(jsonObject,"开门成功");
    }


}

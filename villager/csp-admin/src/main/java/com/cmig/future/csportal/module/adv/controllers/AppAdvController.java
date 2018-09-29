package com.cmig.future.csportal.module.adv.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmig.future.csportal.common.utils.RequestUtils;
import com.cmig.future.csportal.module.adv.dto.AppAdv;
import com.cmig.future.csportal.module.adv.dto.AppAdvReq;
import com.cmig.future.csportal.module.adv.dto.AppAdvVO;
import com.cmig.future.csportal.module.adv.service.IAppAdvService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;

/**
 * class name: AppAdvController 
 * author: fri
 * date: 2017年4月19日
 * function: 广告管理
 */
@Controller
@RequestMapping(value = "/app/adv")
public class AppAdvController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(AppAdvController.class);

    @Autowired
    private IAppAdvService service;
    
    /**
     * class name: saveNewAdv
     * author: fri
     * date: 2017年8月18日
     * function: 新版保存广告信息
     * in:
     *		parameter1: request 请求
     *		parameter2: record 请求对象
     * out:
     *		parameter: ResponseData 保存结果
     * out:
     *		parameter: ResponseData
     */
    @ResponseBody
    @RequestMapping(value = "/submit/newadv",method = RequestMethod.POST)
    public ResponseData saveNewAdv(@RequestBody AppAdvVO appAdvs,HttpServletRequest request,BindingResult bindingResult) throws Exception{
    	logger.info("保存广告信息 start");
    	if(null == appAdvs.getAdvTitle() || null == appAdvs.getAdvType() || null == appAdvs.getAppAdvs() || 0 == appAdvs.getAppAdvs().size()){
    		logger.info("缺少必传参数");
    		ResponseData responseData = new ResponseData();
    		responseData.setMessage("缺少必传参数");
    		responseData.setSuccess(false);
    		return responseData;
    	}
        IRequest irequest = createRequestContext(request);
        return new ResponseData(service.saveNewAdv(irequest,appAdvs));
    }
    
    /**
     * class name: queryNewAdv
     * author: fri
     * date: 2017年8月21日
     * function: 查询广告信息
     * in:
     *		parameter1: page 起始页
     *		parameter2: size 每页显示条数
     *		parameter3: request 请求
     *		parameter4: record 请求对象
     * out:
     *		parameter: ResponseData 查询结果
     * @throws IOException 
     */
    @ResponseBody
    @RequestMapping(value = "/query/newadv",method = RequestMethod.POST)
    public ResponseData queryNewAdv(
    		AppAdvReq record,
    		@RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize,
    		HttpServletRequest request,BindingResult result){
    	logger.info("查询广告信息 start");
        IRequest irequest = createRequestContext(request);
        return new ResponseData(service.queryNewAdv(irequest, record, page, pagesize));
    }
    
    /**
     * class name: queryNewOneAdv
     * author: fri
     * date: 2017年8月21日
     * function: 查询广告编辑信息
     * in:
     *		parameter1: request 请求
     *		parameter2: appAdv 请求对象
     * out:
     *		parameter: ResponseData 查询结果
     * @throws Exception 
     * @throws IOException 
     */
    @ResponseBody
    @RequestMapping(value = "/query/neweditadv",method = RequestMethod.POST)
    public ResponseData queryNewOneAdv(
    		AppAdv appAdv,
    		HttpServletRequest request) throws Exception{
    	logger.info("编辑广告信息查询 start");
    	if(null != appAdv && null != appAdv.getAdvId()){
    		ResponseData responseData = new ResponseData();
    		responseData.setMessage("参数传递错误：请使用groupIdentifying");
    		responseData.setSuccess(false);
    		return responseData;
    	}
    	String[] reParam = {"groupIdentifying"};
    	Boolean isParamOk = RequestUtils.isReParamOk(reParam, appAdv);
    	if(!isParamOk){
    		ResponseData responseData = new ResponseData();
    		responseData.setMessage("缺少必传参数");
    		responseData.setSuccess(false);
    		return responseData;
    	}
        IRequest irequest = createRequestContext(request);
        return new ResponseData(service.getNewOneAdv(irequest,appAdv));
    }
    
    /**
     * class name: editNewAdv
     * author: fri
     * date: 2017年8月21日
     * function: 修改广告信息
     * in:
     *		parameter1: request 请求
     *		parameter2: record 请求对象
     * out:
     *		parameter: ResponseData 修改结果
     */
    @ResponseBody
    @RequestMapping(value = "/edit/newadv",method = RequestMethod.POST)
    public ResponseData editNewAdv(@RequestBody AppAdvVO appAdvs,HttpServletRequest request,BindingResult result) throws Exception{
    	logger.info("修改广告信息 start");
    	if(null == appAdvs.getAdvTitle() || null == appAdvs.getAdvType() || null == appAdvs.getAppAdvs() || 0 == appAdvs.getAppAdvs().size()){
    		logger.info("缺少必传参数");
    		ResponseData responseData = new ResponseData();
    		responseData.setMessage("缺少必传参数");
    		responseData.setSuccess(false);
    		return responseData;
    	}
        IRequest iRequest = createRequestContext(request);
        Boolean isOk = service.updateBatchAdv(iRequest, appAdvs);
        return new ResponseData(isOk);
    }
    
    /**
     * class name: queryNewSort
     * author: fri
     * date: 2017年8月21日
     * function: 查询广告上下线顺序数据
     * in:
     *		parameter1: request
     * out:
     *		parameter: ResponseData
     */
    @RequestMapping(value = "/get/newsort",method = RequestMethod.GET)
    public ResponseData queryNewSort(HttpServletRequest request){
    	logger.info("查询广告上下线顺序数据 start");
    	IRequest iRequest = createRequestContext(request);
    	return new ResponseData(service.queryNewAdvSort(iRequest));
    }
    
    /**
     * class name: saveNewSort
     * author: fri
     * date: 2017年8月21日
     * function: 提交广告广告位置顺序
     * in:
     *		parameter1: request 请求
     *		parameter2: list 请求数据集合
     * out:
     *		parameter: ResponseData 返回结果
     */
    @RequestMapping(value = "/submit/newsort",method = RequestMethod.POST,produces={"application/json"})
    public ResponseData saveNewSort(@RequestBody List<Object> list,HttpServletRequest request,BindingResult result){
    	logger.info("提交广告广告位置顺序 start");
    	if(null == list || null == list.get(0)){
    		logger.info("排序数据不足,应包含offline和online");
    		ResponseData responseData = new ResponseData();
    		responseData.setMessage("排序数据不足，应包含offline和online");
    		responseData.setSuccess(false);
    		return responseData;
    	}
    	IRequest iRequest = createRequestContext(request);
    	return new ResponseData(service.updateNewAdvSort(iRequest, list));
    }
    
    /**
     * class name: deleteNewAdv
     * author: fri
     * date: 2017年8月22日
     * function: 删除广告
     * in:
     *		parameter1: ids 请求删除ID
     * out:
     *		parameter: ResponseData 返回结果
     */
    @RequestMapping(value = "/delete/newadv",method = RequestMethod.POST)
    public ResponseData deleteNewAdv(@RequestBody List<AppAdv> appAdv){
    	logger.info("删除广告 start");
        return new ResponseData(service.deleteBatchByGroupId(appAdv));
    }
    
    /**
     * class name: saveNewExamine
     * author: fri
     * date: 2017年8月22日
     * function: 提交广告审核信息
     * in:
     *		parameter1: request 请求
     *		parameter2: record 请求对象	advId/status/description
     *				审核状态：1同意  2拒绝
     * out:
     *		parameter: ResponseData 返回数据
     * @throws Exception 
     */
    @ResponseBody
    @RequestMapping(value = "/submit/newexamine",method = RequestMethod.POST,produces={"application/json"})
    public ResponseData saveNewExamine(
    		@RequestBody AppAdv appAdv,
    		HttpServletRequest request,BindingResult result) throws Exception{
    	logger.info("提交广告审核信息 start");
    	String[] reParam = {"status","groupIdentifying"};
    	Boolean isParamOk = RequestUtils.isReParamOk(reParam, appAdv);
    	if(!isParamOk)
    		return new ResponseData(false);
        if(appAdv.getStatus() == 1L){
        	appAdv.setStatus(3L);//待上线
        }else{
        	appAdv.setStatus(2L);//审核失败
        }
        return new ResponseData(service.updateAdvExamine(appAdv));
    }
    
}
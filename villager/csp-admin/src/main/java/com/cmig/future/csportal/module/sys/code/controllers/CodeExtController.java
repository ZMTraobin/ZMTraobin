/*
 * #{copyright}#
 */

package com.cmig.future.csportal.module.sys.code.controllers;

import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.hand.hap.cache.impl.SysCodeCache;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 快速编码Controller.
 * 
 * @author njq.niu@hand-china.com
 *
 *         2016年3月2日
 */
@Controller
public class CodeExtController extends BaseController {

	@Autowired
	private SysCodeCache codeCache;

	@Autowired
	private IOrderFormService orderFormService;


    @RequestMapping(value = "/sys/codevalue/queryByCode")
    @ResponseBody
    public ResponseData getCodeValues(Code code, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize, HttpServletRequest request) {
	    Code dbCode= codeCache.getValue(code.getCode());
        return new ResponseData(dbCode.getCodeValues());
    }

	@RequestMapping(value = "/sys/code/queryByCode")
	@ResponseBody
	public ResponseData getCodes(Code code, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	                             @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize, HttpServletRequest request) {
		Code dbCode= codeCache.getValue(code.getCode());
		List<Code> codes=new ArrayList<>();
		codes.add(dbCode);
		return new ResponseData(codes);
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/sys/build/merchant/qr", produces = {"application/json"}, method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String bulidMerchantQr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String merchantNo=request.getParameter("merchantNo")==null?"":request.getParameter("merchantNo");
		String fid = orderFormService.bulidMerchantQr(merchantNo);
		return fid;
	}

}

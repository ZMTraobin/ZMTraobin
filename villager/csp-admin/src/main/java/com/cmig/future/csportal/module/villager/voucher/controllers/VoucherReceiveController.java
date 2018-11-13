package com.cmig.future.csportal.module.villager.voucher.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.excel.ExportExcel;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceive;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceiveExcel;
import com.cmig.future.csportal.module.villager.voucher.service.IVoucherReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

    @Controller
    public class VoucherReceiveController extends BaseController{

    @Autowired
    private IVoucherReceiveService service;


    @RequestMapping(value = "/xl/voucher/receive/query")
    @ResponseBody
    public ResponseData query(VoucherReceive dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/xl/voucher/receive/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<VoucherReceive> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/xl/voucher/receive/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<VoucherReceive> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/xl/voucher/receive/export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String useStatus = request.getParameter("useStatus");
        if(StringUtils.isEmpty(useStatus)){
        	useStatus="3";
        }
		List<VoucherReceiveExcel> list = service.selectByUseStatus(useStatus);
		String fileName = "待充值用户.xlsx";
		new ExportExcel("待充值用户", VoucherReceiveExcel.class, 2).setDataList(list).write(response, fileName,request).dispose();
	}
    
    }
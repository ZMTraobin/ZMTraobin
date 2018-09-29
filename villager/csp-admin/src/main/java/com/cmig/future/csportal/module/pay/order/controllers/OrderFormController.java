package com.cmig.future.csportal.module.pay.order.controllers;

import com.cmig.future.csportal.common.utils.excel.ExportExcel;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.dto.OrderFormExcel;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.hand.hap.core.BaseConstants;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class OrderFormController extends BaseController { 

    @Override
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat(BaseConstants.DATE_TIME_FORMAT);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
    }

    @Autowired
    private IOrderFormService service;

    @RequestMapping(value = "/csp/order/form/query")
    @ResponseBody
    public ResponseData query(OrderForm dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<OrderForm> list = service.selectByCondition(requestContext, dto, page, pageSize);
        return new ResponseData(list);
    }
    
//    @RequestMapping(value = "/csp/order/form/queryById")
//    @ResponseBody
//    public ResponseData queryById(OrderForm dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
//            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
//        IRequest requestContext = createRequestContext(request);
//        return new ResponseData(service.queryById(requestContext, dto, page, pageSize));
//    }

    @RequestMapping(value = "/csp/order/form/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<OrderForm> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/order/form/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<OrderForm> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/order/form/queryById")
    @ResponseBody
    public ResponseData queryById(HttpServletRequest request, OrderForm dto) {
        IRequest requestCtx = createRequestContext(request);
        dto = service.selectByPrimaryKey(requestCtx,dto);
        return new ResponseExtData(dto);
    }

	@RequestMapping(value = "/csp/order/form/export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response, OrderForm dto) throws IOException {
		IRequest requestContext = RequestHelper.createServiceRequest(request);
		List<OrderForm> list = service.selectByCondition(requestContext, dto, 1, 100000);
		List<OrderFormExcel> dataList=new ArrayList();
		for(OrderForm orderForm:list){
			OrderFormExcel orderFormExcel=new OrderFormExcel();
			orderFormExcel.setOrderNumber(orderForm.getOrderNumber());
			orderFormExcel.setPayChannel(orderForm.getPayChannel());
			orderFormExcel.setCreationDate(orderForm.getCreationDate());
			orderFormExcel.setTimePaid(orderForm.getTimePaid());
			orderFormExcel.setOrderAmount(orderForm.getOrderAmount());
			orderFormExcel.setDiscountAmount(orderForm.getDiscountAmount());
			orderFormExcel.setIntegralAmount(orderForm.getIntegralAmount());
			orderFormExcel.setPayableAmount(orderForm.getPayableAmount());
			orderFormExcel.setPaidAmount(orderForm.getPaidAmount());
			orderFormExcel.setMerchantNo(orderForm.getMerchantNo());
			orderFormExcel.setPayStatus(orderForm.getPayStatus());
			orderFormExcel.setOrderType(orderForm.getOrderType());
			orderFormExcel.setTranseq(orderForm.getTranseq());
			orderFormExcel.setClientIp(orderForm.getClientIp());
			dataList.add(orderFormExcel);

		}
		String fileName = "订单信息.xlsx";
		new ExportExcel("订单信息", OrderFormExcel.class, 2).setDataList(dataList).write(response, fileName,request).dispose();
	}
}
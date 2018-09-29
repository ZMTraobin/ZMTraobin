package com.cmig.future.csportal.module.pay.payment.controllers;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.pay.payment.dto.PaymentConfig;
import com.cmig.future.csportal.module.pay.payment.service.IPaymentConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
    public class PaymentConfigController extends BaseController{
    @Autowired
    private IPaymentConfigService paymentConfigService;
    @RequestMapping(value = "/csp/ljh/payment/config/query")
    @ResponseBody
    public ResponseData query(PaymentConfig dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(paymentConfigService.selectPayment(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/ljh/payment/config/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<PaymentConfig> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(paymentConfigService.batchUpdate(requestCtx, dto));
    }

    /**
     * 进行批量删除也就是批量更新状态
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/payment/config/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request){

        PaymentConfig dto = new PaymentConfig();
        String[]payMentIds = request.getParameterValues("arr");
        Map<String,Object> paramMap = new HashMap<String,Object>();
        List<String>listIds = new ArrayList<String>();
        for(int i = 0;i<payMentIds.length;i++){
            listIds.add(payMentIds[i]);
        }
        paramMap.put("DEL_FLAG_DELETE",dto.DEL_FLAG_DELETE);
        paramMap.put("list",listIds);
        paymentConfigService.bathupdatePayment(paramMap);




        //paymentConfigService.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 添加账号配置
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/csp/ljh/payment/config/add")
    @ResponseBody
    public ResponseData add(HttpServletRequest request,@RequestBody PaymentConfig dto){
        dto.setIsenable(Constants.APP_CONFIG_STATUS_ON);
        dto.setIsDel(dto.DEL_FLAG_NORMAL);
        dto.setPayMentYES(Constants.YES);
        //再添加账号的时候查看有没有作为默认的支付账号
        PaymentConfig paymentConfig = paymentConfigService.selectPaymentIsDefault(dto);
        if(paymentConfig!=null && dto.getIsDefault().equals(Constants.YES)){
           // PaymentConfig paymentConfig = list.get(0);
            paymentConfigService.updatePaymentDefault(paymentConfig);
            paymentConfigService.addPaymentConfig(dto);
            return new ResponseData();
        }else if(paymentConfig==null && dto.getIsDefault().equals(Constants.NO)){
            throw  new DataWarnningException("必须指定默认账户");
        }else if(paymentConfig==null && dto.getIsDefault().equals(Constants.YES)){
            paymentConfigService.addPaymentConfig(dto);
            return new ResponseData();
        }
        paymentConfigService.addPaymentConfig(dto);
        return new ResponseData();
    }

    /**
     * 获取对象
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/payment/config/getPaymentById")
    @ResponseBody
    public ResponseData getPaymentById(HttpServletRequest request){
        String id = request.getParameter("id");
        PaymentConfig paymentConfig = new PaymentConfig();
        paymentConfig.setId(Long.parseLong(id));

         paymentConfig = paymentConfigService.getPaymentById(paymentConfig);
        return new ResponseExtData(paymentConfig);
    }

    /**
     * 更新对象
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/payment/config/updatePayment")
    @ResponseBody
    public ResponseData updatePayment(HttpServletRequest request,@RequestBody PaymentConfig dto ){
        //再添加账号的时候查看有没有作为默认的支付账号
        PaymentConfig paymentConfig = paymentConfigService.selectPaymentIsDefault(dto);
        if(paymentConfig==null && dto.getIsDefault().equals(Constants.NO)){
            throw  new DataWarnningException("没有默认账户");
        }else if(paymentConfig!=null && dto.getIsDefault().equals(Constants.NO)){
            throw  new DataWarnningException("至少指定一个默认账户");

        }else if(paymentConfig!=null && dto.getIsDefault().equals(Constants.YES)){
            paymentConfigService.updatePaymentDefault(paymentConfig);
            paymentConfigService.updatePayment(dto);
        }else if(paymentConfig==null && dto.getIsDefault().equals(Constants.YES)){
            paymentConfigService.updatePayment(dto);
        }
        return new ResponseData();
    }

    /**
     * 停用账号
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/payment/config/stop")
    @ResponseBody
    public ResponseData stop(HttpServletRequest request){
        String id = request.getParameter("id");
        PaymentConfig dto = new PaymentConfig();
        dto.setId(Long.parseLong(id));
        dto.setIsenable(Constants.APP_CONFIG_STATUS_OFF);
        paymentConfigService.stopPayment(dto);
        return new ResponseData();
    }

    /**
     * 开启账号
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/payment/config/start")
    @ResponseBody
    public ResponseData start(HttpServletRequest request){
        String id = request.getParameter("id");
        PaymentConfig dto = new PaymentConfig();
        dto.setId(Long.parseLong(id));
        dto.setIsenable(Constants.APP_CONFIG_STATUS_ON);
        paymentConfigService.startPayment(dto);
        return new ResponseData();
    }
    }
package com.cmig.future.csportal.module.integral.controllers;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.operate.integral.IntegralRuleSign;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.MD5;
import com.cmig.future.csportal.module.integral.dto.IntegralFlowing;
import com.cmig.future.csportal.module.integral.service.IIntegralFlowingService;
import com.cmig.future.csportal.module.operate.integral.IntegralUrl;
import com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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
import java.util.*;
@Controller
    public class IntegralFlowingController extends BaseController{
    @Autowired
    private IIntegralFlowingService integralFlowingService;
    @RequestMapping(value = "/csp/ljh/integral/flowing/query")
    @ResponseBody
    public ResponseData query(IntegralFlowing dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(integralFlowingService.selectAllIntegral(requestContext,dto,page,pageSize));
    }
    @RequestMapping(value = "/csp/ljh/integral/flowing/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<IntegralFlowing> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(integralFlowingService.batchUpdate(requestCtx, dto));
    }
    @RequestMapping(value = "/csp/ljh/integral/flowing/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<IntegralFlowing> dto){
        integralFlowingService.batchDelete(dto);
        return new ResponseData();
    }
    /**
     * 重新发送
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/csp/ljh/integral/flowing/resetSend")
    @ResponseBody
    public ResponseData resetSend(HttpServletRequest request,IntegralFlowing dto) {
            IntegralFlowing integralFlowing = integralFlowingService.getIntegralFlowingById(dto);
            String message = "";
            if (integralFlowing != null && integralFlowing.getIntegralType().equals("ADD")) {
                switch (integralFlowing.getStatus()) {
                    case "1006":
                    message = restAddIntegral(integralFlowing);
                        break;
                    case "1008":
                    message = restAddIntegral(integralFlowing);
                        break;
                    case "1016":
                    message = restAddIntegral(integralFlowing);
                        break;
                }
            }else if(integralFlowing!=null && integralFlowing.getIntegralType().equals("DEDUCTION")){
                switch (integralFlowing.getStatus()) {
                    case "1006":
                        message =deductionIntegral(integralFlowing);
                        break;
                    case "1008":
                        message =  deductionIntegral(integralFlowing);
                        break;
                    case "1016":
                        message = deductionIntegral(integralFlowing);
                        break;
                }
            }else if(integralFlowing!=null && integralFlowing.getIntegralType().equals("FREEZE")){
                switch (integralFlowing.getStatus()) {
                    case "1006":
                        message = restfreeze(integralFlowing);
                        break;
                    case "1008":
                        message =  restfreeze(integralFlowing);
                        break;
                    case "1016":
                        message =  restfreeze(integralFlowing);
                        break;
                }
            }else if(integralFlowing!=null && integralFlowing.getIntegralType().equals("UNFREEZE")){
                switch (integralFlowing.getStatus()) {
                    case "1006":
                        message = restunfreeze(integralFlowing);
                        break;
                    case "1008":
                        message = restunfreeze(integralFlowing);
                        break;
                    case "1016":
                        message = restunfreeze(integralFlowing);
                        break;
                }
            }
            if(message.equals(IntegralRuleOperation.SUCCESS_CODE)){
                message=IntegralRuleOperation.SUCCESS_MESSAGE;
            }
        return new ResponseExtData(message);
    }
    /**
     * 冻结积分
     * @param integralFlowing
     */
    public String restfreeze(IntegralFlowing integralFlowing){
        Document document;
        String paramXML = "";
        String resultXML = "";
        String returnCode = "";
        String resultMessage = "";
        Map<String,String> paramMap = new HashMap<String,String>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        try{
            //解析xml，获取参数
            paramXML = integralFlowing.getIntegralParame();
            document = DocumentHelper.parseText(paramXML);
            Element elementRoot = document.getRootElement();
            //app_id
            String app_id = elementRoot.element("app_id").getText();
            //模块id
            String service_id = elementRoot.element("service_id").getText();
            //用户id
            String uid = elementRoot.element("uid").getText();
            //订单号
            String out_trade_no = elementRoot.element("out_trade_no").getText();
            //场景编号
            String type = elementRoot.element("type").getText();
            //积分数量
            String credits = elementRoot.element("credits").getText();
            paramMap.put("app_id",app_id);
            paramMap.put("service_id",service_id);
            paramMap.put("uid",uid);
            paramMap.put("out_trade_no",out_trade_no);
            paramMap.put("type",type);
            paramMap.put("credits",credits);
            paramMap.put("timestamp",timestamp);
            String integralparam = IntegralRuleSign.formatMap(paramMap,false,true);
            String strsign = integralparam+"&key="+ Global.getConfig("INTEGRAL.KEY");
            String sign = MD5.MD5Encode(strsign).toUpperCase();
            //获取sign元素，并设置sign的值
            Element element = elementRoot.element("sign");
            element.setText(sign);
            //获取时间戳元素，并重新设置
            Element timeElement = elementRoot.element("timestamp");
            timeElement.setText(timestamp);
            resultXML = HttpUtil.post(integralFlowing.getIntegralUrl(), document.asXML(), IntegralUrl.headerMap);
            Document resultdocument = DocumentHelper.parseText(resultXML);
            Element resultroot = resultdocument.getRootElement();
            returnCode = resultroot.element("return_code").getText().toString();
            resultMessage = resultroot.element("return_msg").getText().toString();
            //如果重发成功，那么就更新该条信息的状态
            if (returnCode.equals("0")) {
                integralFlowing.setTimestamps(timestamp);
                integralFlowing.setSign(sign);
                integralFlowing.setStatus(returnCode);
                integralFlowingService.updateIntegralFlowingStatus(integralFlowing);
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMessage;
    }
    /**
     * 解冻积分
     * @param integralFlowing
     */
    public String restunfreeze(IntegralFlowing integralFlowing){
        Document document;
        String paramXML = "";
        String resultXML = "";
        String returnCode = "";
        String resultMessage = "";
        try{
            paramXML = integralFlowing.getIntegralParame();
            document = DocumentHelper.parseText(paramXML);
            Element elementRoot = document.getRootElement();
            String timestamp = String.valueOf(System.currentTimeMillis());
            Map<String,String> paramMap = new HashMap<String,String>();
            //app_id
            String app_id = elementRoot.element("app_id").getText();
            //模块id
            String service_id = elementRoot.element("service_id").getText();
            //用户id
            String uid = elementRoot.element("uid").getText();
            //订单号
            String out_trade_no = elementRoot.element("out_trade_no").getText();
            //场景编号
            String type = elementRoot.element("type").getText();
            //积分数量
            String credits = elementRoot.element("credits").getText();
            paramMap.put("app_id",app_id);
            paramMap.put("service_id",service_id);
            paramMap.put("uid",uid);
            paramMap.put("out_trade_no",out_trade_no);
            paramMap.put("type",type);
            paramMap.put("credits",credits);
            paramMap.put("timestamp",timestamp);
            String integralparam = IntegralRuleSign.formatMap(paramMap,false,true);
            String strsign = integralparam+"&key="+ Global.getConfig("INTEGRAL.KEY");
            String sign = MD5.MD5Encode(strsign).toUpperCase();
            //获取sign元素，并设置sign的值
            Element element = elementRoot.element("sign");
            element.setText(sign);
            //获取时间戳元素，并重新设置
            Element timeElement = elementRoot.element("timestamp");
            timeElement.setText(timestamp);
            resultXML = HttpUtil.post(integralFlowing.getIntegralUrl(), document.asXML(),IntegralUrl.headerMap);
            Document resultdocument = DocumentHelper.parseText(resultXML);
            Element resultroot = resultdocument.getRootElement();
            returnCode = resultroot.element("return_code").getText().toString();
            resultMessage = resultroot.element("return_msg").getText().toString();
            //如果重发成功，那么就更新该条信息的状态
            if (returnCode.equals("0")) {
                integralFlowing.setTimestamps(timestamp);
                integralFlowing.setSign(sign);
                integralFlowing.setStatus(returnCode);
                integralFlowingService.updateIntegralFlowingStatus(integralFlowing);
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMessage;
        }
    /**
     * 增加积分重发
     * @param integralFlowing
     */
    public String restAddIntegral(IntegralFlowing integralFlowing){
        Document document;
        String paramXML = "";
        String resultXML = "";
        String returnCode = "";
        String resultMessage = "";
        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String,String> paramMap = new HashMap<String,String>();
        try{
            //解析xml，获取参数
            paramXML = integralFlowing.getIntegralParame();
            document = DocumentHelper.parseText(paramXML);
            Element elementRoot = document.getRootElement();
            //app_id
            String app_id = elementRoot.element("app_id").getText();
            //模块id
            String service_id = elementRoot.element("service_id").getText();
            //用户id
            String uid = elementRoot.element("uid").getText();
            //用户手机
            String phone = elementRoot.element("phone").getText();
            //场景类型
            String type = elementRoot.element("type").getText();
            //订单号
            String autoTrandeNo = UUID.randomUUID().toString().replaceAll("-","");
            paramMap.put("app_id",app_id);
            paramMap.put("service_id",service_id);
            paramMap.put("uid",uid);
            paramMap.put("phone",phone);
            paramMap.put("type",type);
            paramMap.put("out_trade_no",autoTrandeNo);
            paramMap.put("timestamp",timestamp);
            if(elementRoot.element("description")!=null){
                String description = elementRoot.element("description").getText();
                paramMap.put("description",description);
            }
            //消息发布字数
            if(elementRoot.element("msg_length")!=null){
                String msg_length = elementRoot.element("msg_length").getText();
                paramMap.put("msg_length",msg_length);
            }
            //消息图片数量
            if(elementRoot.element("pic_num")!=null){
                String pic_num = elementRoot.element("pic_num").getText();
                paramMap.put("pic_num",pic_num);
            }
            //是否是首评
            if(elementRoot.element("first_comment")!=null){
                String first_comment = elementRoot.element("first_comment").getText();
                paramMap.put("first_comment",first_comment);
            }
            //附加数据
            if(elementRoot.element("attach")!=null){
                String attach = elementRoot.element("attach").getText();
                paramMap.put("attach",attach);
            }
            //消费金额
            if(elementRoot.element("purchase_amount")!=null){
                String purchase_amount = elementRoot.element("purchase_amount").getText();
                paramMap.put("purchase_amount",purchase_amount);
            }
            String integralparam = IntegralRuleSign.formatMap(paramMap,false,true);
            String strsign = integralparam+"&key="+ Global.getConfig("INTEGRAL.KEY");
            String sign = MD5.MD5Encode(strsign).toUpperCase();
            //获取sign元素，并设置sign的值
            Element element = elementRoot.element("sign");
            element.setText(sign);
            //获取时间戳元素，并重新设置
            Element timeElement = elementRoot.element("timestamp");
            timeElement.setText(timestamp);
            //获取订单号元素，并重新设置
            Element outtradenoElement = elementRoot.element("out_trade_no");
            outtradenoElement.setText(autoTrandeNo);
            resultXML = HttpUtil.post(integralFlowing.getIntegralUrl(), document.asXML(),IntegralUrl.headerMap);
            Document resultdocument = DocumentHelper.parseText(resultXML);
            Element resultroot = resultdocument.getRootElement();
            returnCode = resultroot.element("return_code").getText().toString();
            resultMessage = resultroot.element("return_msg").getText();
            //如果重发成功，那么就更新该条信息的状态
            if (returnCode.equals("0")) {
                integralFlowing.setTimestamps(timestamp);
                integralFlowing.setSign(sign);
                integralFlowing.setStatus(returnCode);
                integralFlowingService.updateIntegralFlowingStatus(integralFlowing);
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMessage;
    }
    /**
     * 扣除积分重发
     * @param integralFlowing
     */
    public String deductionIntegral(IntegralFlowing integralFlowing){
        Document document;
        String paramXML = "";
        String resultXML = "";
        String returnCode = "";
        String resultMessage = "";
        Map<String,String> paramMap  = new HashMap<String,String>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        try{
            //解析xml，获取参数
            paramXML = integralFlowing.getIntegralParame();
            document = DocumentHelper.parseText(paramXML);
            Element elementRoot = document.getRootElement();
            //app_id
            String app_id = elementRoot.element("app_id").getText();
            //模块id
            String service_id = elementRoot.element("service_id").getText();
            //用户id
            String uid = elementRoot.element("uid").getText();
            //扣除的积分数
            String credits = elementRoot.element("credits").getText();
            //场景类型
            String type = elementRoot.element("type").getText();
            //订单号
            String autoTrandeNo = UUID.randomUUID().toString().replaceAll("-","");
            paramMap.put("app_id",app_id);
            paramMap.put("service_id",service_id);
            paramMap.put("uid",uid);
            paramMap.put("type",type);
            paramMap.put("out_trade_no",autoTrandeNo);
            paramMap.put("timestamp",timestamp);
            paramMap.put("credits",credits);
            if(elementRoot.element("phone")!=null){
                String phone = elementRoot.element("phone").getText();
                paramMap.put("phone",phone);
            }
            if(elementRoot.element("description")!=null){
                String description = elementRoot.element("description").getText();
                paramMap.put("description",description);
            }
            //消息发布字数
            if(elementRoot.element("msg_length")!=null){
                String msg_length = elementRoot.element("msg_length").getText();
                paramMap.put("msg_length",msg_length);
            }
            //消息图片数量
            if(elementRoot.element("pic_num")!=null){
                String pic_num = elementRoot.element("pic_num").getText();
                paramMap.put("pic_num",pic_num);
            }
            //是否是首评
            if(elementRoot.element("first_comment")!=null){
                String first_comment = elementRoot.element("first_comment").getText();
                paramMap.put("first_comment",first_comment);
            }
            //附加数据
            if(elementRoot.element("attach")!=null){
                String attach = elementRoot.element("attach").getText();
                paramMap.put("attach",attach);
            }
            //消费金额
            if(elementRoot.element("purchase_amount")!=null){
                String purchase_amount = elementRoot.element("purchase_amount").getText();
                paramMap.put("purchase_amount",purchase_amount);
            }
            String integralparam = IntegralRuleSign.formatMap(paramMap,false,true);
            String strsign = integralparam+"&key="+ Global.getConfig("INTEGRAL.KEY");
            String sign = MD5.MD5Encode(strsign).toUpperCase();
            //获取sign元素，并设置sign的值
            Element element = elementRoot.element("sign");
            element.setText(sign);
            //获取时间戳元素，并重新设置
            Element timeElement = elementRoot.element("timestamp");
            timeElement.setText(timestamp);
            //获取订单号元素，并重新设置
            Element outtradenoElement = elementRoot.element("out_trade_no");
            outtradenoElement.setText(autoTrandeNo);
            resultXML = HttpUtil.post(integralFlowing.getIntegralUrl(), document.asXML(),IntegralUrl.headerMap);
            Document resultdocument = DocumentHelper.parseText(resultXML);
            Element resultroot = resultdocument.getRootElement();
            returnCode = resultroot.element("return_code").getText().toString();
            resultMessage = resultroot.element("return_msg").getText();
            //如果重发成功，那么就更新该条信息的状态
            if (returnCode.equals("0")) {
                integralFlowing.setTimestamps(timestamp);
                integralFlowing.setSign(sign);
                integralFlowing.setStatus(returnCode);
                integralFlowingService.updateIntegralFlowingStatus(integralFlowing);
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMessage;
    }
}



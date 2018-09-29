package com.cmig.future.csportal.module.integral.controllers;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.integral.dto.IntegralRule;
import com.cmig.future.csportal.module.integral.service.IIntegralRuleService;
import com.hand.hap.core.web.view.IDGenerator;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
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
import java.util.List;

    @Controller
    public class IntegralRuleController extends BaseController{

    @Autowired
    private IIntegralRuleService integralRuleService;


    @RequestMapping(value = "/csp/ljh/integral/rule/query")
    @ResponseBody
    public ResponseData query(IntegralRule dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(integralRuleService.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/ljh/integral/rule/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<IntegralRule> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(integralRuleService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/integral/rule/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<IntegralRule> dto){
        integralRuleService.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/ljh/integral/rule/save")
    @ResponseBody
    public ResponseData save(HttpServletRequest request,@RequestBody IntegralRule dto){
        //integralRuleService.batchDelete(dto);
        //保存积分规则
        integralRuleService.save(dto);
        return new ResponseData();
    }

    /**
     * 根据主键获取对象
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/integral/rule/get")
    @ResponseBody
    public ResponseExtData get(HttpServletRequest request){
        //integralRuleService.batchDelete(dto);
        //保存积分规则
        IntegralRule integralRule = new IntegralRule();
        String ruleId = request.getParameter("id");
        if(!StringUtils.isEmpty(ruleId)){
            integralRule.setRuleId(Long.parseLong(ruleId));
        }
        integralRule = integralRuleService.getIntegralRuleById(integralRule);
        if(integralRule==null){
            IntegralRule dto = new IntegralRule();
            String ruleCode = IdGen.uuid();
            System.out.println(ruleCode);
            dto.setRuleCode(ruleCode);
            return new ResponseExtData(dto);
        }
        return new ResponseExtData(integralRule);
       }

        /**
         * 停用该积分规则状态
         * @param request
         * @return
         */
        @RequestMapping(value = "/csp/ljh/integral/rule/stopRule")
        @ResponseBody
        public ResponseData stopRule(HttpServletRequest request){

            String id = request.getParameter("id");
            IntegralRule integralRule = new IntegralRule();
            integralRule.setRuleId(Long.parseLong(id));
            integralRule.setStatus(Constants.INTEGRAL_RULE_DISABLED);
            integralRuleService.stopRuleStatus(integralRule);
            //integralRuleService.batchDelete(dto);
            //保存积分规则
           // integralRuleService.save(dto);
            return new ResponseData();
        }

        /**
         * 开启该积分规则状态
         * @param request
         * @return
         */
        @RequestMapping(value = "/csp/ljh/integral/rule/startRule")
        @ResponseBody
        public ResponseData startRule(HttpServletRequest request){

            String id = request.getParameter("id");
            IntegralRule integralRule = new IntegralRule();
            integralRule.setRuleId(Long.parseLong(id));
            System.out.println(Constants.INTEGRAL_RULE_ENABLED);
            integralRule.setStatus(Constants.INTEGRAL_RULE_ENABLED);
            integralRuleService.startRuleStatus(integralRule);
            //integralRuleService.batchDelete(dto);
            //保存积分规则
            // integralRuleService.save(dto);
            return new ResponseData();
        }

        /**
         * 更新积分规则
         * @param request
         * @param dto
         * @return
         */
        @RequestMapping(value = "/csp/ljh/integral/rule/update")
        @ResponseBody
        public ResponseData update(HttpServletRequest request,@RequestBody IntegralRule dto){
            //integralRuleService.batchDelete(dto);
            //更新积分规则
            integralRuleService.updateIntgralRule(dto);
            return new ResponseData();
        }
}
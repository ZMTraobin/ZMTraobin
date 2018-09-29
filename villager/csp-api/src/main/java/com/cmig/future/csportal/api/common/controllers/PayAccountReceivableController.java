package com.cmig.future.csportal.api.common.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.pay.account.constants.PayConstants;
import com.cmig.future.csportal.module.pay.account.dto.AccountReceivable;
import com.cmig.future.csportal.module.pay.account.service.IAccountReceivableService;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:25 2017/4/6.
 * @Modified by zhangtao on 14:25 2017/4/6.
 */
@Controller
@RequestMapping(value = "${userPath}/pay/accountReceivable/")
@ResponseBody
public class PayAccountReceivableController extends BaseExtendController {

    @Autowired
    private IAccountReceivableService accountReceivableService;

    @Autowired
    private IBaseCommunityService baseCommunityService;

    /**
     * 根据费项-项目，获取开通的支付渠道
     * @param dto
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "list", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp test1(@ModelAttribute AccountReceivable dto, HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        try {
            if(StringUtils.isEmpty(dto.getExpenditure())){
                throw new DataWarnningException("费项不能为空");
            }

            BaseCommunity baseCommunity=new BaseCommunity();
            if(PayConstants.PAY_EXPENDITURE_PROPERTY_FEE.equals(dto.getExpenditure())&&StringUtils.isEmpty(dto.getCommunityId())){
                throw new DataWarnningException("物业费所属项目id不能为空");
            }else{
                baseCommunity=baseCommunityService.get(dto.getCommunityId());
                if(null==baseCommunity){
                    throw new DataWarnningException("项目id错误");
                }
            }

            AccountReceivable accountReceivable=new AccountReceivable();
            accountReceivable.setExpenditure(dto.getExpenditure());
            accountReceivable.setCompanyId(baseCommunity.getCompanyId());
            accountReceivable.setCommunityId(baseCommunity.getId());
            accountReceivable.setEnableFlag(Constants.YES);
            List<AccountReceivable> list=accountReceivableService.findList(accountReceivable);
            JSONObject jsonObject=new JSONObject();
            if(!StringUtils.isEmpty(list)){
                AccountReceivable entry=list.get(0);
                jsonObject.put("id",entry.getId());
                jsonObject.put("merchantNumber",entry.getMerchantNumber());
            }

            retApp.setData(jsonObject);
            retApp.setStatus(OK);
            retApp.setMessage("查询成功");
        }catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (DataWarnningException e) {
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("查询失败");
        }
        return retApp;
    }
}

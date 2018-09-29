package com.cmig.future.csportal.api.open.mgt.payment.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouse;
import com.cmig.future.csportal.module.properties.base.house.service.IMgtHouseService;
import com.cmig.future.csportal.module.properties.base.utils.MgtStructureHelper;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.cmig.future.csportal.module.properties.payment.receivable.service.IMgtReceivableDetailService;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.github.pagehelper.Page;
import com.hand.hap.core.IRequest;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${commonPath}")
@ResponseBody
public class MgtReceivableController extends BaseExtendController {

    @Autowired
    private IMgtReceivableDetailService mgtReceivableDetailService;
    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private IMgtHouseService mgtHouseService;

    /**
     * 应收明细-新增
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/receivable/add", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp add(HttpServletRequest request, HttpServletResponse response) {
        //必填项,需要验证
        String appid = getParam(request, "appid", "");
        String sourceReceivableId = getParam(request, "sourceReceivableId", "");
        String sourceBuildCode = getParam(request, "sourceBuildCode", "");
        String expenditure = getParam(request, "expenditure", "");
        String periodName = getParam(request, "periodName", "");
        String totalAmount = getParam(request, "totalAmount", "");
        String backUrl = getParam(request, "backUrl", "");
        String sign = getParam(request, "sign", "");
        String buildingType = getParam(request, "buildingType", "");

        //可为空参数
        String discountAmount = getParam(request, "discountAmount", "");
        String breakContractAmount = getParam(request, "breakContractAmount", "");
        String description = getParam(request, "description", "");
        RetApp retApp = new RetApp();
        try {
            if (StringUtils.isEmpty(appid)) {
                throw new DataWarnningException("appid不能为空");
            }

	        //验证第三方签名
	        OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
	        Map<String, String> map = new HashMap();
	        map.put("appid", appid);
	        map.put("sourceReceivableId", sourceReceivableId);
	        map.put("sourceBuildCode", sourceBuildCode);
	        map.put("expenditure", expenditure);
	        map.put("periodName", periodName);
	        map.put("totalAmount", totalAmount);
	        if (!StringUtils.isEmpty(discountAmount)) {
		        map.put("discountAmount", discountAmount);
	        }
	        if (!StringUtils.isEmpty(breakContractAmount)) {
		        map.put("breakContractAmount", breakContractAmount);
	        }
	        map.put("backUrl", backUrl);
	        if (!StringUtils.isEmpty(description)) {
		        map.put("description", description);
	        }
	        map.put("buildingType", buildingType);

	        MgtReceivableDetail mgtReceivableDetail = new MgtReceivableDetail();
	        if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
		        throw new DataWarnningException("签名验证未通过");
	        }

	        if(StringUtils.isEmpty(discountAmount)){
		        discountAmount="0";
	        }
	        if(StringUtils.isEmpty(breakContractAmount)){
		        breakContractAmount="0";
	        }
            if (StringUtils.isEmpty(sourceReceivableId)) {
                throw new DataWarnningException("sourceReceivableId不能为空");
            }
            if (StringUtils.isEmpty(sourceBuildCode)) {
                throw new DataWarnningException("建筑原始编码不能为空");
            }

            if (StringUtils.isEmpty(expenditure)) {
                throw new DataWarnningException("费项不能为空");
            }else if (!CodeUtil.checkDicValueExists(Constants.CODE_MGT_EXPENDITURE_TYPE,expenditure)) {
                throw new DataWarnningException("不正确的费项类型！");
            }
            if (StringUtils.isEmpty(periodName)) {
                throw new DataWarnningException("期间不能为空");
            } else if (!RegExpValidatorUtils.checkPeriod(periodName)) {
                throw new DataWarnningException("期间格式不正确");
            }
            if (StringUtils.isEmpty(totalAmount)) {
                throw new DataWarnningException("总金额不能为空");
            } else if (!RegExpValidatorUtils.isIntNumberAndZero(totalAmount)) {
                throw new DataWarnningException("总金额格式不正确");
            }
            if (!StringUtils.isEmpty(discountAmount)) {
                Long diff = Long.valueOf(totalAmount) - Long.valueOf(discountAmount);
                if (diff < 0){
                    throw new DataWarnningException("折扣金额不能大于总金额");
                }
            }
            if (!RegExpValidatorUtils.isIntNumberAndZero(discountAmount)) {
                throw new DataWarnningException("折扣金额格式不正确");
            }
            if (!RegExpValidatorUtils.isIntNumberAndZero(breakContractAmount)) {
                throw new DataWarnningException("违约金格式不正确");
            }
            if (StringUtils.isEmpty(backUrl)) {
                throw new DataWarnningException("服务端通知地址不能为空");
            }
           /* if (!RegExpValidatorUtils.checkURL(backUrl)) {
                throw new DataWarnningException("服务端通知地址格式不正确");
            }*/
            if (StringUtils.isEmpty(sign)) {
                throw new DataWarnningException("签名信息不能为空");
            }
            if (description.length() > 255) {
                throw new DataWarnningException("描述不能超过255个字符");
            }
            if (StringUtils.isEmpty(buildingType)){
                throw new DataWarnningException("建筑实体类别不能为空");
            }else if (!CodeUtil.checkDicValueExists(Constants.CODE_MGT_BUILDING_TYPE,buildingType)) {
                throw new DataWarnningException("不正确的建筑实体类型！");
            }

            Long buildingId = null;
            //唯一性校验
            MgtReceivableDetail mgtReceivableDetailQuery = new MgtReceivableDetail();
            mgtReceivableDetailQuery.setSourceSystem(openAppInfo.getAppName());
            mgtReceivableDetailQuery.setSourceReceivableId(sourceReceivableId);
            MgtReceivableDetail dto = mgtReceivableDetailService.findBySourceReceivableIdAndSourceSystem(mgtReceivableDetailQuery);
            if (!StringUtils.isEmpty(dto)){
                throw new DataWarnningException("该应用下源系统应收ID已存在,请勿重复添加!");
            }

            //根据类型查询关联数据
            if (buildingType.equals(MgtStructureHelper.HOUSE)){//房屋
                MgtHouse mgtHouseQuery = new MgtHouse();
                mgtHouseQuery.setSourceSystem(openAppInfo.getAppName());
                mgtHouseQuery.setSourceHouseCode(sourceBuildCode);
                //查询建筑实体
                MgtHouse mgtHouse = mgtHouseService.findBySourceHouseCodeAndSourceSystem(mgtHouseQuery);
                if (StringUtils.isEmpty(mgtHouse)){
                    throw new DataWarnningException("该项目下不存在该建筑实体!");
                }else{
                    buildingId = mgtHouse.getHouseId();
                }
            }else if (buildingType.equals(MgtStructureHelper.PARKING)){//车位

            }else if (buildingType.equals(MgtStructureHelper.SHOP)){//商铺

            }
            if (!StringUtils.isEmpty(buildingId)){
                //新增
                mgtReceivableDetail.setSourceReceivableId(sourceReceivableId);
                mgtReceivableDetail.setBuildingType(buildingType);
                mgtReceivableDetail.setBuildingId(buildingId);
                mgtReceivableDetail.setExpenditure(expenditure);
                mgtReceivableDetail.setPeriodName(periodName);
                mgtReceivableDetail.setTotalAmount(Long.parseLong(totalAmount));
                mgtReceivableDetail.setDiscountAmount(Long.parseLong(discountAmount));
                mgtReceivableDetail.setBreakContractAmount(Long.parseLong(breakContractAmount));
	            //应收总额=总金额-抵扣金额+违约金
                mgtReceivableDetail.setReceivableAmount(mgtReceivableDetail.getTotalAmount()-mgtReceivableDetail.getDiscountAmount()+mgtReceivableDetail.getBreakContractAmount());
                mgtReceivableDetail.setBackUrl(backUrl);
                if (!StringUtils.isEmpty(description)) {
                    mgtReceivableDetail.setDescription(description);
                }
                mgtReceivableDetail.setAppId(appid);
                mgtReceivableDetail.setSourceSystem(openAppInfo.getAppName());
                mgtReceivableDetailService.save(mgtReceivableDetail);
            }else{
                throw new DataWarnningException("该项目下不存在该建筑实体!");
            }
            retApp.setStatus(OK);
            retApp.setMessage("新增成功!");
        }catch (DataWarnningException e) {
            String msg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(msg);
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (Exception e) {
            retApp.setStatus(FAIL);
            retApp.setMessage("新增失败.");
        }
        return retApp;
    }

    /**
     * 应收明细-更新
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/receivable/update", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp update(HttpServletRequest request, HttpServletResponse response) {
        //必填项,需要验证
        String appid = getParam(request, "appid", "");
        String sourceReceivableId = getParam(request, "sourceReceivableId", "");
        String periodName = getParam(request, "periodName", "");
        String totalAmount = getParam(request, "totalAmount", "");
        String backUrl = getParam(request, "backUrl", "");
        String sign = getParam(request, "sign", "");

        //可为空参数
        String discountAmount = getParam(request, "discountAmount", "");
        String breakContractAmount = getParam(request, "breakContractAmount", "");
        String description = getParam(request, "description", "");
        RetApp retApp = new RetApp();
        try {
            if (StringUtils.isEmpty(appid)) {
                throw new DataWarnningException("appid不能为空");
            }
            if (StringUtils.isEmpty(sourceReceivableId)) {
                throw new DataWarnningException("sourceReceivableId不能为空");
            }
            if (!StringUtils.isEmpty(periodName)&&!RegExpValidatorUtils.checkPeriod(periodName)) {
                throw new DataWarnningException("期间格式不正确");
            }
            if (!StringUtils.isEmpty(totalAmount)&&!RegExpValidatorUtils.isIntNumberAndZero(totalAmount)) {
                throw new DataWarnningException("总金额格式不正确");
            }
            if (!StringUtils.isEmpty(discountAmount)&&!RegExpValidatorUtils.isIntNumberAndZero(discountAmount)) {
                throw new DataWarnningException("折扣金额格式不正确");
            }
            if (!StringUtils.isEmpty(breakContractAmount)&&!RegExpValidatorUtils.isIntNumberAndZero(breakContractAmount)) {
                throw new DataWarnningException("违约金格式不正确");
            }
            /*if (!RegExpValidatorUtils.checkURL(backUrl)) {
                throw new DataWarnningException("服务端通知地址格式不正确");
            }*/
            if (StringUtils.isEmpty(sign)) {
                throw new DataWarnningException("签名信息不能为空");
            }
            if (description.length() > 255) {
                throw new DataWarnningException("描述不能超过255个字符");
            }

            //验证第三方签名
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
            Map<String, String> map = new HashMap();
            map.put("appid", appid);
            map.put("sourceReceivableId", sourceReceivableId);
            if (!StringUtils.isEmpty(periodName)) {
                map.put("periodName", periodName);
            }
            if (!StringUtils.isEmpty(totalAmount)) {
                map.put("totalAmount", totalAmount);
            }
            if (!StringUtils.isEmpty(discountAmount)) {
                map.put("discountAmount", discountAmount);
            }
            if (!StringUtils.isEmpty(breakContractAmount)) {
                map.put("breakContractAmount", breakContractAmount);
            }
            if (!StringUtils.isEmpty(backUrl)) {
                map.put("backUrl", backUrl);
            }
            if (!StringUtils.isEmpty(description)) {
                map.put("description", description);
            }

            if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
                throw new DataWarnningException("签名验证未通过");
            } else {
                //查询明细
                MgtReceivableDetail mgtReceivableDetailQuery = new MgtReceivableDetail();
                mgtReceivableDetailQuery.setSourceSystem(openAppInfo.getAppName());
                mgtReceivableDetailQuery.setSourceReceivableId(sourceReceivableId);
	            MgtReceivableDetail mgtReceivableDetail = mgtReceivableDetailService.findBySourceReceivableIdAndSourceSystem(mgtReceivableDetailQuery);

                if (!StringUtils.isEmpty(mgtReceivableDetail)){

	                if(!mgtReceivableDetail.getPayStatus().equals(OrderFormHelper.OREDER_STATUS_N)){
						throw new DataWarnningException("该应收明细已完成支付，不能修改。");
	                }

                    //更新
                    if (!StringUtils.isEmpty(periodName)) {
                        mgtReceivableDetail.setPeriodName(periodName);
                    }
	                if (!StringUtils.isEmpty(backUrl)) {
		                mgtReceivableDetail.setBackUrl(backUrl);
	                }
	                if (!StringUtils.isEmpty(description)) {
		                mgtReceivableDetail.setDescription(description);
	                }
	                if (!StringUtils.isEmpty(totalAmount)) {
		                mgtReceivableDetail.setTotalAmount(Long.parseLong(totalAmount));
	                }
	                if (!StringUtils.isEmpty(discountAmount)) {
		                mgtReceivableDetail.setDiscountAmount(Long.parseLong(discountAmount));
	                }
	                if (!StringUtils.isEmpty(breakContractAmount)) {
		                mgtReceivableDetail.setBreakContractAmount(Long.parseLong(breakContractAmount));
	                }

	                Long diff = mgtReceivableDetail.getTotalAmount() - mgtReceivableDetail.getDiscountAmount();
	                if (diff < 0){
		                throw new DataWarnningException("折扣金额不能大于总金额");
	                }

	                //应收总额=总金额-抵扣金额+违约金
	                mgtReceivableDetail.setReceivableAmount(mgtReceivableDetail.getTotalAmount()-mgtReceivableDetail.getDiscountAmount()+mgtReceivableDetail.getBreakContractAmount());
	                mgtReceivableDetailService.save(mgtReceivableDetail);
                }else{
                    throw new DataWarnningException("该应用下不存在这个源系统应收ID!");
                }
            }
            retApp.setStatus(OK);
            retApp.setMessage("更新成功!");
        }catch (DataWarnningException e) {
            String msg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(msg);
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (Exception e) {
            retApp.setStatus(FAIL);
            retApp.setMessage("更新失败.");
        }
        return retApp;
    }

    /**
     * 应收明细-失效
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/receivable/unable", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp unable(HttpServletRequest request, HttpServletResponse response){
        //必填项,需要验证
        String appid = getParam(request, "appid", "");
        String sourceReceivableId = getParam(request, "sourceReceivableId", "");
        String sign = getParam(request, "sign", "");
        RetApp retApp = new RetApp();
        try {
            if (StringUtils.isEmpty(appid)) {
                throw new DataWarnningException("appid不能为空");
            }
            if (StringUtils.isEmpty(sourceReceivableId)) {
                throw new DataWarnningException("sourceReceivableId不能为空");
            }
            if (StringUtils.isEmpty(sign)) {
                throw new DataWarnningException("签名信息不能为空");
            }
            //验证第三方签名
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
            Map<String, String> map = new HashMap();
            map.put("appid", appid);
            map.put("sourceReceivableId", sourceReceivableId);
            MgtReceivableDetail mgtReceivableDetail = new MgtReceivableDetail();
            if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
                throw new DataWarnningException("签名验证未通过");
            } else {
                //查询明细
                MgtReceivableDetail mgtReceivableDetailQuery = new MgtReceivableDetail();
                mgtReceivableDetailQuery.setSourceSystem(openAppInfo.getAppName());
                mgtReceivableDetailQuery.setSourceReceivableId(sourceReceivableId);
                mgtReceivableDetail = mgtReceivableDetailService.findBySourceReceivableIdAndSourceSystem(mgtReceivableDetailQuery);

                if (!StringUtils.isEmpty(mgtReceivableDetail)) {
	                if(!mgtReceivableDetail.getPayStatus().equals(OrderFormHelper.OREDER_STATUS_N)){
		                throw new DataWarnningException("该应收明细已完成支付，不能失效");
	                }
                    //失效
                    mgtReceivableDetail.setEnableFlag(BaseExtDTO.STATUS_UNABLE);
                    mgtReceivableDetailService.save(mgtReceivableDetail);
                }else{
                    throw new DataWarnningException("该应用下不存在这个源系统应收ID");
                }
            }

            retApp.setStatus(OK);
            retApp.setMessage("失效成功");
        }catch (DataWarnningException e) {
            String msg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(msg);
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (Exception e) {
            retApp.setStatus(FAIL);
            retApp.setMessage("失效失败");
        }
        return retApp;
    }

    /**
     * 应收明细-账单状态
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/receivable/queryStatus", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp queryStatus(HttpServletRequest request, HttpServletResponse response){
        //必填项,需要验证
        String appid = getParam(request, "appid", "");
        String sourceReceivableId = getParam(request, "sourceReceivableId", "");
        String sign = getParam(request, "sign", "");
        RetApp retApp = new RetApp();
        try {
            if (StringUtils.isEmpty(appid)) {
                throw new DataWarnningException("appid不能为空");
            }
            if (StringUtils.isEmpty(sourceReceivableId)) {
                throw new DataWarnningException("sourceReceivableId不能为空");
            }
            if (StringUtils.isEmpty(sign)) {
                throw new DataWarnningException("签名信息不能为空");
            }
            //验证第三方签名
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
            Map<String, String> map = new HashMap();
            map.put("appid", appid);
            map.put("sourceReceivableId", sourceReceivableId);
            MgtReceivableDetail mgtReceivableDetail = new MgtReceivableDetail();
            if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
                throw new DataWarnningException("签名验证未通过");
            } else {
                //查询明细
                MgtReceivableDetail mgtReceivableDetailQuery = new MgtReceivableDetail();
                mgtReceivableDetailQuery.setSourceSystem(openAppInfo.getAppName());
                mgtReceivableDetailQuery.setSourceReceivableId(sourceReceivableId);
                mgtReceivableDetail = mgtReceivableDetailService.findBySourceReceivableIdAndSourceSystem(mgtReceivableDetailQuery);

                if (StringUtils.isEmpty(mgtReceivableDetail)) {
                    throw new DataWarnningException("该应用下不存在这个源系统应收ID!");
                }
            }
            JSONObject object = new JSONObject();
            object.put("sourceReceivableId", mgtReceivableDetail.getSourceReceivableId());
	        object.put("backUrl", mgtReceivableDetail.getBackUrl());
            object.put("checkFlag", mgtReceivableDetail.getCheckFlag());
            object.put("payStatus", mgtReceivableDetail.getPayStatus());
            object.put("enableFlag", mgtReceivableDetail.getEnableFlag());
            object.put("paidTime", mgtReceivableDetail.getPaidTime());
            object.put("expenditure", mgtReceivableDetail.getExpenditure());
	        object.put("totalAmount", mgtReceivableDetail.getTotalAmount());
	        object.put("discountAmount", mgtReceivableDetail.getDiscountAmount());
	        object.put("breakContractAmount", mgtReceivableDetail.getBreakContractAmount());
	        object.put("reveivableAmount", mgtReceivableDetail.getReceivableAmount());
	        object.put("paidTime", mgtReceivableDetail.getPaidTime());

            //渠道:PAY_CHANNEL & TRANSEQ

            retApp.setData(object);
            retApp.setStatus(OK);
            retApp.setMessage("查询成功!");
        }catch (DataWarnningException e) {
            String msg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(msg);
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (Exception e) {
            retApp.setStatus(FAIL);
            retApp.setMessage("查询失败.");
        }
        return retApp;
    }

    /**
     * 应收明细-分页查询
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/receivable/list", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp list(HttpServletRequest request, HttpServletResponse response){
        //必填项,需要验证
        String appid = getParam(request, "appid", "");
        String pageNo = getParam(request, "pageNo", "");
        String pageSize = getParam(request, "pageSize", "");
        String sign = getParam(request, "sign", "");

        RetApp retApp = new RetApp();
        try {
            if (StringUtils.isEmpty(appid)) {
                throw new DataWarnningException("appid不能为空");
            }
            if (StringUtils.isEmpty(pageNo)) {
                throw new DataWarnningException("pageNo不能为空");
            }
            if (StringUtils.isEmpty(pageSize)) {
                throw new DataWarnningException("pageSize不能为空");
            }
            if (StringUtils.isEmpty(sign)) {
                throw new DataWarnningException("签名信息不能为空");
            }
            //验证第三方签名
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
            Map<String, String> map = new HashMap();
            map.put("appid", appid);
            map.put("pageNo", pageNo);
            map.put("pageSize", pageSize);
            MgtReceivableDetail mgtReceivableDetail = new MgtReceivableDetail();

            if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
                throw new DataWarnningException("签名验证未通过");
            } else {
                Map<String,Object> jsonObject = new HashMap<String,Object>();
                List<Map<String,Object>> array = new ArrayList<>();
                //查询
                IRequest requestContext = createRequestContext(request);

	            mgtReceivableDetail.setAppId(appid);
                List<MgtReceivableDetail> list = mgtReceivableDetailService.select(null,mgtReceivableDetail,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
                for (MgtReceivableDetail dto : list){
                    Map<String,Object> obj = new HashMap<String,Object>();
                    obj.put("reveivableId", dto.getReceivableId());
                    obj.put("buildingId", dto.getBuildingId());
                    obj.put("buildingType", dto.getBuildingType());
                    obj.put("expenditure", dto.getExpenditure());
                    obj.put("periodName", dto.getPeriodName());
                    obj.put("priceAmout", dto.getPriceAmout());
                    obj.put("area", dto.getArea());
	                obj.put("totalAmount", dto.getTotalAmount());
                    obj.put("discountAmount", dto.getDiscountAmount());
                    obj.put("breakContractAmount", dto.getBreakContractAmount());
                    obj.put("reveivableAmount", dto.getReceivableAmount());
                    obj.put("buildTime", dto.getBuildTime());
                    obj.put("paidTime", dto.getPaidTime());
                    obj.put("backUrl", dto.getBackUrl());
                    obj.put("description", dto.getDescription());
                    obj.put("notifyFlag", dto.getNotifyFlag());
                    obj.put("checkFlag", dto.getCheckFlag());
                    obj.put("payStatus", dto.getPayStatus());
                    obj.put("enableFlag", dto.getEnableFlag());
                    obj.put("sourceReceivableId", dto.getSourceReceivableId());
                    array.add(obj);
                }
                jsonObject.put("receivableList", array);
                retApp.setData(jsonObject);
                retApp.setTotall(Long.valueOf(((Page)list).getTotal()));
            }
            retApp.setStatus(OK);
            retApp.setMessage("查询成功!");
        }catch (DataWarnningException e) {
            String msg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(msg);
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (Exception e) {
            retApp.setStatus(FAIL);
            retApp.setMessage("查询失败.");
        }
        return retApp;
    }

}
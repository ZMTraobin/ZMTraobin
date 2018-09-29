package com.cmig.future.csportal.module.properties.payment.receivable.service.impl;

import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.excel.ExcelError;
import com.cmig.future.csportal.common.utils.excel.ExcelUtil;
import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.common.utils.excel.annotation.ExcelField;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.pay.order.dto.AppOrderVo;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.dto.OrderFormLine;
import com.cmig.future.csportal.module.pay.order.mapper.OrderFormLineMapper;
import com.cmig.future.csportal.module.pay.order.mapper.OrderFormMapper;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouse;
import com.cmig.future.csportal.module.properties.base.house.service.IMgtHouseService;
import com.cmig.future.csportal.module.properties.base.utils.MgtStructureHelper;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableImport;
import com.cmig.future.csportal.module.properties.payment.receivable.mapper.MgtReceivableDetailMapper;
import com.cmig.future.csportal.module.properties.payment.receivable.service.IMgtReceivableDetailService;
import com.cmig.future.csportal.module.sys.openinfo.OpenAppUtils;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional
public class MgtReceivableDetailServiceImpl extends BaseServiceImpl<MgtReceivableDetail> implements IMgtReceivableDetailService {

    @Autowired
    private MgtReceivableDetailMapper mgtReceivableDetailMapper;
    @Autowired
    private IMgtHouseService mgtHouseService;

	@Autowired
	private IOrderFormService orderFormService;

	@Autowired
	private IAppUserService appUserService;

	@Autowired
	private OrderFormLineMapper orderFormLineMapper;

	@Autowired
	private OrderFormMapper orderFormMapper;

    private static Logger logger = LoggerFactory.getLogger(MgtReceivableDetailServiceImpl.class);

	//创建一个可重用固定线程数的线程池
	static ExecutorService pool = Executors.newFixedThreadPool(5);

    @Override
    public void save(MgtReceivableDetail dto){
        if (!StringUtils.isEmpty(dto.getReceivableId())) {
            mgtReceivableDetailMapper.updateByPrimaryKeySelective(dto);
        } else {
            super.mapper.insertSelective(dto);
        }
    }

    @Override
    public MgtReceivableDetail findBySourceReceivableIdAndSourceSystem(MgtReceivableDetail dto){
        return mgtReceivableDetailMapper.findBySourceReceivableIdAndSourceSystem(dto);
    }

    @Override
    public void validationImportData(ImportExcel ei, List<MgtReceivableImport> list) {
        logger.debug("校验导入数据开始......");
        // 校验数据重复
        ExcelUtil.duplicateMutiColumsValidator(ei.getSheetName(),ei.getDataRowNum(), list, ei.getExcelErrorList(), new String[] {"sourceSystem","sourceBuildCode","expenditure","periodName"}, MgtReceivableImport.class);
        //校验数据格式
        for (int i = 0; i < list.size(); i++) {
            MgtReceivableImport mgtReceivableImport =  list.get(i);
            String periodName = mgtReceivableImport.getPeriodName();//期间
            String totalAmount = mgtReceivableImport.getTotalAmount().toString();//总金额
            String discountAmount = mgtReceivableImport.getDiscountAmount()==null?"0":mgtReceivableImport.getDiscountAmount().toString();//折扣金额
            String breakContractAmount = mgtReceivableImport.getBreakContractAmount()==null?"0":mgtReceivableImport.getBreakContractAmount().toString();//违约金
            String buildingType = mgtReceivableImport.getBuildingType();//实体类型
            String sourceSystem = mgtReceivableImport.getSourceSystem();//应用标识
            String sourceBuildCode = mgtReceivableImport.getSourceBuildCode();//原建筑编码
            //String sourceReceivableId = mgtReceivableImport.getSourceReceivableId();//源系统应收ID
            Long buildingId = null;
            if (!RegExpValidatorUtils.checkPeriod(periodName)) {
                ExcelField ef = ExcelUtil.getExcelField(mgtReceivableImport, "periodName");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "期间格式不正确。");
                ei.getExcelErrorList().add(error);
            }
            if (!RegExpValidatorUtils.IsNumber(totalAmount)) {
                ExcelField ef = ExcelUtil.getExcelField(mgtReceivableImport, "totalAmount");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "总金额格式不正确。");
                ei.getExcelErrorList().add(error);
            }
            if(!StringUtils.isEmpty(discountAmount)){
                if (!RegExpValidatorUtils.IsNumber(discountAmount)) {
                    ExcelField ef = ExcelUtil.getExcelField(mgtReceivableImport, "discountAmount");
                    ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "折扣金额格式不正确。");
                    ei.getExcelErrorList().add(error);
                }else{
                    Long receivableAmount = Long.parseLong(totalAmount) - Long.parseLong(discountAmount);
                    if(receivableAmount < 0){
                        ExcelField ef = ExcelUtil.getExcelField(mgtReceivableImport, "discountAmount");
                        ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "折扣金额不能大于总金额。");
                        ei.getExcelErrorList().add(error);
                    }else{
                        mgtReceivableImport.setReceivableAmount(receivableAmount);
                    }
                }
            }else{
                mgtReceivableImport.setReceivableAmount(Long.parseLong(totalAmount));
            }
            if(!StringUtils.isEmpty(breakContractAmount)){
                if (!RegExpValidatorUtils.IsNumber(breakContractAmount)) {
                    ExcelField ef = ExcelUtil.getExcelField(mgtReceivableImport, "breakContractAmount");
                    ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "违约金格式不正确。");
                    ei.getExcelErrorList().add(error);
                }else{
	                if(null==mgtReceivableImport.getReceivableAmount()){
		                mgtReceivableImport.setReceivableAmount(new Long(0));
	                }
	                mgtReceivableImport.setReceivableAmount(mgtReceivableImport.getReceivableAmount()+Long.parseLong(breakContractAmount));
                }
            }
	        if(buildingType.equals(MgtStructureHelper.HOUSE)){//房屋
		        MgtHouse mgtHouseQuery = new MgtHouse();
		        mgtHouseQuery.setSourceSystem(sourceSystem);
		        mgtHouseQuery.setSourceHouseCode(sourceBuildCode);
		        //查询建筑实体
		        MgtHouse mgtHouse = mgtHouseService.findBySourceHouseCodeAndSourceSystem(mgtHouseQuery);
		        if (StringUtils.isEmpty(mgtHouse)){
			        ExcelField ef = ExcelUtil.getExcelField(mgtReceivableImport, "sourceBuildCode");
			        ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "不存在该建筑实体。");
			        ei.getExcelErrorList().add(error);
		        }else{
			        buildingId = mgtHouse.getHouseId();
			        mgtReceivableImport.setBuildingId(buildingId);
			        mgtReceivableImport.setPaymentArea(mgtHouse.getPaymentArea());
		        }
	        }else if (buildingType.equals(MgtStructureHelper.PARKING)){//车位

	        }else if (buildingType.equals(MgtStructureHelper.SHOP)){//商铺

	        }
        }
        logger.debug("校验导入数据结束......");
    }

    @Override
    @Transactional(readOnly = false)
    public void saveImportDate(List<MgtReceivableImport> list){
        logger.debug("导入数据持久化开始......");
        for (int i = 0; i < list.size(); i++) {
            MgtReceivableImport mgtReceivableImport =  list.get(i);
            String sourceSystem = mgtReceivableImport.getSourceSystem();
            String sourceReceivableId = mgtReceivableImport.getSourceReceivableId();
            //String sourceBuildCode = mgtReceivableImport.getSourceBuildCode();
            String expenditure = mgtReceivableImport.getExpenditure();
            String periodName = mgtReceivableImport.getPeriodName();
            Long totalAmount = mgtReceivableImport.getTotalAmount();
            Long discountAmount = mgtReceivableImport.getDiscountAmount();
            Long breakContractAmount = mgtReceivableImport.getBreakContractAmount();
            String backUrl = mgtReceivableImport.getBackUrl();
            String description = mgtReceivableImport.getDescription();
            String buildingType = mgtReceivableImport.getBuildingType();
            Long receivableAmount = mgtReceivableImport.getReceivableAmount();
            Long buildingId = mgtReceivableImport.getBuildingId();
            //新增
            MgtReceivableDetail mgtReceivableDetail = new MgtReceivableDetail();
	        if(!StringUtils.isEmpty(sourceReceivableId)) {
		        mgtReceivableDetail.setSourceReceivableId(sourceReceivableId);
	        }else{
		        mgtReceivableDetail.setSourceReceivableId(IdGen.uuid());
	        }
            mgtReceivableDetail.setBuildingType(buildingType);
            mgtReceivableDetail.setBuildingId(buildingId);
            mgtReceivableDetail.setExpenditure(expenditure);
            mgtReceivableDetail.setPeriodName(periodName);
            mgtReceivableDetail.setTotalAmount(totalAmount);
            if (!StringUtils.isEmpty(discountAmount)) {
                mgtReceivableDetail.setDiscountAmount(discountAmount);
            }
            if (!StringUtils.isEmpty(breakContractAmount)) {
                mgtReceivableDetail.setBreakContractAmount(breakContractAmount);
            }
            mgtReceivableDetail.setReceivableAmount(receivableAmount);
            if (!StringUtils.isEmpty(backUrl)) {
                mgtReceivableDetail.setBackUrl(backUrl);
            }
            if (!StringUtils.isEmpty(description)) {
                mgtReceivableDetail.setDescription(description);
            }
	        if(null!=mgtReceivableImport.getPaymentArea()){
		        mgtReceivableDetail.setArea(mgtReceivableImport.getPaymentArea());
	        }
            mgtReceivableDetail.setSourceSystem(sourceSystem);

            mgtReceivableDetailMapper.insertSelective(mgtReceivableDetail);
        }
        logger.debug("导入数据持久化结束......共导入:"+list.size()+" 条数据");
    }

	@Override
	public List<MgtReceivableDetail> queryMgtReceivableDetail(IRequest requestContext,MgtReceivableDetail dto,int page,
            int pageSize) {
		 PageHelper.startPage(page, pageSize);
		return mgtReceivableDetailMapper.queryMgtReceivableDetail(dto);
	}

    @Override
    public List<MgtReceivableDetail> queryBills(MgtReceivableDetail detail, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mgtReceivableDetailMapper.queryBills(detail);
    }

    @Override
    public List<MgtReceivableDetail> queryBillsFirstType(MgtReceivableDetail detail, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        return mgtReceivableDetailMapper.queryBillsByType(detail);
    }

	/**
	 * APP提交缴费单
	 * @param appOrderVo
	 * @return
	 */
	@Override
	public AppOrderVo paymentSubmit(AppOrderVo appOrderVo) throws Exception {
		Long orderAmount=0L;
		if(StringUtils.isEmpty(appOrderVo)||StringUtils.isEmpty(appOrderVo.getReceivableDetailList())){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"缴费明细清单");
		}
		if(StringUtils.isEmpty(appOrderVo.getToken())){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"token");
		}

		if(StringUtils.isEmpty(appOrderVo.getClientIp())){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"clientIp");
		}
		for(MgtReceivableDetail mgtReceivableDetail:appOrderVo.getReceivableDetailList()){
			mgtReceivableDetail=mgtReceivableDetailMapper.selectByPrimaryKey(mgtReceivableDetail.getReceivableId());
			if(null==mgtReceivableDetail) throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"缴费明细清单");
			orderAmount+=mgtReceivableDetail.getReceivableAmount();
		}

		AppUser appUser=appUserService.getByToken(appOrderVo.getToken());
		Map<String, String> map = new HashMap();
		map.put("appid", OrderFormHelper.CSP_APP_ID);
		map.put("openid", appUser.getSourceSystemId());
		map.put("tradeNo", OrderFormHelper.getRandomNum());
		map.put("orderAmount", orderAmount.toString());
		map.put("subject", "物业缴费");
		map.put("body", "物业缴费");
		map.put("backUrl", appOrderVo.getBackUrl());
		map.put("clientIp", appOrderVo.getClientIp());
		map.put("frontUrl", appOrderVo.getFrontUrl());
		map.put("orderType", OrderFormHelper.ORDER_TYPE_MGT_PAYMENT);
		map.put("description", "");
		map.put("timeExpire", "");
		map.put("integralAmount", appOrderVo.getUseIntegralNum()==null?"":appOrderVo.getUseIntegralNum().toString());
		map.put("sign", CspSignUtil.generateSign(map,OrderFormHelper.CSP_APP_SECRET));
		//提交订单
		OrderForm orderForm = orderFormService.orderSubmit(map);
		appOrderVo.setOrderForm(orderForm);
		//订单行插入
		saveOrder(appOrderVo.getReceivableDetailList(),orderForm.getId());
		return appOrderVo;
	}

	/**
	 * 支付成功通知回调处理
	 * @param map
	 */
	@Override
	public void notifySuccess(Map map) throws Exception {
		String tradeNo=map.get("tradeNo").toString();
		logger.debug("tradeNo {} 支付成功回调处理开始......",tradeNo);
		String timePaid=map.get("timePaid").toString();
		//商户订单号唯一性校验
		OrderForm orderFormQuery=new OrderForm();
		orderFormQuery.setAppId(OrderFormHelper.CSP_APP_ID);
		orderFormQuery.setSourceOrderNumber(tradeNo);
		orderFormQuery=orderFormMapper.findBySourceOrderNumber(orderFormQuery);
		if(null!=orderFormQuery){
			List<OrderFormLine> orderFormLineList=orderFormLineMapper.findByOrderId(orderFormQuery.getId());
			for(OrderFormLine orderFormLine:orderFormLineList){
				MgtReceivableDetail mgtReceivableDetail= mgtReceivableDetailMapper.selectByPrimaryKey(orderFormLine.getReceivableId());
				if(null!=mgtReceivableDetail){
					mgtReceivableDetail.setPayStatus(OrderFormHelper.OREDER_STATUS_Y);
					mgtReceivableDetail.setPaidTime(DateUtils.longToDate(new Long(timePaid)));
					mgtReceivableDetailMapper.updateByPrimaryKeySelective(mgtReceivableDetail);

					notifyToMerchant(orderFormQuery,mgtReceivableDetail);
				}
			}
		}
		logger.debug("tradeNo {} 支付成功回调处理结束......",tradeNo);
	}

	/**
	 * 订单行插入
	 * @param receivableDetailList
	 * @param orderId
	 */
	private void saveOrder(List<MgtReceivableDetail> receivableDetailList,Long orderId) {
		for(MgtReceivableDetail mgtReceivableDetail:receivableDetailList){
			OrderFormLine orderFormLine=new OrderFormLine();
			orderFormLine.setOrderId(orderId);
			orderFormLine.setReceivableId(mgtReceivableDetail.getReceivableId());
			orderFormLineMapper.insertSelective(orderFormLine);
		}
	}


	/**
	 * 物业缴费支付成功后通知第三方物业公司该记录已支付成功
	 * @param orderForm
	 * @param mgtReceivableDetail
	 */
	public void notifyToMerchant(OrderForm orderForm,MgtReceivableDetail mgtReceivableDetail){
		Runnable r= new Runnable(){
			@Override
			public void run() {
				if(!StringUtils.isEmpty(mgtReceivableDetail.getAppId())&&!StringUtils.isEmpty(mgtReceivableDetail.getBackUrl())) {
					logger.info("物业缴费支付成功后通知第三方物业公司，开始... orderNumber {}  backUrl {} sourceReceivableId {} ",orderForm.getOrderNumber(),mgtReceivableDetail.getBackUrl(),mgtReceivableDetail.getSourceReceivableId());
					// 获取商户来源
					OpenAppInfo openAppInfo = OpenAppUtils.getOpenAppInfo(mgtReceivableDetail.getAppId());
					if (null != openAppInfo) {
						// 签名
						Map<String, String> map = new HashMap();
						map.put("sourceReceivableId", mgtReceivableDetail.getSourceReceivableId());//源系统应收id
						map.put("tradeNo", orderForm.getOrderNumber());//乐家慧订单号
						map.put("channel", orderForm.getPayChannel());//支付渠道
						map.put("timePaid", new Long(mgtReceivableDetail.getPaidTime().getTime()).toString());//支付完成时间
						map.put("transeq", orderForm.getTranseq());//渠道流水号
						map.put("tradeStatus", "Y");//交易状态
						String sign = CspSignUtil.generateSign(map, openAppInfo.getAppSecret());
						map.put("sign", sign);
						// 调用接口通知商户
						String result = HttpUtil.post(mgtReceivableDetail.getBackUrl(), map);
						logger.info("物业缴费支付成功后通知第三方物业公司，结束... orderNumber {}  backUrl {} sourceReceivableId {} result {} ",orderForm.getOrderNumber(),mgtReceivableDetail.getBackUrl(),mgtReceivableDetail.getSourceReceivableId(),result);
						// 通知返回成功
						if (OrderFormHelper.OREDER_NOTIFY_SUCCESS.equals(result)) {
							mgtReceivableDetail.setNotifyFlag(Constants.YES);
							mgtReceivableDetailMapper.updateByPrimaryKeySelective(mgtReceivableDetail);
							// 通知返回订单不存在
						} else if (OrderFormHelper.OREDER_NOTIFY_NO_ORDER.equals(result)) {
							// 通知返回失败
						} else {

						}
					}
				}
			}
		};

		pool.execute(r);
	}

}
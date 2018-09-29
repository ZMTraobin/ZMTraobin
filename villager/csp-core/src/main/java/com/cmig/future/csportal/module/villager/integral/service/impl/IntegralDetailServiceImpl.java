package com.cmig.future.csportal.module.villager.integral.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmig.future.csportal.common.utils.DatetimeUtils;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.mapper.AppUserMapper;
import com.cmig.future.csportal.module.villager.integral.dto.IntegralConvert;
import com.cmig.future.csportal.module.villager.integral.dto.IntegralDetail;
import com.cmig.future.csportal.module.villager.integral.dto.IntegralScores;
import com.cmig.future.csportal.module.villager.integral.mapper.IntegralConvertMapper;
import com.cmig.future.csportal.module.villager.integral.mapper.IntegralDetailMapper;
import com.cmig.future.csportal.module.villager.integral.mapper.IntegralScoresMapper;
import com.cmig.future.csportal.module.villager.integral.service.IIntegralDetailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IntegralDetailServiceImpl extends BaseServiceImpl<IntegralDetail> implements IIntegralDetailService{

	@Autowired
	IntegralDetailMapper integralDetailMapper;
	
	@Autowired
	IntegralScoresMapper integralScoresMapper;
	
	@Autowired
	IntegralConvertMapper integralConvertMapper;
	
	@Autowired
	AppUserMapper appUserMapper;
	
	@Override
	public List<IntegralDetail> queryDetail(String appUserId,String month){
		// 按照用户id、月份查询
		List<IntegralDetail> detail = integralDetailMapper.queryDetail(appUserId,month);
    	
		return detail;
	};
	
	@Override
	public Map insertDetail(String code,String uid){
		Map map = new HashedMap();
		IntegralDetail detail = new IntegralDetail();
		Boolean flag = false;
		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 查询最新积分数据获取总积分
		IntegralDetail last= integralDetailMapper.queryLastDetail(uid);
		// 根据分值编码查询分数
		IntegralScores integralScores = integralScoresMapper.selectByCode(code);
		if(integralScores != null) {
			// 积分增加限制验证
			if("0".equals(integralScores.getCycles())){
				String date = shortSdf.format(DatetimeUtils.getCurrentDayStartTime())+"%";
				int count = integralDetailMapper.selectByCode(code,date);
				if(count < integralScores.getFrequencys()) {
					flag = true;
				}
			}else if("1".equals(integralScores.getCycles())) {
				Date start= DatetimeUtils.getCurrentWeekDayStartTime();
				Date end = DatetimeUtils.getCurrentWeekDayEndTime();
				int count = integralDetailMapper.selectByCycle(code,start,end);
				if(count < integralScores.getFrequencys()) {
					flag = true;
				}
			}else if("2".equals(integralScores.getCycles())) {
				Date start= DatetimeUtils.getCurrentMonthStartTime();
				Date end = DatetimeUtils.getCurrentMonthEndTime();
				int count = integralDetailMapper.selectByCycle(code,start,end);
				if(count < integralScores.getFrequencys()) {
					flag = true;
				}
			}else if("3".equals(integralScores.getCycles())) {
				Date start= DatetimeUtils.getCurrentQuarterStartTime();
				Date end = DatetimeUtils.getCurrentQuarterEndTime();
				int count = integralDetailMapper.selectByCycle(code,start,end);
				if(count < integralScores.getFrequencys()) {
					flag = true;
				}
			}else if("4".equals(integralScores.getCycles())) {
				Date start= DatetimeUtils.getCurrentYearStartTime();
				Date end = DatetimeUtils.getCurrentYearEndTime();
				int count = integralDetailMapper.selectByCycle(code,start,end);
				if(count < integralScores.getFrequencys()) {
					flag = true;
				}
			}
			
			if(flag) {
				// 当前的总积分+更新积分=新总积分
				if(last == null) {
					detail.setTotalScores(integralScores.getScores());
				}else {
					detail.setTotalScores(last.getTotalScores()+integralScores.getScores());
				}
				// 总积分插入用户表
				AppUser user = appUserMapper.selectByPrimaryKey(uid);
				user.setIntegralBalance(detail.getTotalScores());
				appUserMapper.updateByPrimaryKey(user);
				
				detail.setChangeScores(integralScores.getScores());
				detail.setChangeDescribe(integralScores.getScoresDescribe());
				detail.setScoresCode(code);
				detail.setUserId(uid);
				// 插入积分明细数据
				detail.setChangeDate(new Date());
				integralDetailMapper.insertSelective(detail);
				
				map.put("scores", integralScores.getScores());
				map.put("describe", "增加积分成功");
				return map;
			}
			map.put("scores", 0);
			map.put("describe", "今日已达积分限额");
			return map;
		}
		map.put("scores", 0);
		map.put("describe", "非法请求");
		return map;
	};
	
	@Override
	public Map convertToScores(String code,String coin,String uid){
		IntegralDetail detail = new IntegralDetail();
		Map map = new HashedMap();
		Boolean flag = false;
		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 查询最新积分数据获取总积分
		IntegralDetail last= integralDetailMapper.queryLastDetail(uid);
		// 根据分值编码查询分数
		IntegralConvert integralConvert = integralConvertMapper.selectByCode(code);
		if(integralConvert != null) {
			// 积分增加限制验证
			if("0".equals(integralConvert.getCycles())){
				String date = shortSdf.format(DatetimeUtils.getCurrentDayStartTime())+"%";
				int count = integralDetailMapper.selectByCode(code,date);
				if(count < integralConvert.getFrequencys()) {
					flag = true;
				}
			}else if("1".equals(integralConvert.getCycles())) {
				Date start= DatetimeUtils.getCurrentWeekDayStartTime();
				Date end = DatetimeUtils.getCurrentWeekDayEndTime();
				int count = integralDetailMapper.selectByCycle(code,start,end);
				if(count < integralConvert.getFrequencys()) {
					flag = true;
				}
			}else if("2".equals(integralConvert.getCycles())) {
				Date start= DatetimeUtils.getCurrentMonthStartTime();
				Date end = DatetimeUtils.getCurrentMonthEndTime();
				int count = integralDetailMapper.selectByCycle(code,start,end);
				if(count < integralConvert.getFrequencys()) {
					flag = true;
				}
			}else if("3".equals(integralConvert.getCycles())) {
				Date start= DatetimeUtils.getCurrentQuarterStartTime();
				Date end = DatetimeUtils.getCurrentQuarterEndTime();
				int count = integralDetailMapper.selectByCycle(code,start,end);
				if(count < integralConvert.getFrequencys()) {
					flag = true;
				}
			}else if("4".equals(integralConvert.getCycles())) {
				Date start= DatetimeUtils.getCurrentYearStartTime();
				Date end = DatetimeUtils.getCurrentYearEndTime();
				int count = integralDetailMapper.selectByCycle(code,start,end);
				if(count < integralConvert.getFrequencys()) {
					flag = true;
				}
			}
			
			if(flag) {
				// 按比例换算
				String[] rate = integralConvert.getConvertRate().split(":");
				Long score= Long.parseLong(rate[0]) * (Long.parseLong(coin) / Long.parseLong(rate[1]));
				Long remainder = Long.parseLong(coin) % Long.parseLong(rate[1]);
				// 判断是否到达上限
				if(score>integralConvert.getScores()) {
					Long convert = integralConvert.getScores() / Long.parseLong(rate[0]) *Long.parseLong(rate[1]);
					remainder = Long.parseLong(coin) - convert;
					score = Long.parseLong(rate[0]) * (convert / Long.parseLong(rate[1]));
				}
				// 当前的总积分+兑换积分=新总积分
				if(last == null) {
					detail.setTotalScores(score);
				}
				detail.setTotalScores(last.getTotalScores()+score);
				// 总积分插入用户表
				AppUser user = appUserMapper.selectByPrimaryKey(uid);
				user.setIntegralBalance(detail.getTotalScores());
				appUserMapper.updateByPrimaryKey(user);
				
				detail.setChangeScores(score);
				detail.setChangeDescribe(integralConvert.getScoresDescribe());
				detail.setScoresCode(code);
				detail.setUserId(uid);
				// 插入积分明细数据
				detail.setChangeDate(new Date());
				integralDetailMapper.insertSelective(detail);
				
				map.put("score", score);//兑换积分
				map.put("remainder", remainder);//剩余货币
				return map;
			}
		}
		map.put("score", 0);//兑换积分
		map.put("remainder", coin);//剩余货币
		return map;
	};
}
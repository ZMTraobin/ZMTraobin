package com.cmig.future.csportal.module.user.attentionCommunity.controllers;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.service.TestService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;

@Controller
public class TestController extends BaseController{
	@Autowired
	private TestService service;
	
	/**
	 *通过主键id进行删除
	 * @param attentionConmmunityUser
	 */
	@ResponseBody
	@RequestMapping(value = "/ljh/mgt/user/test")
	public ResponseData deleteCommunityUsers(AttentionCommunityUser attentionConmmunityUser) {
		attentionConmmunityUser.setId("9");
		service.deleteByPrimaryKey(attentionConmmunityUser);
		return new ResponseData();
	}
	/**
	 * 查询
	 * @param attentionConmmunityUser
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ljh/mgt/user/listTest")
	public ResponseData listCommunityUsers(AttentionCommunityUser attentionConmmunityUser,
			@RequestParam(defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			HttpServletRequest request) {
		IRequest IRequest = createRequestContext(request);
		System.out.println("ParamId:" + attentionConmmunityUser.getId());
		List<AttentionCommunityUser> listAttentionCommunityUsers = service.select(IRequest, attentionConmmunityUser, page, pageSize);
		System.err.println("results:" + listAttentionCommunityUsers.toString());
		for (AttentionCommunityUser attentionCommunityUser : listAttentionCommunityUsers) {
			System.err.println("singleObject:" + attentionCommunityUser.toString());
		}
		return new ResponseData(listAttentionCommunityUsers);
		
	}
	/**
	 * 修改
	 * @param request
	 * @param listAttentionCommunityUser
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ljh/mgt/user/updateTest")
	public ResponseData updateCommunityUsers(HttpServletRequest request,
			@RequestBody List<AttentionCommunityUser> listAttentionCommunityUser) {
		IRequest IRequest = createRequestContext(request);
		for (AttentionCommunityUser attentionCommunityUser : listAttentionCommunityUser) {
			System.err.println("ParamValues:"+attentionCommunityUser.toString());
		}
		List<AttentionCommunityUser> resultListAttentioncommunityUsers = service.batchUpdate(IRequest, listAttentionCommunityUser);
		for (AttentionCommunityUser attentionCommunityUser2 : resultListAttentioncommunityUsers) {
			System.err.println("ResultValues:"+attentionCommunityUser2.toString());
		}
		return new ResponseData(resultListAttentioncommunityUsers);
	}
	

}

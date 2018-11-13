/**
 * .
 */
package com.cmig.future.csportal.api.app.villager.sns.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.exceptions.ServiceExceptionHelper;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.service.IAttentionCommunityUserService;
import com.cmig.future.csportal.module.user.attentionCommunity.util.CommunityHelper;
import com.cmig.future.csportal.module.villager.follow.service.IFollowService;
import com.cmig.future.csportal.module.villager.sns.dto.PersonalSocietyUser;
import com.cmig.future.csportal.module.villager.sns.service.PersonalSocietyApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * controller of society
 */
@Controller
@ResponseBody
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PersonalSocietyApiController extends BaseExtendController {

    private final static Logger logger = LoggerFactory.getLogger(PersonalSocietyApiController.class);

    @Autowired
    private IAttentionCommunityUserService attentionCommunityUserService;
    
    @Autowired
    private PersonalSocietyApiService personalSocietyApiService;

    @Autowired
    private IAppUserService appUserService;
    
    @Autowired
	IAttentionCommunityUserService iAttentionCommunityUserService;
    
    @Autowired
    private IFollowService followService;

    /**
     * 查询该app用户的联系人
     * @param personalSocietyUser
     * @param request
     * @param response
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("null")
	@RequestMapping(value = "/st/personal/society/list", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp list(PersonalSocietyUser personalSocietyUser, 
    		HttpServletRequest request, 
    		HttpServletResponse response,
    		@RequestParam(defaultValue = DEFAULT_PAGE) int page,
    		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

        String token = getParam(request, "token", "");
        AppUser appUser = appUserService.getByToken(token);
        //当前关注的小区信息
        AttentionCommunityUser attentionCommunityUser = new AttentionCommunityUser();
        attentionCommunityUser.setUserId(appUser.getId());
        attentionCommunityUser.setIsAttention(CommunityHelper.IS_ATTENTION);
        List<AttentionCommunityUser> list = attentionCommunityUserService.findList(attentionCommunityUser);
        PersonalSocietyUser personalSocietyUsers = new PersonalSocietyUser();
        if (list != null && list.size() > 0) {
            AttentionCommunityUser thisAttentionCommunityUser = list.get(0);
            //小区的id
            personalSocietyUsers.setCommunityId(thisAttentionCommunityUser.getCommunityId());
            personalSocietyUsers.setSocietyUserId(appUser.getId());
        }
        String projectPath=Global.getProjectPath(request);
        List<PersonalSocietyUser> listPersonalSocietyUsers = personalSocietyApiService.PersonalSocietyUsers(personalSocietyUsers,page, pageSize);
        for (PersonalSocietyUser societyUser : listPersonalSocietyUsers) {
        	societyUser.setSocietyUserIcon(Global.getFullImagePath(societyUser.getSocietyUserIcon()));
		    societyUser.setSocietyUserIcon((societyUser.getSocietyUserIcon()==null||"".equals(societyUser.getSocietyUserIcon().trim()))?(projectPath+"/resources/upload/icon.png"):societyUser.getSocietyUserIcon());
		    AttentionCommunityUser cu = iAttentionCommunityUserService.getDefault(appUser.getId());
		    societyUser.setCommunityName(cu.getCommunityName());
		    //当前用户是否已关注目标用户
	        boolean isAttention=false;
	        if(!appUser.getId().equals(societyUser.getSocietyUserId())) {
	            isAttention=followService.isAttention(appUser.getId(), societyUser.getSocietyUserId());
	        }
	        societyUser.setIsAttention(isAttention);
        }
        return RetAppUtil.success(listPersonalSocietyUsers,"用户联系人列表!");
    }

    /**
     * 邀请新用户
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/sns/invite", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp invite(HttpServletRequest request, HttpServletResponse response) {
        String userId=getParam(request,"userId","");
        if(StringUtils.isEmpty(userId)){
            throw ServiceExceptionHelper.argsNull();
        }
        String token = getParam(request, "token", "");
        AppUser user=appUserService.getByToken(token);
        AppUser appUser= appUserService.get(userId);
        SmsUtil.send(String.format("%s邀请您参与我爱我乡的社交活动,请点击下面的链接参与活动 http://www.baidu.com",user.getMobile()),appUser.getMobile());
        return RetAppUtil.success("邀请成功!");
    }

}

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cmig.future.csportal.module.operate.neighbor.service;

import com.cmig.future.csportal.module.base.entity.AppPage;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicView;
import com.github.pagehelper.Page;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

/**
 * 每日美图Service
 * @author jinghao.che@zymobi.com
 * @version 2016-05-11
 */
public interface NhTopicViewService {

	public NhTopicView get(String id);
	
	public Page<NhTopicView> findPage(Page<NhTopicView> page, NhTopicView nhTopicView);
	
	public AppPage<NhTopicView> findPageApp(IRequest requestContext, AppPage<NhTopicView> page, NhTopicView nhTopicView);
	
	public AppPage<NhTopicView> findPageMyTopic(AppPage<NhTopicView> page, NhTopicView nhTopicView);

    public void updateTopic(NhTopicView nhTopicView);
}
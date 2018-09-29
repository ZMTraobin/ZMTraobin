/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cmig.future.csportal.module.operate.neighbor.service;

import com.cmig.future.csportal.module.base.entity.AppPage;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicCommentView;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

/**
 * 每日美图评论Service
 * @author jinghao.che@zymobi.com
 * @version 2016-05-11
 */
public interface NhTopicCommentViewService {
	public AppPage<NhTopicCommentView> findPage(IRequest requestContext, AppPage<NhTopicCommentView> page, NhTopicCommentView nhTopicCommentView);
}
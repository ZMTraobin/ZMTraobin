/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cmig.future.csportal.module.operate.neighbor.service.impl;

import java.util.List;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicReply;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmig.future.csportal.module.base.entity.AppPage;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicCommentView;
import com.cmig.future.csportal.module.operate.neighbor.mapper.NhTopicCommentViewMapper;
import com.cmig.future.csportal.module.operate.neighbor.service.NhTopicCommentViewService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;

/**
 * 每日美图评论Service
 * @author jinghao.che@zymobi.com
 * @version 2016-05-11
 */
@Service
@Transactional(readOnly = true)
public class NhTopicCommentViewServiceImpl implements NhTopicCommentViewService{

    Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private NhTopicCommentViewMapper nhTopicCommentViewMapper;
    @Autowired
    private ILjhNeighborTopicReplyService ljhNeighborTopicReplyService;

    @Override
    public AppPage<NhTopicCommentView> findPage(IRequest requestContext, AppPage<NhTopicCommentView> page,
            NhTopicCommentView nhTopicCommentView) {
        nhTopicCommentView.setPage(page); 
        PageHelper.startPage(page.getPageNo(), page.getPageSize());
        List<NhTopicCommentView> list = nhTopicCommentViewMapper.findList(nhTopicCommentView);
        if(!list.isEmpty()){
            list.forEach(comment->{
                logger.info("评论id:" + comment.getId());
                List<LjhNeighborTopicReply> replies = ljhNeighborTopicReplyService.getRepliesByCommentId(comment.getId());
                comment.setReplies(replies);
            });
        }

        page.setList(list);
        if (list != null&&list.size()>0&&list instanceof com.github.pagehelper.Page) {
            long total = ((com.github.pagehelper.Page)list).getTotal();
            page.setCount(Long.valueOf(total));
          }
        return page;
    }

    public NhTopicCommentViewMapper getNhTopicCommentViewMapper() {
        return nhTopicCommentViewMapper;
    }

    public void setNhTopicCommentViewMapper(NhTopicCommentViewMapper nhTopicCommentViewMapper) {
        this.nhTopicCommentViewMapper = nhTopicCommentViewMapper;
    }
    
}
/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cmig.future.csportal.module.operate.neighbor.service.impl;

import java.util.Arrays;
import java.util.List;

import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cmig.future.csportal.module.base.entity.AppPage;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopic;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicView;
import com.cmig.future.csportal.module.operate.neighbor.mapper.LjhNeighborTopicMapper;
import com.cmig.future.csportal.module.operate.neighbor.mapper.NhTopicViewMapper;
import com.cmig.future.csportal.module.operate.neighbor.service.NhTopicViewService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;

/**
 * 每日美图Service
 * @author jinghao.che@zymobi.com
 * @version 2016-05-11
 */
@Service
public class NhTopicViewServiceImpl implements NhTopicViewService{
    
    @Autowired
    private NhTopicViewMapper nhTopicViewMapper;
    @Autowired
    LjhNeighborTopicMapper ljhNeighborTopicMapper;

    @Override
    @Transactional(readOnly = true)
    public NhTopicView get(String id) {
        NhTopicView nhTopicView = nhTopicViewMapper.get(id);
        return nhTopicView;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NhTopicView> findPage(Page<NhTopicView> page, NhTopicView nhTopicView) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public AppPage<NhTopicView> findPageMyTopic(AppPage<NhTopicView> page, NhTopicView nhTopicView) {
        nhTopicView.setPage(page);
        PageHelper.startPage(page.getPageNo(), page.getPageSize());
        List<NhTopicView> list = nhTopicViewMapper.findListMyTopic(nhTopicView);
        page.setList(list);
        if (list != null&&list.size()>0&&list instanceof com.github.pagehelper.Page) {
          page.setCount(Long.valueOf(((com.github.pagehelper.Page)list).getTotal()));
        }
        return page;
    }

    @Override
    @Transactional(readOnly = true)
    public AppPage<NhTopicView> findPageApp(IRequest requestContext, AppPage<NhTopicView> page,
            NhTopicView nhTopicView) {
        nhTopicView.setPage(page);
        PageHelper.startPage(page.getPageNo(), page.getPageSize());
        //设置话题分类
        String typeIds = nhTopicView.getTypeIds();
        if(!StringUtil.isEmpty(typeIds)){
            String[] types = typeIds.split(",");
            List<String> typeList = Arrays.asList(types);
            nhTopicView.setTypeList(typeList);
        }
        List<NhTopicView> list = nhTopicViewMapper.findListApp(nhTopicView);
        page.setList(list);
        if (list != null&&list.size()>0&&list instanceof com.github.pagehelper.Page) {
          page.setCount(Long.valueOf(((com.github.pagehelper.Page)list).getTotal()));
        }
        return page;
    }

    public NhTopicViewMapper getNhTopicViewMapper() {
        return nhTopicViewMapper;
    }

    public void setNhTopicViewMapper(NhTopicViewMapper nhTopicViewMapper) {
        this.nhTopicViewMapper = nhTopicViewMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTopic(NhTopicView nhTopicView) {
        LjhNeighborTopic topic = new LjhNeighborTopic();
        topic.setId(nhTopicView.getId());
        topic.setDelFlag("1");
        ljhNeighborTopicMapper.updateByPrimaryKeySelective(topic);
    }
    
}
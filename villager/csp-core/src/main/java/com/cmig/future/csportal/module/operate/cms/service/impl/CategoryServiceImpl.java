package com.cmig.future.csportal.module.operate.cms.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmig.future.csportal.module.operate.cms.dto.Category;
import com.cmig.future.csportal.module.operate.cms.mapper.CategoryMapper;
import com.cmig.future.csportal.module.operate.cms.service.ICategoryService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

@Service
@Transactional
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements ICategoryService {

    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getAllCategories(IRequest requestContext, Category dto, int page, int pageSize) {
        return categoryMapper.getAllCategories();
    }

    @Override
    public Category getCategoryById(IRequest requestContext, String id) {
        return categoryMapper.getCategoryById(id);
    }

    @Override
    public Category getCategoryByParentId(IRequest requestContext, String parentId) {
        return categoryMapper.getCategoryByParentId(parentId);
    }

}
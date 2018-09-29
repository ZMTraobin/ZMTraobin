package com.cmig.future.csportal.module.operate.cms.service;

import java.util.List;

import com.cmig.future.csportal.module.operate.cms.dto.Category;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

public interface ICategoryService extends IBaseService<Category>, ProxySelf<ICategoryService>{

    /**
     * 获取所有cms栏目
     * @return 栏目列表
     */
    List<Category> getAllCategories(IRequest requestContext, Category dto, int page, int pageSize);


    /**
     * 根据id获取栏目
     * @param requestContext
     * @param dto 栏目
     * @return
     */
    Category getCategoryById(IRequest requestContext, String id);
    
    /**
     * 根据parentId获取栏目
     * @param requestContext
     * @param dto 栏目
     * @return
     */
    Category getCategoryByParentId(IRequest requestContext, String parentId);

}
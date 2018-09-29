package com.cmig.future.csportal.module.operate.cms.mapper;

import java.util.List;

import com.cmig.future.csportal.module.operate.cms.dto.Category;
import com.hand.hap.mybatis.common.Mapper;

/**
 * cms栏目管理 mapper
 * 
 * @author wangc
 *
 */
public interface CategoryMapper extends Mapper<Category> {

    List<Category> getAllCategories();

    /**
     * 根据id查询category
     * 
     * @param id
     * @return
     */
    Category getCategoryById(String id);

    /**
     * 根据parentId查询category
     * 
     * @param parentId
     * @return
     */
    Category getCategoryByParentId(String parentId);

    String getCategoryIdByType( String contentType );
}
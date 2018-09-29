/*
 * #{copyright}#
 */
package com.cmig.future.csportal.module.operate.cms.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.module.operate.cms.dto.Category;
import com.cmig.future.csportal.module.operate.cms.service.ICategoryService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;

/**
 * cms 栏目管理controller
 * 
 * @author wangc
 *
 */
@Controller
public class CategoryController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private ICategoryService categoryService;

//    @RequestMapping(value = "/cms/category/submit")
//    @ResponseBody
//    public ResponseData saveCategory(HttpServletRequest request, @RequestBody List<Category> categories,
//            BindingResult result) {
//        getValidator().validate(categories, result);
//        if (result.hasErrors()) {
//            ResponseData rd = new ResponseData(false);
//            rd.setMessage(getErrorMessage(result, request));
//            return rd;
//        } else {
//            IRequest requestCtx = createRequestContext(request);
//            categoryService.saveOrUpdateCategory(requestCtx, categories);
//            return new ResponseData(categories);
//        }
//    }

    @RequestMapping(value = "/cms/category/query")
    @ResponseBody
    public ResponseData query(Category dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(categoryService.getAllCategories(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/cms/category/queryById")
    @ResponseBody
    public ResponseData queryById(HttpServletRequest request, @RequestParam(required = true) String id) {
        IRequest requestContext = createRequestContext(request);
        List<Category> list = new ArrayList<Category>();
        Category category = categoryService.getCategoryById(requestContext, id);
        list.add(category);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/cms/category/update")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Category> dto,BindingResult result) {
        IRequest requestCtx = createRequestContext(request);
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData rd = new ResponseData(false);
            rd.setMessage(getErrorMessage(result, request));
            return rd;
        } else {
            return new ResponseData(categoryService.batchUpdate(requestCtx, dto));
        }
        
    }
    
    
//    @RequestMapping(value = "/cms/category/saveOrUpdate")
//    @ResponseBody
//    public ResponseData saveOrUpdate(HttpServletRequest request, @RequestBody List<Category> categories) {
//        IRequest requestCtx = createRequestContext(request);
//        return new ResponseData(categoryService.saveOrUpdateCategory(requestCtx, categories));
//    }

    @RequestMapping(value = "/cms/category/updateCategoryDelFlag")
    @ResponseBody
    public ResponseData deleteCategoryByFlag(HttpServletRequest request, Category category) {
        IRequest requestCtx = createRequestContext(request);
        category.setDelFlag(category.DEL_FLAG_DELETE);
        categoryService.updateByPrimaryKeySelective(requestCtx, category);
        return new ResponseData();
    }

    @RequestMapping(value = "/cms/category/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Category> dto) {
        categoryService.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * category_detail page.
     * 
     * @param request
     * @return ModelAndView对象
     */
    @RequestMapping(value = "/module/operate/cms/category_detail.html")
    public ModelAndView home(HttpServletRequest request, @RequestParam(required = false) String isedit,
            @RequestParam(required = false) String parentId, @RequestParam(required = false) String id) {
        IRequest iRequest = createRequestContext(request);
        ModelAndView view = new ModelAndView(getViewPath() + "/module/operate/cms/category_detail");
        String parentName = "";
        // isEdit:0是新增，1是更新
        if ("1".equals(isedit)) {
            view.addObject("categoryId", id);
        } else {
            // 提前取出category的id
            view.addObject("categoryId", IdGen.uuid());
            if (parentId != null && !"".equals(parentId) && !"null".equals(parentId)) {
                Category parentCategory = categoryService.getCategoryById(iRequest, parentId);
                parentName = parentCategory.getName();
            }
        }
        view.addObject("parentName", parentName);
        return view;
    }
}
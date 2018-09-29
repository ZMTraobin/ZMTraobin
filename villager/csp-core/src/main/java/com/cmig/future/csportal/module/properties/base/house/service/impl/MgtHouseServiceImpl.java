package com.cmig.future.csportal.module.properties.base.house.service.impl;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.excel.ExcelError;
import com.cmig.future.csportal.common.utils.excel.ExcelUtil;
import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.common.utils.excel.annotation.ExcelField;
import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouse;
import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouseImport;
import com.cmig.future.csportal.module.properties.base.house.mapper.MgtHouseMapper;
import com.cmig.future.csportal.module.properties.base.house.service.IMgtHouseService;
import com.cmig.future.csportal.module.properties.base.map.dto.StructureBuildingMap;
import com.cmig.future.csportal.module.properties.base.map.mapper.StructureBuildingMapMapper;
import com.cmig.future.csportal.module.properties.base.utils.MgtStructureHelper;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.mapper.BaseCommunityMapper;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class MgtHouseServiceImpl extends BaseServiceImpl<MgtHouse> implements IMgtHouseService {

    private static Logger logger = LoggerFactory.getLogger(MgtHouseServiceImpl.class);
    @Autowired
    private MgtHouseMapper mgtHouseMapper;
    @Autowired
    private StructureBuildingMapMapper structureBuildingMapMapper;
    @Autowired
    private BaseCommunityMapper baseCommunityMapper;

    @Override
    public MgtHouse queryByHouseId(MgtHouse dto) {
        return mgtHouseMapper.queryByHouseId(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public MgtHouse saveOrUpdate(IRequest requestCtx, MgtHouse dto){
        logger.info("dto对象是:{}", JSONUtils.valueToString(dto));
        if (logger.isDebugEnabled()) {
            logger.debug("dto对象是:{}", JSONUtils.valueToString(dto));
        }
        String status = dto.get__status();
        if (DTOStatus.ADD.equals(status)) {
            mgtHouseMapper.insertSelective(dto);
            Long houseId = dto.getHouseId();
            Long parentStructureId = dto.getParentStructureId();
            if(houseId != null && parentStructureId != null){
                StructureBuildingMap structureBuildingMap = new StructureBuildingMap();
                structureBuildingMap.setBuildingType(MgtStructureHelper.HOUSE);
                structureBuildingMap.setBuildingId(houseId);
                structureBuildingMap.setStructureId(parentStructureId);
                structureBuildingMapMapper.insertSelective(structureBuildingMap);
            }
        } else if (DTOStatus.UPDATE.equals(status)) {
            MgtHouse house = mgtHouseMapper.queryByHouseId(dto);
            Long new_parentStructureId = dto.getParentStructureId();
            Long old_parentStructureId = house.getParentStructureId();
            if (new_parentStructureId != null && !new_parentStructureId.equals(old_parentStructureId)){
                Long houseId = dto.getHouseId();
                StructureBuildingMap  structureBuildingMap = new StructureBuildingMap();
                structureBuildingMap.setBuildingType(MgtStructureHelper.HOUSE);
                structureBuildingMap.setBuildingId(houseId);
                structureBuildingMap.setStructureId(old_parentStructureId);

                StructureBuildingMap sbm = structureBuildingMapMapper.findByBuildingIdAndStructureId(structureBuildingMap);
                sbm.setStructureId(new_parentStructureId);
                structureBuildingMapMapper.updateByPrimaryKeySelective(sbm);
            }
            mgtHouseMapper.updateByPrimaryKeySelective(dto);
        }
        return dto;
    }

    @Override
    public void validationImportData(ImportExcel ei, List<MgtHouseImport> list) {
        BaseCommunity baseCommunity = new BaseCommunity();
        logger.debug("校验导入数据开始......");
        // 校验数据重复
        ExcelUtil.duplicateMutiColumsValidator(ei.getSheetName(),ei.getDataRowNum(), list, ei.getExcelErrorList(), new String[] {"structureCode"}, MgtHouseImport.class);
        ExcelUtil.duplicateMutiColumsValidator(ei.getSheetName(),ei.getDataRowNum(), list, ei.getExcelErrorList(), new String[] {"sourceStructureCode"}, MgtHouseImport.class);
        //编码和父结构的数据格式
        String regEx = "([A-Z]{1,}[0-9]{0,})(\\-[A-Z0-9]{1,}){0,}";
        for (int i = 0; i < list.size(); i++) {
            MgtHouseImport mgtHouseImport = list.get(i);
            String structureCode = mgtHouseImport.getStructureCode();//编码
            String communityCode = mgtHouseImport.getCommunityCode();//项目编码
            //编码格式校验
            boolean sCode = strMatch(regEx,structureCode);
            if(!sCode){
                ExcelField ef = ExcelUtil.getExcelField(mgtHouseImport, "structureCode");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "编码格式不正确。");
                ei.getExcelErrorList().add(error);
            }
            String check_structureCode = structureCode;//取数据的编码
            if(check_structureCode.indexOf("-") == -1){//如果编码不存在"-", 是错的
                ExcelField ef = ExcelUtil.getExcelField(mgtHouseImport, "structureCode");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "编码不正确。");
                ei.getExcelErrorList().add(error);
            }else{//如果存在"-",查询数据库中
                //编码是否已经存在于数据库中,编码类型校验
                hasStructureCode(mgtHouseImport,ei,i,structureCode);
            }
            //校验项目编码
            baseCommunity.setCommunityCode(communityCode);
            List<BaseCommunity> communityList = baseCommunityMapper.findListByCommunityCode(baseCommunity);
            if(StringUtils.isEmpty(communityList)){
                ExcelField ef = ExcelUtil.getExcelField(mgtHouseImport, "communityCode");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "项目编码不正确,不存在该项目编码。");
                ei.getExcelErrorList().add(error);
            }else{
                String communityId = communityList.get(0).getId();
                mgtHouseImport.setCommunityId(communityId);
                //有源编码,则校验房屋源编码+项目id唯一
                String sourceStructureCode = mgtHouseImport.getSourceStructureCode();
                if(!StringUtils.isEmpty(sourceStructureCode) && !StringUtils.isEmpty(communityId)){
                    MgtHouse mgtHouse = new MgtHouse();
                    mgtHouse.setCommunityId(communityId);
                    mgtHouse.setSourceHouseCode(sourceStructureCode);
                    MgtHouse dto = mgtHouseMapper.findBySourceHouseCodeAndCid(mgtHouse);
                    if(!StringUtils.isEmpty(dto)){
                        ExcelField ef = ExcelUtil.getExcelField(mgtHouseImport, "sourceStructureCode");
                        ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "该项目下已存在这个建筑原始编码。");
                        ei.getExcelErrorList().add(error);
                    }
                }
            }
        }
        logger.debug("校验导入数据结束......");
    }

    @Override
    @Transactional(readOnly = false)
    public void saveImportDate(List<MgtHouseImport> list){
        logger.debug("导入数据持久化开始......");
        //String communityCode = baseCommunity.getCommunityCode();//项目编码
        for (int i = 0; i < list.size(); i++) {
            MgtHouseImport mgtHouseImport = list.get(i);
            String structureCode = mgtHouseImport.getStructureCode();//编码
            //String communityCode = mgtHouseImport.getCommunityCode();//项目编码
            String structureType = mgtHouseImport.getStructureType();//建筑类型
            String hType = MgtStructureHelper.HOUSE_TYPE;//配置的房屋实体代码
            String structureName = mgtHouseImport.getStructureName();//节点名称
            String structureFullName = mgtHouseImport.getStructureFullName();//建筑名称
            String sourceStructureCode = mgtHouseImport.getSourceStructureCode();//建筑原始编码
            String buildingArea  = mgtHouseImport.getBuildingArea();//建筑面积
            String paymentArea = mgtHouseImport.getPaymentArea();//收费面积
            String useType = mgtHouseImport.getUseType();//用途
            String decorationStatus = mgtHouseImport.getDecorationStatus();//装修情况
            String communityId = mgtHouseImport.getCommunityId();//项目id
            if(hType.equals(structureType)) {//如果建筑类型等于配置的代码:eg:CN03,则是房屋信息
                //已验证所有导入数据都不存在,直接保存
                MgtHouse mgtHouse = new MgtHouse();
                mgtHouse.setHouseCode(structureCode);
                mgtHouse.setCommunityId(communityId);
                mgtHouse.setHouseName(structureName);
                mgtHouse.setHouseFullName(structureFullName);
                mgtHouse.setUseType(useType);
                if(!StringUtils.isEmpty(buildingArea)){
                    mgtHouse.setBuildingArea(BigDecimal.valueOf(Double.parseDouble(buildingArea)));
                }
                if(!StringUtils.isEmpty(paymentArea)){
                    mgtHouse.setPaymentArea(BigDecimal.valueOf(Double.parseDouble(paymentArea)));
                }
                mgtHouse.setDecorationStatus(decorationStatus);
                mgtHouse.setSourceHouseCode(sourceStructureCode);
                mgtHouseMapper.insertSelective(mgtHouse);
            }
        }
        logger.debug("导入数据持久化结束......共导入:"+list.size()+" 条数据");
    }

    @Override
    public MgtHouse findBySourceHouseCodeAndSourceSystem(MgtHouse dto) {
        return mgtHouseMapper.findBySourceHouseCodeAndSourceSystem(dto);
    }

    /**
     * 根据正则表达式,验证数据是否符合规则
     * @param regEx
     * @param str
     * @return
     */
    public boolean strMatch(String regEx,String str){
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        boolean b = matcher.matches();
        return b;
    }

    /**
     * 判断编码是否存在与数据库中(编码唯一)
     * @param mgtHouseImport
     * @param ei
     * @param i
     * @param structureCode
     * @return
     */
    public boolean hasStructureCode(MgtHouseImport mgtHouseImport,ImportExcel ei,int i,String structureCode){
        String hType = MgtStructureHelper.HOUSE_TYPE;//配置的房屋实体代码
        String structureType = mgtHouseImport.getStructureType();
        if(!hType.equals(structureType)) {//如果建筑类型不是配置的代码:eg:CN03,则不是房屋实体
            ExcelField ef = ExcelUtil.getExcelField(mgtHouseImport, "structureType");
            ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "编码类型不正确。");
            ei.getExcelErrorList().add(error);
            return false;
        }
        else if(hType.equals(structureType)){//如果建筑类型是配置的代码:eg:CN03,则是房屋实体
            MgtHouse house = new MgtHouse();
            house.setHouseCode(structureCode);
            MgtHouse dto = mgtHouseMapper.findByStructureCode(house);
            if(!StringUtils.isEmpty(dto)){
                ExcelField ef = ExcelUtil.getExcelField(mgtHouseImport, "structureCode");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "房屋编码已经存在,不能重复添加。");
                ei.getExcelErrorList().add(error);
                return false;
            }
            return true;
        }
        else{//建筑类型不是结构,也不是房屋
            ExcelField ef = ExcelUtil.getExcelField(mgtHouseImport, "structureType");
            ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "编码类型不正确。");
            ei.getExcelErrorList().add(error);
            return false;
        }
    }

    @Override
    public List<MgtHouse> selectList(IRequest requestContext, MgtHouse dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mgtHouseMapper.selectList(dto);
    }

	@Override
	public MgtHouse get(Object o) {
		return mgtHouseMapper.selectByPrimaryKey(o);
	}
}
package com.cmig.future.csportal.module.properties.base.structure.service.impl;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.excel.ExcelError;
import com.cmig.future.csportal.common.utils.excel.ExcelUtil;
import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.common.utils.excel.annotation.ExcelField;
import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouse;
import com.cmig.future.csportal.module.properties.base.house.mapper.MgtHouseMapper;
import com.cmig.future.csportal.module.properties.base.map.dto.StructureBuildingMap;
import com.cmig.future.csportal.module.properties.base.map.mapper.StructureBuildingMapMapper;
import com.cmig.future.csportal.module.properties.base.structure.dto.MgtStructure;
import com.cmig.future.csportal.module.properties.base.structure.dto.MgtStructureImport;
import com.cmig.future.csportal.module.properties.base.structure.mapper.MgtStructureMapper;
import com.cmig.future.csportal.module.properties.base.structure.service.IMgtStructureService;
import com.cmig.future.csportal.module.properties.base.utils.MgtStructureHelper;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.mapper.BaseCommunityMapper;
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
public class MgtStructureServiceImpl extends BaseServiceImpl<MgtStructure> implements IMgtStructureService {

    @Autowired
    private BaseCommunityMapper baseCommunityMapper;
    @Autowired
    private MgtStructureMapper mgtStructureMapper;
    @Autowired
    private MgtHouseMapper mgtHouseMapper;
    @Autowired
    private StructureBuildingMapMapper structureBuildingMapMapper;

    private static Logger logger = LoggerFactory.getLogger(MgtStructureServiceImpl.class);
    @Override
    public void validationImportData(ImportExcel ei, List<MgtStructureImport> list,String communityId) {
        logger.debug("校验导入数据开始......");
	    BaseCommunity baseCommunity=new BaseCommunity();
        // 校验数据重复
        ExcelUtil.duplicateMutiColumsValidator(ei.getSheetName(),ei.getDataRowNum(), list, ei.getExcelErrorList(), new String[] {"structureCode"}, MgtStructureImport.class);
        ExcelUtil.duplicateMutiColumsValidator(ei.getSheetName(),ei.getDataRowNum(), list, ei.getExcelErrorList(), new String[] {"sourceStructureCode"}, MgtStructureImport.class);
        //ExcelUtil.duplicateMutiColumsValidator(ei.getSheetName(),ei.getDataRowNum(), list, ei.getExcelErrorList(), new String[] {"structureFullName"}, MgtStructureImport.class);
        //编码和父结构的数据格式
        String regEx = "([A-Z]{1,}[0-9]{0,})(\\-[A-Z0-9]{1,}){0,}";
        for (int i = 0; i < list.size(); i++) {
            MgtStructureImport mgtStructureImport = list.get(i);
            String parentStructureCode = mgtStructureImport.getParentStructureCode();//父结构
            String structureCode = mgtStructureImport.getStructureCode();//编码
            //父结构格式校验
            boolean pCode = strMatch(regEx,parentStructureCode);
            if(!pCode){
                ExcelField ef = ExcelUtil.getExcelField(mgtStructureImport, "parentStructureCode");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "父结构编码格式不正确。");
                ei.getExcelErrorList().add(error);
            }
            //编码格式校验
            boolean sCode = strMatch(regEx,structureCode);
            if(!sCode){
                ExcelField ef = ExcelUtil.getExcelField(mgtStructureImport, "structureCode");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "编码格式不正确。");
                ei.getExcelErrorList().add(error);
            }
            String check_parentStructureCode = parentStructureCode;//取数据的父结构
            if(check_parentStructureCode.indexOf("-") == -1){//如果父节点不存在"-",则第一条是最高级父节点,对应项目编码
                String communityCode = check_parentStructureCode;
                baseCommunity.setCommunityCode(communityCode);
                List<BaseCommunity> communityList = baseCommunityMapper.findListByCommunityCode(baseCommunity);
                if(StringUtils.isEmpty(communityList)){
                    ExcelField ef = ExcelUtil.getExcelField(mgtStructureImport, "parentStructureCode");
                    ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "父结构编码不正确,不存在该项目编码。");
                    ei.getExcelErrorList().add(error);
                }else{
                    String cid = communityList.get(0).getId();
                    if(!communityId.equals(cid)){
                        ExcelField ef = ExcelUtil.getExcelField(mgtStructureImport, "parentStructureCode");
                        ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "父结构编码不是所选项目的项目编码。");
                        ei.getExcelErrorList().add(error);
                    }
                }
            }else{//如果存在"-",则不是最高级,查询数据库中,是否已经存在该父节点,如果不存在,则报错
                String communityCode = check_parentStructureCode.substring(0,check_parentStructureCode.indexOf("-"));//项目编码
                baseCommunity.setCommunityCode(communityCode);
                //hasParentStructureCode(mgtStructureImport,ei,i,parentStructureCode);
                //如果父结构不存在"-",父结构一定存在于编码中
                boolean hasTree = false;//是否存在(构成树形结构):默认否
                for(int x = 0; x< list.size(); x++){
                    String structureCode_x = list.get(x).getStructureCode();//编码
                    if(parentStructureCode.equals(structureCode_x)){//父结构存在编码中,符合逻辑
                        hasTree = true;//存在
                        break;
                    }
                }
                if(!hasTree){//父结构不存在于excel的编码列中,则去数据库查询是否存在
                    hasParentStructureCode(mgtStructureImport,ei,i,parentStructureCode);
                }
                //编码是否已经存在于数据库中,编码类型校验
                hasStructureCode(mgtStructureImport,ei,i,structureCode,communityId);
            }
        }

        logger.debug("校验导入数据结束......");
    }

    @Override
    @Transactional(readOnly = false)
    public void saveImportDate(List<MgtStructureImport> list,String versionId,String communityId){
        logger.debug("导入数据持久化开始......");
        //String communityCode = baseCommunity.getCommunityCode();//项目编码
        String sType = MgtStructureHelper.STRUCTURE_TYPE;//配置的建筑类型代码
        String hType = MgtStructureHelper.HOUSE_TYPE;//配置的房屋实体代码
        for (int i = 0; i < list.size(); i++) {
            MgtStructureImport mgtStructureImport = list.get(i);
            String parentStructureCode = mgtStructureImport.getParentStructureCode();//父结构
            String structureCode = mgtStructureImport.getStructureCode();//编码
            String structureType = mgtStructureImport.getStructureType();//建筑类型
            String structureName = mgtStructureImport.getStructureName();//节点名称
            String structureFullName = mgtStructureImport.getStructureFullName();//建筑名称
            String sourceStructureCode = mgtStructureImport.getSourceStructureCode();//建筑原始编码
            String buildingArea  = mgtStructureImport.getBuildingArea();//建筑面积
            String paymentArea = mgtStructureImport.getPaymentArea();//收费面积
            String useType = mgtStructureImport.getUseType();//用途
            String decorationStatus = mgtStructureImport.getDecorationStatus();//装修情况
            if(sType.equals(structureType)) {//如果建筑类型为配置的代码:eg:CN02,则是建筑结构
                MgtStructure mgtStructure = new MgtStructure();
                mgtStructure.setStructureCode(structureCode);
                mgtStructure.setVersionId(Long.parseLong(versionId));
                mgtStructure.setCommunityId(communityId);
                mgtStructure.setStructureName(structureName);
                mgtStructure.setStructureFullName(structureFullName);
                mgtStructure.setSourceStructureCode(sourceStructureCode);
                mgtStructure.setStructureType(structureType);
                MgtStructure mgtStructureQuery = new MgtStructure();
                String check_parentStructureCode = parentStructureCode;
                if (check_parentStructureCode.indexOf("-") == -1) {//如果父节点不存在"-",则是最高级父节点,对应项目编码
                    mgtStructure.setParentStructureId((long) 0);
                } else {//如果父节点存在"-",则第一条不是最高级父节点
                    mgtStructureQuery.setStructureCode(parentStructureCode);
                    MgtStructure dto = mgtStructureMapper.findByStructureCode(mgtStructureQuery);//根据父结构查询数据库中数据(已校验过,必定存在数据)
                    mgtStructure.setParentStructureId(dto.getStructureId());
                }
                mgtStructureMapper.insertSelective(mgtStructure);
            }else if(hType.equals(structureType)){//如果建筑类型等于配置的代码:eg:CN03,则是房屋实体
                MgtHouse mgtHouseQuery = new MgtHouse();
                mgtHouseQuery.setHouseCode(structureCode);
                mgtHouseQuery = mgtHouseMapper.findByStructureCode(mgtHouseQuery);
                Long houseId = null;
                //如果房屋已存在,则只增加房屋与结构的对应关系
                //如果不存在,则先创建房屋实体,在创建与结构的对应关系
                if(!StringUtils.isEmpty(mgtHouseQuery)){//如果房屋实体已存在
                    houseId = mgtHouseQuery.getHouseId();
                }else{//房屋实体不存在
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
                    houseId = mgtHouse.getHouseId();
                }
                //建筑实体与结构关系
                StructureBuildingMap structureBuildingMap = new StructureBuildingMap();
                structureBuildingMap.setBuildingType(MgtStructureHelper.HOUSE);
                MgtStructure mgtStructureQuery = new MgtStructure();
                mgtStructureQuery.setStructureCode(parentStructureCode);
                MgtStructure dto = mgtStructureMapper.findByStructureCode(mgtStructureQuery);//根据父结构查询数据库中数据(已校验过,必定存在数据)
                structureBuildingMap.setStructureId(dto.getStructureId());
                structureBuildingMap.setBuildingId(houseId);
                structureBuildingMapMapper.insertSelective(structureBuildingMap);
            }
        }
        logger.debug("导入数据持久化结束......共导入:"+list.size()+" 条数据");
    }

    /**
     * 判断父结构是否存在与数据库中(建筑结构表的建筑编码字段)
     * @param mgtStructureImport
     * @param ei
     * @param i
     * @param parentStructureCode
     */
    public boolean hasParentStructureCode(MgtStructureImport mgtStructureImport,ImportExcel ei,int i,String parentStructureCode){
        MgtStructure mgtStructure = new MgtStructure();
        mgtStructure.setStructureCode(parentStructureCode);
        MgtStructure dto = mgtStructureMapper.findByStructureCode(mgtStructure);
        if(StringUtils.isEmpty(dto)){
            ExcelField ef = ExcelUtil.getExcelField(mgtStructureImport, "parentStructureCode");
            ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "不是项目编码,也不存在该建筑编码。");
            ei.getExcelErrorList().add(error);
            return false;
        }
        return true;
    }

    /**
     * 判断编码是否存在与数据库中(编码唯一)
     * 如果是房屋,且有源编码,则校验房屋源编码+项目id唯一
     * @param mgtStructureImport
     * @param ei
     * @param i
     * @param structureCode
     * @return
     */
    public boolean hasStructureCode(MgtStructureImport mgtStructureImport,ImportExcel ei,int i,String structureCode,String communityId){
        String sType = MgtStructureHelper.STRUCTURE_TYPE;//配置的建筑类型代码
        String hType = MgtStructureHelper.HOUSE_TYPE;//配置的房屋实体代码
        String structureType = mgtStructureImport.getStructureType();
        if(sType.equals(structureType)) {//如果建筑类型为配置的代码:eg:CN02,则是建筑结构
            MgtStructure mgtStructure = new MgtStructure();
            mgtStructure.setStructureCode(structureCode);
            MgtStructure dto = mgtStructureMapper.findByStructureCode(mgtStructure);
            if(!StringUtils.isEmpty(dto)){
                ExcelField ef = ExcelUtil.getExcelField(mgtStructureImport, "structureCode");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "编码已经存在,不能重复添加。");
                ei.getExcelErrorList().add(error);
                return false;
            }
            return true;
        }
        else if(hType.equals(structureType)){//如果建筑类型是配置的代码:eg:CN03,则是房屋实体
            //校验房屋源编码+项目id唯一
            String sourceStructureCode = mgtStructureImport.getSourceStructureCode();
            if(!StringUtils.isEmpty(sourceStructureCode) && !StringUtils.isEmpty(communityId)) {
                MgtHouse mgtHouse = new MgtHouse();
                mgtHouse.setCommunityId(communityId);
                mgtHouse.setSourceHouseCode(sourceStructureCode);
                MgtHouse dto = mgtHouseMapper.findBySourceHouseCodeAndCid(mgtHouse);
                if (!StringUtils.isEmpty(dto)) {
                    ExcelField ef = ExcelUtil.getExcelField(mgtStructureImport, "sourceStructureCode");
                    ExcelError error = new ExcelError(ei.getSheetName(), ei.getDataRowNum() + 1 + i, ef.title(), "该项目下已存在这个建筑原始编码。");
                    ei.getExcelErrorList().add(error);
                    return false;
                }
            }
            return true;
        }else{//建筑类型不是结构,也不是房屋
            ExcelField ef = ExcelUtil.getExcelField(mgtStructureImport, "structureType");
            ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "编码类型不正确。");
            ei.getExcelErrorList().add(error);
            return false;
        }
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

    @Override
    public MgtStructure findByStructureCode(MgtStructure dto){
        return mgtStructureMapper.findByStructureCode(dto);
    }

    @Override
    public MgtStructure queryByStructureId(MgtStructure dto){
        return mgtStructureMapper.queryByStructureId(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public MgtStructure saveOrUpdate(IRequest requestCtx, MgtStructure dto,String houseId) {
        logger.info("dto对象是:{}", JSONUtils.valueToString(dto));
        if (logger.isDebugEnabled()) {
            logger.debug("dto对象是:{}", JSONUtils.valueToString(dto));
        }
        String status = dto.get__status();
        if (DTOStatus.ADD.equals(status)) {
            mgtStructureMapper.insertSelective(dto);
            if(!StringUtils.isEmpty(houseId)){
                StructureBuildingMap structureBuildingMap = new StructureBuildingMap();
                structureBuildingMap.setStructureId(dto.getStructureId());
                structureBuildingMap.setBuildingId(Long.parseLong(houseId));
                structureBuildingMap.setBuildingType(MgtStructureHelper.HOUSE);
                structureBuildingMapMapper.insertSelective(structureBuildingMap);
            }
        } else if (DTOStatus.UPDATE.equals(status)) {
            mgtStructureMapper.updateByPrimaryKeySelective(dto);
        }
        return dto;
    }

    @Override
    public int deleteByVersionId(MgtStructure dto){
        return mgtStructureMapper.deleteByVersionId(dto);
    }

}
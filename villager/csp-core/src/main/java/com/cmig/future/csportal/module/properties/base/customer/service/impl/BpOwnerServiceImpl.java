package com.cmig.future.csportal.module.properties.base.customer.service.impl;

import com.cmig.future.csportal.common.utils.IdcardUtils;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.excel.ExcelError;
import com.cmig.future.csportal.common.utils.excel.ExcelUtil;
import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.common.utils.excel.annotation.ExcelField;
import com.cmig.future.csportal.module.properties.base.customer.CustomerInfoException;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpGeneral;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpHouseMap;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpOwner;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpOwnerImport;
import com.cmig.future.csportal.module.properties.base.customer.mapper.BpGeneralMapper;
import com.cmig.future.csportal.module.properties.base.customer.mapper.BpHouseMapMapper;
import com.cmig.future.csportal.module.properties.base.customer.mapper.BpOwnerMapper;
import com.cmig.future.csportal.module.properties.base.customer.service.IBpOwnerService;
import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouse;
import com.cmig.future.csportal.module.properties.base.house.mapper.MgtHouseMapper;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BpOwnerServiceImpl extends BaseServiceImpl<BpOwner> implements IBpOwnerService {

    @Autowired
    private BpGeneralMapper bpGeneralMapper;
    @Autowired
    private BpHouseMapMapper bpHouseMapMapper;
    @Autowired
    private BpOwnerMapper bpOwnerMapper;
    @Autowired
    private MgtHouseMapper mgtHouseMapper;

    private static Logger logger = LoggerFactory.getLogger(BpOwnerServiceImpl.class);

    @Override
    @Transactional
    public void validationImportData(ImportExcel ei, List<BpOwnerImport> list) {
        logger.debug("校验导入数据开始......");
        // 校验数据重复
        ExcelUtil.duplicateMutiColumsValidator(ei.getSheetName(), ei.getDataRowNum(), list, ei.getExcelErrorList(),
                new String[] { "structureCode", "idNo" }, BpOwnerImport.class);
        // 编码和父结构的数据格式
        for (int i = 0; i < list.size(); i++) {
            BpOwnerImport bpOwnerImport = list.get(i);
            String houseCode = bpOwnerImport.getStructureCode();// 房屋编码
            String idType = bpOwnerImport.getIdType();
            String idNo = bpOwnerImport.getIdNo();// 身份证号
            String mobile = bpOwnerImport.getMobile();

	        MgtHouse house = new MgtHouse();
	        house.setHouseCode(houseCode);
	        MgtHouse build = mgtHouseMapper.findByStructureCode(house);
            if (build == null) {
                ExcelField ef = ExcelUtil.getExcelField(bpOwnerImport, "structureCode");
                ExcelError error = new ExcelError(ei.getSheetName(), ei.getDataRowNum() + 1 + i, ef.title(), "建筑代码不存在");
                ei.getExcelErrorList().add(error);
            }else{
	            bpOwnerImport.setBuildingId(build.getHouseId());

            }
            // 校验身份证
             if ("I".equals(idType)) {
                boolean idCard = IdcardUtils.validateCard(idNo);
	             if (!idCard) {
		             ExcelField ef = ExcelUtil.getExcelField(bpOwnerImport, "idNo");
		             ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(),"身份证号码错误");
		             ei.getExcelErrorList().add(error);
	             }
             }
	        //boolean updateFlag=false;
             //校验重复信息
             BpGeneral mobileBp = bpGeneralMapper.getUnionMobile(mobile);
             BpGeneral idNoBp = bpGeneralMapper.getByIdNo(idNo,idType);
              if(mobileBp!=null){
                  if(null!=idNoBp){
                      if(mobileBp.getBpId().equals(idNoBp.getBpId())
                              && mobileBp.getIdType().equals(mobileBp.getIdType())){
                          //更新
                          bpOwnerImport.setBpId(idNoBp.getBpId());
                          if(null!=bpOwnerImport.getBuildingId()){
                              List<BpHouseMap> listBpHouseMap=bpHouseMapMapper.getByBpBuilding(idNoBp.getBpId(),bpOwnerImport.getBuildingId());
                              if(!StringUtils.isEmpty(listBpHouseMap)){
                                  ExcelField ef = ExcelUtil.getExcelField(bpOwnerImport, "structureCode");
                                  ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(),"业主与房屋关系已存在，不能重复导入。");
                                  ei.getExcelErrorList().add(error);
                              }
                          }
                      }else{
                          //报错
                          ExcelField ef = ExcelUtil.getExcelField(bpOwnerImport, "mobile");
                          ExcelError error = new ExcelError(ei.getSheetName(), ei.getDataRowNum() + 1 + i, ef.title(), "证件号,手机号都已存在");
                          ei.getExcelErrorList().add(error);
                      }
                  }else{
                      ExcelField ef = ExcelUtil.getExcelField(bpOwnerImport, "idNo");
                      ExcelError error = new ExcelError(ei.getSheetName(), ei.getDataRowNum() + 1 + i, ef.title(), "手机号已存在，证件号与原有值【"+mobileBp.getIdNo()+"】不一致");
                      ei.getExcelErrorList().add(error);
                  }
              }else{
                  if(null!=idNoBp){
                      //报错
                      ExcelField ef = ExcelUtil.getExcelField(bpOwnerImport, "mobile");
                      ExcelError error = new ExcelError(ei.getSheetName(), ei.getDataRowNum() + 1 + i, ef.title(), "证件号已存在，手机号与原有值【"+idNoBp.getMobile()+"】不一致");
                      ei.getExcelErrorList().add(error);
                  }else{
                      //新增
                  }
              }
	        
//	        if(null!=mobileBp){
//		        if(idType.equals(mobileBp.getIdType())&&idNo.equals(mobileBp.getIdNo())){
//			        updateFlag=true;
//					//更新
//			        bpOwnerImport.setBpId(mobileBp.getBpId());
//		        }else{
//			        ExcelField ef = ExcelUtil.getExcelField(bpOwnerImport, "idNo");
//			        ExcelError error = new ExcelError(ei.getSheetName(), ei.getDataRowNum() + 1 + i, ef.title(), "手机号已存在，证件号与原有值【"+mobileBp.getIdNo()+"】不一致");
//			        ei.getExcelErrorList().add(error);
//		        }
//	        }else{
//		        BpGeneral idNoBp = bpGeneralMapper.getByIdNo(idNo, idType);
//		        if(null!=idNoBp) {
//			        if (mobile.equals(idNoBp.getMobile())) {
//				        updateFlag=true;
//				        bpOwnerImport.setBpId(idNoBp.getBpId());
//			        } else {
//				        ExcelField ef = ExcelUtil.getExcelField(bpOwnerImport, "mobile");
//				        ExcelError error = new ExcelError(ei.getSheetName(), ei.getDataRowNum() + 1 + i, ef.title(), "证件号已存在，手机号与原有值【" + idNoBp.getMobile() + "】不一致");
//				        ei.getExcelErrorList().add(error);
//			        }
//		        }else{
//			        //新增
//		        }
//	        }
//
//	        if(updateFlag){
//		        //更新
//		        if (null != bpOwnerImport.getBuildingId()) {
//			        List<BpHouseMap> listBpHouseMap = bpHouseMapMapper.getByBpBuilding(bpOwnerImport.getBpId(), bpOwnerImport.getBuildingId());
//			        if (!StringUtils.isEmpty(listBpHouseMap)) {
//				        ExcelField ef = ExcelUtil.getExcelField(bpOwnerImport, "structureCode");
//				        ExcelError error = new ExcelError(ei.getSheetName(), ei.getDataRowNum() + 1 + i, ef.title(), "业主与房屋关系已存在，不能重复导入。");
//				        ei.getExcelErrorList().add(error);
//			        }
//		        }
//	        }
        }
        logger.debug("校验导入数据结束......");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveImportDate(List<BpOwnerImport> list) {
        if (list != null && !list.isEmpty()) {
            logger.debug("保存数据开始......");
            for (BpOwnerImport bpOwnerImport : list) {
	                // 业主基表
	                BpGeneral general = new BpGeneral();
	                String idType = bpOwnerImport.getIdType();
	                String idNo = bpOwnerImport.getIdNo();
	                String bpName = bpOwnerImport.getBpName();
	                String mobile = bpOwnerImport.getMobile();
	                String gender = bpOwnerImport.getGender();
	                general.setBpName(bpName);
	                general.setIdType(idType);
	                general.setIdNo(idNo);
	                general.setMobile(mobile);
	                general.setGender(gender);
	                BpOwner owner = new BpOwner();
	                if(bpOwnerImport.getBpId()!=null){
	                    general.setBpId(bpOwnerImport.getBpId());
	                    bpGeneralMapper.updateByPrimaryKeySelective(general);
	                }else{
	                    //新增
	                    general.setBpCode(createBpCode());
	                    bpGeneralMapper.insertSelective(general);
	                    owner.setBpId(general.getBpId());
	                    bpOwnerMapper.insertSelective(owner);
	                }
                }
            }

            // 关系表处理
            for (BpOwnerImport bpOwnerImport : list) {
                String idNo = bpOwnerImport.getIdNo();
                Long ownerId = bpOwnerMapper.getOwnerIdByIdNo(idNo);
                String effectiveStartString = bpOwnerImport.getEffectiveStartDate();
                String effectiveEndString = bpOwnerImport.getEffectiveEndDate();
                Date effectiveStartDate = null;
                Date effectiveEndDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 小写的mm表示的是分钟
                try {
                    effectiveStartDate = sdf.parse(effectiveStartString);
                    effectiveEndDate = sdf.parse(effectiveEndString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                BpHouseMap bpHouse = new BpHouseMap();
                bpHouse.setBpType("OWNER");
                bpHouse.setBpExtId(ownerId);
                bpHouse.setBuildingType("HOUSING");
                bpHouse.setEffectiveStartDate(effectiveStartDate);
                bpHouse.setEffectiveEndDate(effectiveEndDate);
                bpHouse.setBuildingId(bpOwnerImport.getBuildingId());
                bpHouseMapMapper.insertSelective(bpHouse);
            }
            logger.debug("保存数据结束......");
        }

    @Override
    public List<BpOwner> selectCustomer(IRequest requestContext, Long id, int page, int pageSize) {
        return bpOwnerMapper.selectCustomer(id);
    }

    @Override
    public List<BpOwner> findGeneralByBuildingId(Long buildingId) {
        return bpOwnerMapper.findGeneralByBuildingId(buildingId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<BpOwner> saveOrUpdate(IRequest requestCtx, List<BpOwner> dto) throws CustomerInfoException {
        if (!dto.isEmpty()) {
            for (BpOwner owner : dto) {
                String __status = owner.get__status();
                String bpName = owner.getBpName();
                String idType = owner.getIdType();
                String idNo = owner.getIdNo();
                String mobile = owner.getMobile();
                String phone = owner.getPhone();
                String email = owner.getEmail();
                List<BpHouseMap> houseList = owner.getHouseMapList();
                //校验数据
                if ("I".equals(idType)) {
                    boolean idCard = IdcardUtils.validateCard(idNo);
                    if (!idCard) {
                        throw new CustomerInfoException(CustomerInfoException.MSG_ERROR_ID_CARD_ERROR,CustomerInfoException.MSG_ERROR_ID_CARD_ERROR,null);
                    }
                    }
                
                boolean checkMobile = RegExpValidatorUtils.checkMobile(mobile);
                if(!checkMobile){
                    throw new CustomerInfoException(CustomerInfoException.MSG_ERROR_MOBILE_ERROR,CustomerInfoException.MSG_ERROR_MOBILE_ERROR,null);
                }
                if(houseList==null || houseList.isEmpty()){
                    throw new CustomerInfoException(CustomerInfoException.MSG_ERROR_HOUSEMAP_ERROR,CustomerInfoException.MSG_ERROR_HOUSEMAP_ERROR,null);
                }
                
                BpGeneral general = new BpGeneral();
                general.setBpName(bpName);
                general.setIdType(idType);
                general.setIdNo(idNo);
                general.setMobile(mobile);
                general.setPhone(phone);
                general.setEmail(email);
                general.setGender(owner.getGender());
                general.setBirthday(owner.getBirthday());
                
                if (DTOStatus.ADD.equals(__status)) {
                  //校验重复信息，1.手机号
	                BpGeneral mobileBp = bpGeneralMapper.getUnionMobile(mobile);
                    if(mobileBp!=null){
                        throw new CustomerInfoException(CustomerInfoException.MSG_ERROR_MOBILE_REPEAT,CustomerInfoException.MSG_ERROR_MOBILE_REPEAT,null);
                    }
                    //校验重复信息，身份证
	                BpGeneral idNoBp = bpGeneralMapper.getByIdNo(idNo,idType);
                    if(idNoBp!=null){
                        throw new CustomerInfoException(CustomerInfoException.MSG_ERROR_IDNO_REPEAT,CustomerInfoException.MSG_ERROR_IDNO_REPEAT,null);
                    }
                    
                    general.setBpCode(createBpCode());
                    bpGeneralMapper.insertSelective(general);
                    BpOwner bpOwner = new BpOwner();
                    bpOwner.setBpId(general.getBpId());
                    bpOwnerMapper.insertSelective(bpOwner);
                    Long bpOwnerId = bpOwner.getBpOwnerId();
                    List<BpHouseMap> houseMapList = houseList;
                    if (!houseMapList.isEmpty()) {
                        for (BpHouseMap house : houseMapList) {
                            house.setBpExtId(bpOwnerId);
                            house.setBpType("OWNER");
                            house.setBuildingType("HOUSING");
                            bpHouseMapMapper.insertSelective(house);
                        }
                    }
                } else if (DTOStatus.UPDATE.equals(__status)) {
                    general.setBpId(general.getBpId());
                    bpGeneralMapper.updateByPrimaryKeySelective(general);
                }
            }
        }
        return dto;
    }

    @Override
    @Transactional
    public void deleteOwner(List<BpOwner> dto) {
        if(dto!=null){
            for(BpOwner owner : dto){
                Long bpOwnerId = owner.getBpOwnerId();
                Long bpId = owner.getBpId();
                if(bpOwnerId!=null){
                    bpOwnerMapper.deleteByPrimaryKey(owner);
                    bpHouseMapMapper.deleteByExtId(bpOwnerId);
                }
                if(bpId!=null){
                    bpGeneralMapper.deleteByBpId(bpId);
                }
            }
        }
    }

    @Override
    public List<BpOwner> queryOwners(IRequest requestContext, BpOwner dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return bpOwnerMapper.queryOwners(dto);
    }
    
    String createBpCode(){
        Long nextCode = bpGeneralMapper.getNextIndex();
        System.out.println("index:"+nextCode);
        return String.format("%1"+9+"d", nextCode);
    }
}
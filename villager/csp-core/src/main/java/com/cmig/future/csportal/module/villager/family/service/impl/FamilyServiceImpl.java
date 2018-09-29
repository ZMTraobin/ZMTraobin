package com.cmig.future.csportal.module.villager.family.service.impl;

import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.excel.ExcelError;
import com.cmig.future.csportal.common.utils.excel.ExcelUtil;
import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.common.utils.excel.annotation.ExcelField;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.villager.family.dto.AssetsInfo;
import com.cmig.future.csportal.module.villager.family.dto.FamilyImport;
import com.cmig.future.csportal.module.villager.family.dto.FamilyInfo;
import com.cmig.future.csportal.module.villager.family.dto.HousingInfo;
import com.cmig.future.csportal.module.villager.family.dto.LandInfo;
import com.cmig.future.csportal.module.villager.family.mapper.AssetsInfoMapper;
import com.cmig.future.csportal.module.villager.family.mapper.FamilyInfoMapper;
import com.cmig.future.csportal.module.villager.family.mapper.HousingInfoMapper;
import com.cmig.future.csportal.module.villager.family.mapper.LandInfoMapper;
import com.cmig.future.csportal.module.villager.family.service.IFamilyService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
@Transactional
public class FamilyServiceImpl extends BaseServiceImpl<FamilyInfo> implements IFamilyService {

    @Autowired
    private FamilyInfoMapper familyInfoMapper;
    @Autowired
    private LandInfoMapper landInfoMapper;
    @Autowired
    private HousingInfoMapper houseInfoMapper;
    @Autowired
    private AssetsInfoMapper assetsInfoMapper;
    @Autowired
    private IAppUserService iAppUserService;

    private static Logger logger = LoggerFactory.getLogger(FamilyServiceImpl.class);

    @Override
    public void validationImportData(ImportExcel ei, List<FamilyImport> list) {
        logger.debug("校验导入数据开始......");
        // 校验数据重复
        ExcelUtil.duplicateMutiColumsValidator(ei.getSheetName(),ei.getDataRowNum(), list, ei.getExcelErrorList(), new String[] {"mobile","idcard"}, FamilyImport.class);
        //校验数据格式
        for (int i = 0; i < list.size(); i++) {
        	FamilyImport familyImport =  list.get(i);
        	String mobile = String.valueOf(familyImport.getMobile());//手机
            String uname = familyImport.getUserName();//姓名
            String familyRelation = familyImport.getFamilyRelation();//成员关系
            String villageCode = familyImport.getVillageCode();//村编码
            String householderMobile = String.valueOf(familyImport.getHouseholderMobile());//户主手机号
            String age = String.valueOf(familyImport.getAge());//年龄
            String population = String.valueOf(familyImport.getPopulation());//人口数量
            String houseFloors = String.valueOf(familyImport.getHouseFloors());//楼层
            String buildYear = String.valueOf(familyImport.getBuildYear());//建造年份
            
            if (StringUtils.isEmpty(mobile) || !RegExpValidatorUtils.checkMobile(mobile)) {
                ExcelField ef = ExcelUtil.getExcelField(familyImport, "mobile");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "手机号格式不正确。");
                ei.getExcelErrorList().add(error);
            }
            if(StringUtils.isEmpty(uname)){
            	ExcelField ef = ExcelUtil.getExcelField(familyImport, "userName");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "姓名不能为空。");
                ei.getExcelErrorList().add(error);
            }
            if(StringUtils.isEmpty(familyRelation)){
            	ExcelField ef = ExcelUtil.getExcelField(familyImport, "familyRelation");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "成员关系不能为空。");
                ei.getExcelErrorList().add(error);
            }
            if(StringUtils.isEmpty(villageCode)){
            	ExcelField ef = ExcelUtil.getExcelField(familyImport, "villageCode");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "村落编码不能为空。");
                ei.getExcelErrorList().add(error);
            }
            if (StringUtils.isEmpty(householderMobile) || !RegExpValidatorUtils.checkMobile(householderMobile)) {
                ExcelField ef = ExcelUtil.getExcelField(familyImport, "householderMobile");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "户主手机号格式不正确。");
                ei.getExcelErrorList().add(error);
            }
            if(!StringUtils.isNumeric(age)){
            	ExcelField ef = ExcelUtil.getExcelField(familyImport, "age");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "年龄格式不正确。");
                ei.getExcelErrorList().add(error);
            }
            if(!StringUtils.isNumeric(population)){
            	ExcelField ef = ExcelUtil.getExcelField(familyImport, "population");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "人口数量格式不正确。");
                ei.getExcelErrorList().add(error);
            }
            if(!StringUtils.isNumeric(houseFloors)){
            	ExcelField ef = ExcelUtil.getExcelField(familyImport, "houseFloors");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "楼层格式不正确。");
                ei.getExcelErrorList().add(error);
            }
            if(!StringUtils.isNumeric(buildYear)){
            	ExcelField ef = ExcelUtil.getExcelField(familyImport, "buildYear");
                ExcelError error = new ExcelError(ei.getSheetName(),ei.getDataRowNum() + 1 + i, ef.title(), "建造年份格式不正确。");
                ei.getExcelErrorList().add(error);
            }
        }
        logger.debug("校验导入数据结束......");
    }

    @Override
    @Transactional(readOnly = false)
    public void saveImportDate(List<FamilyImport> list){
        logger.debug("导入数据持久化开始......");
        try {
        for (int i = 0; i < list.size(); i++) {
        	FamilyImport familyImport =  list.get(i);
            
            // 新增家庭信息
        	//户主手机号转码为32位串码（家庭编码）
	        String encodeStr=DigestUtils.md5Hex(String.valueOf(familyImport.getHouseholderMobile()));
        	// 按照手机号查找数据
        	FamilyInfo finfo = familyInfoMapper.selectByFamilyCode(familyImport.getMobile());
        	// 如果手机号存在不做处理，不存在则插入。
        	if (finfo == null) {
        		FamilyInfo info = new FamilyInfo();
    			BeanUtils.copyProperties(info,familyImport);
    			info.setFamilyCode(encodeStr);
    			
    			info.setVillageId("1");//通过编码获取村子表中的id++++++++++++++++++++++++
    			
                familyInfoMapper.insertSelective(info);
                // 新增用户表信息
                iAppUserService.importUser(info.getMobile().toString(),info.getUserName(),info.getVillageCode());
        	}
            
            // 新增土地信息
            // 按照家庭编码查找数据
    		LandInfo linfo=landInfoMapper.selectByFamilyCode(encodeStr);
    		// 如果家庭编码存在不做处理，不存在则插入。
            if (linfo == null) {
            	LandInfo landInfo = new LandInfo();
            	BeanUtils.copyProperties(landInfo,familyImport);
                landInfo.setFamilyCode(encodeStr);
                landInfoMapper.insertSelective(landInfo);
            }
            
            // 新增房屋信息
            // 按照家庭编码查找数据
            HousingInfo hinfo=houseInfoMapper.selectByFamilyCode(encodeStr);
            // 如果家庭编码存在不做处理，不存在则插入。
            if (linfo == null) {
            	HousingInfo houseInfo = new HousingInfo();
                BeanUtils.copyProperties(houseInfo,familyImport);
                houseInfo.setFamilyCode(encodeStr);
                houseInfoMapper.insertSelective(houseInfo);
            }
            
            // 新增资金信息
            // 按照家庭编码查找数据
            AssetsInfo ainfo=assetsInfoMapper.selectByFamilyCode(encodeStr);
            // 如果家庭编码存在不做处理，不存在则插入。
            if (linfo == null) {
            	AssetsInfo assetsInfo = new AssetsInfo();
                BeanUtils.copyProperties(assetsInfo,familyImport);
                assetsInfo.setFamilyCode(encodeStr);
                assetsInfoMapper.insertSelective(assetsInfo);
            }
            
        }
        } catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
        logger.debug("导入数据持久化结束......共导入:"+list.size()+" 条数据");
    }

}
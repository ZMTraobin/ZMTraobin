package com.cmig.future.csportal.module.properties.base.contacter.service.impl;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.IdcardUtils;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.properties.base.contacter.mapper.BpOwnerContacterMapper;
import com.cmig.future.csportal.module.properties.base.customer.CustomerInfoException;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpGeneral;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpHouseMap;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpOwner;
import com.cmig.future.csportal.module.properties.base.customer.mapper.BpGeneralMapper;
import com.cmig.future.csportal.module.properties.base.customer.mapper.BpHouseMapMapper;
import com.cmig.future.csportal.module.properties.base.customer.mapper.BpOwnerMapper;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.properties.base.contacter.dto.BpOwnerContacter;
import com.cmig.future.csportal.module.properties.base.contacter.service.IBpOwnerContacterService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BpOwnerContacterServiceImpl extends BaseServiceImpl<BpOwnerContacter> implements IBpOwnerContacterService{

    @Autowired
    private BpHouseMapMapper bpHouseMapMapper;
    @Autowired
    private BpOwnerContacterMapper bpOwnerContacterMapper;
    @Autowired
    private BpGeneralMapper bpGeneralMapper;
    @Autowired
    private BpOwnerMapper bpOwnerMapper;


    @Override
    public List<BpOwnerContacter> getContacterList(String userId, String communityId,Integer page, Integer pageSize) {
        //PageHelper.startPage(page,pageSize);
        if(StringUtils.isEmpty(communityId)){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"小区id");
        }
        return bpOwnerContacterMapper.getContacterList(userId, communityId);
    }

    @Override
    public BpOwnerContacter getContacterDetail(String userId,Long ownerContacterId,String communityId) {
        if(StringUtils.isEmpty(ownerContacterId)){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"关系人id");
        }
        if(StringUtils.isEmpty(communityId)){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"小区id");
        }
        return bpOwnerContacterMapper.selectById(userId,ownerContacterId,communityId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveContacter(BpOwnerContacter contacter) throws ServiceException {
        if (StringUtils.isEmpty(contacter.getContacterName())) {
            throw new DataWarnningException("contacterName:" + contacter.getContacterName() + "联系人名称为空");
        }
        if (StringUtils.isEmpty(contacter.getMobile())) {
            throw new DataWarnningException("mobile:" + contacter.getMobile() + "联系人手机为空");
        }
        if (StringUtils.isEmpty(contacter.getType())) {
            throw new DataWarnningException("type:" + contacter.getType() + "联系人类型为空");
        }
        if (StringUtils.isEmpty(contacter.getCommunityId())) {
            throw new DataWarnningException("communityId:" + contacter.getCommunityId() + "小区为空");
        }
        //校验身份证，手机号格式
        String mobile = contacter.getMobile();
        boolean checkMobile = RegExpValidatorUtils.checkMobile(mobile);
        if(!checkMobile){
            throw new ServiceException(RestApiExceptionEnums.MOBILE_FORMAT_ERROR);
        }

        String idNo = contacter.getIdNo();
        if(!StringUtil.isEmpty(idNo)){
            boolean idCard = IdcardUtils.validateCard(idNo);
            if (!idCard) {
                throw new ServiceException(RestApiExceptionEnums.ID_FORMAT_ERROR);
            }
        }


        Long id = contacter.getOwnerContacterId();
        if(id != null){
            bpOwnerContacterMapper.updateByPrimaryKeySelective(contacter);
            //更新联系人房屋，先删除，后增加
            bpHouseMapMapper.deleteContacter(contacter.getOwnerContacterId(),contacter.getCommunityId());
        }else{
            //根据appUserId查询业主id
            Long bpOwnerId = null;
            String token = contacter.getToken();
            String userId= UserTokenUtils.getUserIdByToken(token);
            System.out.print("user id:"+userId);
            List<BpOwner> ownerList = bpOwnerMapper.getByUserId(userId);

            if(!ownerList.isEmpty()){
                bpOwnerId = ownerList.get(0).getBpOwnerId();
            }

            if(bpOwnerId!=null){
                contacter.setBpOwnerId(bpOwnerId);
            }else{
                //业主id错误
                throw new ServiceException(RestApiExceptionEnums.GET_OWNER_ID);
            }
            //校验唯一
            Long ownerId = contacter.getBpOwnerId();
            Integer count = bpOwnerContacterMapper.checkUnique(ownerId,mobile);
            if(count > 0){
                throw new ServiceException(RestApiExceptionEnums.CONTACTER_MOBILE_REPEAT);
            }
            String contacterName = contacter.getContacterName();
            String type = contacter.getType();
            bpOwnerContacterMapper.insertSelective(contacter);
        }
        //增加房屋信息
        List<BpHouseMap> ownerHouse = contacter.getOwnerHouse();
        if(!ownerHouse.isEmpty()){
            for(BpHouseMap houseMap :ownerHouse){
                BpHouseMap map = new BpHouseMap();
                map.setBpExtId(contacter.getOwnerContacterId());
                map.setBpType("CONTACTER");
                map.setBuildingType("HOUSING");
                map.setBuildingId(houseMap.getHouseId());
                //设置有效期，和业主的有效期保持一致
                map.setEffectiveStartDate(houseMap.getEffectiveStartDate());
                map.setEffectiveEndDate(houseMap.getEffectiveEndDate());
                map.setStatus("Y");
                map.setAuthenticateStatus("N");
                bpHouseMapMapper.insertSelective(map);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteContacter(BpOwnerContacter contacter) {
        //1.删除联系人表，2.删除联系人-房屋关系表
        if (StringUtils.isEmpty(contacter.getOwnerContacterId())) {
            throw new DataWarnningException("ownerContacterId:" + contacter.getOwnerContacterId() + "联系人id为空");
        }
        if (StringUtils.isEmpty(contacter.getCommunityId())) {
            throw new DataWarnningException("communityId:" + contacter.getCommunityId() + "小区为空");
        }
        Long ownerContacterId = contacter.getOwnerContacterId();
        bpOwnerContacterMapper.deleteByPrimaryKey(contacter);
        bpHouseMapMapper.deleteContacter(ownerContacterId,contacter.getCommunityId());
    }
}
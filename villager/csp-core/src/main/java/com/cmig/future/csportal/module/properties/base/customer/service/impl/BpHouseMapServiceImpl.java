package com.cmig.future.csportal.module.properties.base.customer.service.impl;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.mapper.OrderFormMapper;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpHouseMap;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpOwner;
import com.cmig.future.csportal.module.properties.base.customer.mapper.BpGeneralMapper;
import com.cmig.future.csportal.module.properties.base.customer.mapper.BpHouseMapMapper;
import com.cmig.future.csportal.module.properties.base.customer.mapper.BpOwnerMapper;
import com.cmig.future.csportal.module.properties.base.customer.service.IBpHouseMapService;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BpHouseMapServiceImpl extends BaseServiceImpl<BpHouseMap> implements IBpHouseMapService {

    @Autowired
    private BpHouseMapMapper bpHouseMapMapper;
    @Autowired
    private BpGeneralMapper bpGeneralMapper;
    @Autowired
    private OrderFormMapper orderFormMapper;
    @Autowired
    private BpOwnerMapper bpOwnerMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, String> identifyHouse(BpHouseMap houseMap) {
        Map<String, String> result = new HashMap<String, String>();
	    //业主联系人认证
	    //1、验证手机号+房屋 查list,
        // 先根据手机号找到业主信息，在根据业主信息和房屋信息查询有没有匹配记录，若有则认证成功，没有则返回失败信息
        String mobile = houseMap.getMobile();
        Long buildingId = houseMap.getBuildingId();
//        List<BpGeneral> byMobileList = bpGeneralMapper.getByMobile(mobile);
//        if(byMobileList.isEmpty()){
//            result.put("status", "E");
//            result.put("msg", "认证失败，未找到有此手机号的业主信息");
//            return result;
//        }
//        Long bpOwnerId = byMobileList.get(0).getBpOwnerId();
//        houseMap.setBpExtId(bpOwnerId);
        BpHouseMap map = bpHouseMapMapper.identifyHouse(mobile, buildingId);
        if (map != null) {
            map.setAuthenticateStatus("Y");
            map.setAppUserId(houseMap.getAppUserId());
            bpHouseMapMapper.updateByPrimaryKeySelective(map);
            result.put("status", "S");
            result.put("msg", "认证成功");
            return result;
        } else {
            result.put("status", "E");
            result.put("msg", "认证失败，该业主没有此房屋信息");
            return result;
        }
    }

    @Override
    public List<BpHouseMap> queryByBpId(IRequest requestContext, Long bpId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return bpHouseMapMapper.queryByBpId(bpId);
    }

    @Override
    public List<BpHouseMap> queryByUserId(MgtReceivableDetail detail) {
        Date sysdate = new Date();
        String userId=UserTokenUtils.getUserIdByToken(detail.getToken());
        String communityId = detail.getCommunityId();
        if(StringUtils.isEmpty(communityId)){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"小区id");
        }
        return bpHouseMapMapper.queryByUserId(userId,communityId,sysdate);
    }

    @Override
    public List<MgtReceivableDetail> queryBillsFirstType(MgtReceivableDetail detail, int page, int pageSize) {
        return bpHouseMapMapper.queryBillsFirstType(detail);
    }

    @Override
    public List<MgtReceivableDetail> queryBills(MgtReceivableDetail detail, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return bpHouseMapMapper.queryBills(detail);
    }

    @Override
    public Long queryUncalledFee(MgtReceivableDetail detail) {
        return bpHouseMapMapper.queryUnCalledFee(detail);
    }

    @Override
    public Long queryPayableFee(MgtReceivableDetail detail) {
        return bpHouseMapMapper.queryPayableFee(detail);
    }

    @Override
    public MgtReceivableDetail queryPeriod(MgtReceivableDetail detail) {
        return bpHouseMapMapper.queryPeriod(detail);
    }

    @Override
    public List<MgtReceivableDetail> getMyBill(MgtReceivableDetail detail,Integer page, Integer pageSize) {
        if (StringUtils.isEmpty(detail) || StringUtils.isEmpty(detail.getCommunityId())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL, "小区id");
        }
        //我的缴费：区分业主和租户
        String userId = UserTokenUtils.getUserIdByToken(detail.getToken());
        String communityId = detail.getCommunityId();
        return bpHouseMapMapper.queryByBuildingId(communityId, userId);
//        Long buildingId = detail.getBuildingId();
//        String houseType = bpHouseMapMapper.getHouseType(userId,buildingId);
//        PageHelper.startPage(page,pageSize);
//        if("OWNER".equals(houseType)){
//            //房间的所有缴费信息
//            return bpHouseMapMapper.queryByBuildingIdOwner(buildingId);
//        }else{
//            //房屋下租户的缴费信息
//            return bpHouseMapMapper.queryByBuildingId(buildingId,userId);
//        }
    }

    @Override
    public List<OrderForm> getMyAllBill(MgtReceivableDetail detail,Integer page, Integer pageSize) {
        String userId=UserTokenUtils.getUserIdByToken(detail.getToken());
        PageHelper.startPage(page, pageSize);
        return orderFormMapper.getAllByUserId(userId);
    }

    @Override
    public String queryOwnerName(Long buildingId) {
        return bpHouseMapMapper.queryOwnerName(buildingId, new Date());
    }

    @Override
    public OrderForm getOrderDetail(OrderForm order) {
        if(StringUtils.isEmpty(order)||StringUtils.isEmpty(order.getId())){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"订单id");
        }
        return orderFormMapper.selectOderDetail(order.getId());
    }

    @Override
    public List<BpHouseMap> saveOrUpdate(IRequest requestCtx, List<BpHouseMap> dto) {
        if(!dto.isEmpty()){
            for(BpHouseMap map : dto){
                Long bpExtId = map.getBpExtId();
                Long buildingId = map.getBuildingId();
                int byExtBuilding = bpHouseMapMapper.getByExtBuilding(bpExtId,buildingId);
                if(byExtBuilding > 0){
                    throw new ServiceException(RestApiExceptionEnums.OWNER_BUILD_REPEAT,"业主与房屋关系还未失效，请先失效，再添加");
                }
                bpHouseMapMapper.insertSelective(map);
            }
        }
        return dto;
    }

    @Override
    public Boolean isOwner(String userId) {
        List<BpOwner> owner = bpOwnerMapper.getByUserId(userId);
        if(owner.isEmpty()){
            return Boolean.FALSE;
        }else {
            return Boolean.TRUE;
        }
    }

    @Override
    public List<BpHouseMap> getOwnerHouse(String userId, String communityId) {
        if(StringUtils.isEmpty(communityId)){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"小区id");
        }
        return bpHouseMapMapper.getOwnerHouse(userId,communityId);
    }

    @Override
    public Long queryCommunityBill(String communityId, String userId) {
        return bpHouseMapMapper.queryCommunityBills(communityId,userId);
    }

    @Override
    public void batchValid( List<BpHouseMap> dto ) {
        if(!dto.isEmpty()){
            for(BpHouseMap map : dto){
//                Long buildingId = map.getBuildingId();
//                bpHouseMapMapper.lossValid(buildingId);
                Long mapId = map.getMapId();
                bpHouseMapMapper.valiadByMapId(mapId);

            }
        }
    }

    @Override
    public void excuteDeleteHouseMapDataJob() {
        List<BpHouseMap> list = bpHouseMapMapper.queryValidData();
        if(!list.isEmpty()){
            for (BpHouseMap map : list){
                Long mapId = map.getMapId();
                bpHouseMapMapper.valiadByMapId(mapId);
            }
        }
    }

}
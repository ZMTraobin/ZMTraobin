package com.cmig.future.csportal.module.properties.base.customer.service;

import java.util.List;
import java.util.Map;

import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpHouseMap;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

public interface IBpHouseMapService extends IBaseService<BpHouseMap>, ProxySelf<IBpHouseMapService> {

    Map<String, String> identifyHouse(BpHouseMap houseMap);

    List<BpHouseMap> queryByBpId(IRequest requestContext, Long bpId, int page, int pageSize);

    List<BpHouseMap> queryByUserId(MgtReceivableDetail detail);

    String queryOwnerName(Long buildingId);

    List<MgtReceivableDetail> queryBillsFirstType(MgtReceivableDetail detail, int page, int pageSize);

    List<MgtReceivableDetail> queryBills(MgtReceivableDetail detail, int page, int pageSize);

    Long queryUncalledFee(MgtReceivableDetail detail);

    Long queryPayableFee(MgtReceivableDetail detail);

    MgtReceivableDetail queryPeriod(MgtReceivableDetail detail);

    List<MgtReceivableDetail> getMyBill(MgtReceivableDetail detail, Integer page, Integer pageSize);

    List<OrderForm> getMyAllBill(MgtReceivableDetail detail, Integer page, Integer pageSize);

    OrderForm getOrderDetail(OrderForm order);

    List<BpHouseMap> saveOrUpdate(IRequest requestCtx, List<BpHouseMap> dto);

    Boolean isOwner(String userId);

    List<BpHouseMap> getOwnerHouse(String userId, String communityId);

    Long queryCommunityBill(String communityId, String userId);

    void batchValid(List<BpHouseMap> dto);

    /**
     * 定时任务，自动失效到期的房屋-业主关系
     */
    void excuteDeleteHouseMapDataJob();
}
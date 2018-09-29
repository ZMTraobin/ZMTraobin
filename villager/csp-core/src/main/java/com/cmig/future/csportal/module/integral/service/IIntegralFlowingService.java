package com.cmig.future.csportal.module.integral.service;
import com.cmig.future.csportal.module.integral.dto.IntegralFlowing;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IIntegralFlowingService extends IBaseService<IntegralFlowing>, ProxySelf<IIntegralFlowingService>{
    /**
     * 分页查询所有的积分流水信息
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
   List<IntegralFlowing> selectAllIntegral(IRequest requestContext, IntegralFlowing dto, int page, int pageSize);

    /**
     * 保存积分流水明细
     * @param dto
     */
    void save(IntegralFlowing dto);

    /**
     * 根据id获取IntegralFlowing对象
     * @param dto
     * @return
     */
    IntegralFlowing getIntegralFlowingById(IntegralFlowing dto);

    /**
     * 更新状态
     * @param dto
     */
    void updateIntegralFlowingStatus(IntegralFlowing dto);

    /**
     * 根据订单号和场景类型查看积分详细信息
     * @param dto
     * @return
     */
    List<IntegralFlowing>  getIntegralByTypeAndOutTradeno(IntegralFlowing dto);

    /**
     * 根据uid和sign查询IntegralFlowing 信息
     * @param dto
     * @return
     */
    IntegralFlowing selectIntegralByUidAndSign(IntegralFlowing dto);



}
package com.cmig.future.csportal.module.integral.mapper;
import com.cmig.future.csportal.module.integral.dto.IntegralFlowing;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface IntegralFlowingMapper extends Mapper<IntegralFlowing>{
    /**
     * 分页查询积分流水的信息
     * @param dto
     * @return
     */
    List<IntegralFlowing> selectAllIntegral(IntegralFlowing dto);


    /**
     * 根据订单号和场景类型查看积分详细信息
     * @param dto
     * @return
     */
    List<IntegralFlowing>  getIntegralByTypeAndOutTradeno(IntegralFlowing dto);

    /**
     * 根据uid和sign查询integralFlowing 信息
     * @param dto
     * @return
     */
    IntegralFlowing selectIntegralByUidAndSign(IntegralFlowing dto);

    /**
     * 更新积分状态
     * @param dto
     */
    void updateIntegralFlowingStatus(IntegralFlowing dto);

    /**
     * 根据主键获取IntegralFlowing信息
     * @param dto
     * @return
     */
    IntegralFlowing getIntegralFlowingById(IntegralFlowing dto);

}
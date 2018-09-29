package com.cmig.future.csportal.module.integral.service.impl;
import com.cmig.future.csportal.module.integral.dto.IntegralFlowing;
import com.cmig.future.csportal.module.integral.mapper.IntegralFlowingMapper;
import com.cmig.future.csportal.module.integral.service.IIntegralFlowingService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IntegralFlowingServiceImpl extends BaseServiceImpl<IntegralFlowing> implements IIntegralFlowingService {
    @Autowired
    private IntegralFlowingMapper integralFlowingMapper;

    /**
     * 分页查询积分流水的信息
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<IntegralFlowing> selectAllIntegral(IRequest requestContext, IntegralFlowing dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return integralFlowingMapper.selectAllIntegral(dto);
    }

    @Override
    public void save(IntegralFlowing dto) {
        integralFlowingMapper.insertSelective(dto);
    }

    /**
     * 根据主键获取IntegralFlowing对象
     * @param dto
     * @return
     */
    @Override
    public IntegralFlowing getIntegralFlowingById(IntegralFlowing dto) {
        return integralFlowingMapper.getIntegralFlowingById(dto);
    }

    /**
     * 更新状态
     * @param dto
     */
    @Override
    public void updateIntegralFlowingStatus(IntegralFlowing dto) {
        integralFlowingMapper.updateIntegralFlowingStatus(dto);
    }

    /**
     * 根据订单号和场景类型查询积分的详细信息
     * @param dto
     * @return
     */
    @Override
    public List<IntegralFlowing>  getIntegralByTypeAndOutTradeno(IntegralFlowing dto) {

        return integralFlowingMapper.getIntegralByTypeAndOutTradeno(dto);
    }

    /**
     * 根据uid和sign查询integralFlowing信息
     * @param dto
     * @return
     */
    @Override
    public IntegralFlowing selectIntegralByUidAndSign(IntegralFlowing dto) {
        return integralFlowingMapper.selectIntegralByUidAndSign(dto);
    }
}